package edu.carroll.initMusic.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import edu.carroll.initMusic.InitMusicApplication;
import edu.carroll.initMusic.jpa.model.Album;
import edu.carroll.initMusic.jpa.model.Song;
import edu.carroll.initMusic.jpa.model.Artist;
import edu.carroll.initMusic.jpa.repo.AlbumRepository;
import edu.carroll.initMusic.jpa.repo.ArtistRepository;
import edu.carroll.initMusic.jpa.repo.SongRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Service
public class SongPopulatingService {
    /**
     * Logger object used for logging
     */
    private static final Logger log = LoggerFactory.getLogger(SongPopulatingService.class);

    private final SongRepository songRepo;

    private final ArtistRepository artistRepo;

    private final AlbumRepository albumRepo;

    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    public SongPopulatingService(SongRepository songRepo, ArtistRepository artistRepo, AlbumRepository albumRepo,EntityManager entityManager) {
        this.songRepo = songRepo;
        this.artistRepo = artistRepo;
        this.albumRepo = albumRepo;
        this.entityManager = entityManager;
    }

    @PostConstruct
    @Transactional
    public void init() {
        createSongByID(854914322L);
        createSongByID(2814830342L);
        createSongByID(1070310902L);
        createSongByID(2417266345L);
        createSongByID(2417266175L);
    }

    public Artist createArtistByID(int id){
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.deezer.com/artist/" + id))
                .build();
        HttpResponse<String> response;

        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            final JSONObject jsonResponse = new JSONObject(response.body());
            final String artistName = jsonResponse.getString("name");

            // Create and save new artist
            log.info("Creating artist: {}", artistName);
            final Artist newArtist = new Artist(artistName);
            return artistRepo.save(newArtist); // Save and return new artist

        } catch (IOException | InterruptedException | JSONException e) {
            log.error("{} occurred during createArtistByID with ID {}", e, id);
        }
        return null;
    }

    public Album createAlbumByID(int id){
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.deezer.com/album/" + id))
                .build();
        HttpResponse<String> response;

        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            final JSONObject jsonResponse = new JSONObject(response.body());
            final String title = jsonResponse.getString("title");
            final String genre = jsonResponse.getJSONObject("genres").getJSONArray("data").getJSONObject(0).getString("name");
            final String releaseDate = convertDate(jsonResponse.getString("release_date"));

            final Album newAlbum = new Album(title, genre, releaseDate);
            log.info("Created album: {}", newAlbum);

            // Add associated artists
            final JSONArray artists = jsonResponse.getJSONArray("contributors");
            for (int i = 0; i < artists.length(); i++) {
                final String artistName = artists.getJSONObject(i).getString("name");
                List<Artist> foundArtists = artistRepo.findByArtistName(artistName);

                if (foundArtists.isEmpty()) {
                    Artist newArtist = createArtistByID(artists.getJSONObject(i).getInt("id"));
                    newAlbum.addArtist(newArtist);
                    newArtist.addAlbum(newAlbum);
                } else {
                    newAlbum.addArtist(foundArtists.getFirst());
                    foundArtists.getFirst().addAlbum(newAlbum);
                }
            }

            return newAlbum; // Save and return new album

        } catch (IOException | InterruptedException | JSONException e) {
            log.error("{} occurred during createAlbumByID with ID {}", e, id);
        }
        return null;
    }

    public Song createSongByID(Long id) {
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.deezer.com/track/" + id))
                .build();
        HttpResponse<String> response;

        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            final JSONObject jsonResponse = new JSONObject(response.body());

            final String title = jsonResponse.getString("title");
            final JSONArray contributors = jsonResponse.getJSONArray("contributors");
            final String searchName = contributors.getJSONObject(0).getString("name");

            // Check for existing song
            List<Song> songFound = songRepo.findBySongNameAndArtists_ArtistName(title, searchName);
            if (!songFound.isEmpty()) {
                return songFound.getFirst(); // Return first matching song
            }

            final int duration = jsonResponse.getInt("duration");
            final String releaseDate = convertDate(jsonResponse.getString("release_date"));
            final Song newSong = new Song(title, releaseDate, duration);

            // Add associated artists
            for (int i = 0; i < contributors.length(); i++) {
                final JSONObject artist = contributors.getJSONObject(i);
                final List<Artist> artistFound = artistRepo.findByArtistName(artist.getString("name"));
                Artist artistToUse;

                if (artistFound.isEmpty()) {
                    artistToUse = createArtistByID(artist.getInt("id"));
                } else {
                    artistToUse = artistFound.getFirst();
                    artistToUse = entityManager.merge(artistToUse);
                }

                newSong.addArtist(artistToUse);
                artistToUse.addSong(newSong);
            }

            // Handle album association
            final JSONObject album = jsonResponse.getJSONObject("album");
            final List<Album> albumsFound = albumRepo.findByArtists_ArtistNameAndAlbumName(searchName, album.getString("title"));
            Album albumToUse;

            if (albumsFound.isEmpty()) {
                albumToUse = createAlbumByID(album.getInt("id"));
                albumRepo.save(albumToUse);
            } else {
                albumToUse = albumsFound.getFirst();
                albumToUse = entityManager.merge(albumToUse);
            }

            newSong.setAlbum(albumToUse);
            albumToUse.addSong(newSong);

            //artistRepo.saveAllAndFlush(newSong.getArtists());
            songRepo.saveAndFlush(newSong);
            //List <Artist> artists = artistRepo.findAll();
            //artistRepo.saveAllAndFlush(artists);

            log.info("State before saving:");
            log.info("New Song: {}", newSong);
            log.info("Associated Artists: {}", newSong.getArtists());
            log.info("Associated Album: {}", newSong.getAlbum());

            return newSong;

        } catch (IOException | InterruptedException | JSONException e) {
            log.error("{} occurred during createSongByID with ID {}", e, id);
        }
        return null;
    }

    public String convertDate(String date){
        return date.replace('-','/');
    }

    public static void main(String[] args) {
        // Create the application context
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(InitMusicApplication.class)) {
            SongPopulatingService songService = context.getBean(SongPopulatingService.class);
            songService.createSongByID(854914322L); // Example song ID
            //album test id 127270232
//            songService.createAlbumByID(127270232);
            //artist test id = 13
//            songService.createArtistByID(13);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //854914322, 916414
    }
}
