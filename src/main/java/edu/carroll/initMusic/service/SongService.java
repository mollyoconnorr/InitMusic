package edu.carroll.initMusic.service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import edu.carroll.initMusic.InitMusicApplication;
import edu.carroll.initMusic.jpa.model.Playlist;
import edu.carroll.initMusic.jpa.model.Song;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.jpa.repo.PlaylistRepository;
import edu.carroll.initMusic.jpa.repo.SongRepository;
import edu.carroll.initMusic.jpa.repo.UserRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * <p>
 * This services handles everything related to songs, like
 * Searching for songs on Deezer and adding a song to a playlist
 * </p>
 *
 * @author Nick Clouse
 *
 * @since September 30, 2024
 */
@Service
public class SongService {
    /**
     * Logger object used for logging
     */
    private static final Logger log = LoggerFactory.getLogger(SongService.class);

    /**
     * Song repository
     */
    private final SongRepository songRepository;

    /**
     * Playlist repository
     */
    private final PlaylistRepository playlistRepository;

    /**
     * User Repository
     */
    private final UserRepository userRepository;

    /**
     * Constructor, injects several repositories
     * @param songRepository Song Repo to inject
     * @param playlistRepository Playlist Repo to inject
     * @param userRepository User repo to inject
     */
    public SongService(SongRepository songRepository, PlaylistRepository playlistRepository, UserRepository userRepository) {
        this.songRepository = songRepository;
        this.playlistRepository = playlistRepository;
        this.userRepository = userRepository;
    }

    /**
     * Searches for songs related to given query. Using the Deezer API, this
     * searches for anything related to the query on Deezer. The API returns a
     * HttpResponse, and this function takes the body of it, and parses the data into song objects,
     * and returns a list of songs found related to the query.
     * @param query Query to search for
     * @return Set of songs related to query
     */
    public Set<Song> searchForSongs(String query) {

        //Make sure there is test in query
        if (query.trim().isEmpty() || query.length() < 3) {
            return new HashSet<>();
        }

        final HttpRequest request;
        // Encode the query to handle special characters
        final String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        final String url = "https://api.deezer.com/search?q=" + encodedQuery;

        // Build the HTTP request
        request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        final HttpResponse<String> response;

        final Set<Song> songsFound = new HashSet<>();

        try {
            // Send the HTTP request and get the response
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            // Check if the response status code is 200 (OK)
            if (response.statusCode() == 200) {
                final JSONObject jsonResponse = new JSONObject(response.body());

                // Parse the JSON response
                JSONArray dataArray = jsonResponse.getJSONArray("data");

                //Take each object in the data array and convert it to a song object
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject track = dataArray.getJSONObject(i);

                    final Long id = track.getLong("id");
                    final String title = track.getString("title");
                    final int duration = track.getInt("duration");

                    final String artistName = track.getJSONObject("artist").getString("name");
                    final long artistID = track.getJSONObject("artist").getLong("id");

                    final String albumName = track.getJSONObject("album").getString("title");
                    final long albumID = track.getJSONObject("album").getLong("id");

                    final String songImg = track.getJSONObject("album").getString("cover");
                    final String songPreview = track.getString("preview");

                    final Song newSong = new Song(id,title,duration,artistName,artistID,albumName,albumID);
                    newSong.setSongImg(songImg);
                    newSong.setSongPreview(songPreview);
                    songsFound.add(newSong);
                }
            } else {
                log.error("Error response from Deezer API: Status Code {}", response.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            log.error("Network error occurred during search with query {}", query, e);
        } catch (JSONException e) {
            log.error("JSON parsing error occurred with query {}", query, e);
        }

        log.info("Found {} songs for query '{}'",songsFound.size(),query);
        return songsFound;
    }

    /**
     * Adds a song to the given playlist. This first searches for the playlist by id. It
     * should always find a playlist, because when used, it takes the playlist id directly from
     * a playlist object that has already been created.
     * @param playlistId ID of playlist to add song to
     * @param song Song to add to playlist
     * @return True if playlist was added, false if not
     */
    public boolean addSongToPlaylist(Long playlistId, Song song) {
        final List<Playlist> playlistsFound = playlistRepository.findByPlaylistIDEquals(playlistId);

        // Check if exactly one playlist was found
        if (playlistsFound.size() != 1) {
            return false; // Or throw an exception if that suits your design better
        }

        final Playlist playlist = playlistsFound.getFirst(); // Get the playlist from the list

        // Check if the song is already in the playlist
        if (playlist.containsSong(song)) {
            return false; // Song is already in the playlist
        }

        // Attempt to find the song in the repository
        final Optional<Song> songFound = songRepository.findById(song.getSongID());
        if (songFound.isPresent()) {
            // Add the managed song to the playlist
            playlist.addSong(songFound.get());
            songFound.get().addPlaylist(playlist);
        } else {
            // If the song does not exist, save it
            songRepository.save(song);
            playlist.addSong(song);
            song.addPlaylist(playlist);
        }

        // Save only the playlist, which will cascade the updates
        playlistRepository.save(playlist);

        log.info("Song {} added to playlist {}", song.getSongID(), playlist.getPlaylistID());
        return true;
    }


    /**
     * Gets user object from inputted username. All usernames are unique, so
     * there should only be 1 username found
     * @param username Username to use for search
     * @return User object found, null if nothing found, or too many were found.
     */
    public User getUser(String username){
        final List<User> user = userRepository.findByUsernameIgnoreCase(username);
        if(user.size() != 1){
            return null;
        }
        return user.getFirst();
    }

    /**
     * Main method used for testing, will get deleted eventually
     * @param args Args
     */
    public static void main(String[] args) {
        // Create the application context
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(InitMusicApplication.class)) {
            SongService songService = context.getBean(SongService.class);

            Set<Song> songsFound = songService.searchForSongs("Zach Bryan");
            for(Song song : songsFound) {
                System.out.println(song);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
