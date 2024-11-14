package edu.carroll.initMusic.service;

import edu.carroll.initMusic.jpa.model.QueryCache;
import edu.carroll.initMusic.jpa.model.Song;
import edu.carroll.initMusic.jpa.repo.QueryCacheRepository;
import edu.carroll.initMusic.jpa.repo.SongRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This services handles Searching for songs using the deezer api.
 *
 * @author Nick Clouse
 *
 * @see <a href="https://developers.deezer.com/api">Deezer API</a>
 *
 * @since September 30, 2024
 */
@Service
public class SongServiceDeezerImpl implements SongService{
    /** Logger object used for logging */
    private static final Logger log = LoggerFactory.getLogger(SongServiceDeezerImpl.class);

    /** httpClient used */
    private HttpClient httpClient = HttpClient.newHttpClient();

    /** QueryCache repository */
    private final QueryCacheRepository queryCacheRepository;

    /** Song repository */
    private final SongRepository songRepository;

    /**
     * Constructor
     */
    public SongServiceDeezerImpl(QueryCacheRepository queryCacheRepository, SongRepository songRepository) {
        this.queryCacheRepository = queryCacheRepository;
        this.songRepository = songRepository;
    }

    /**
     * This method sets the httpClient to the given client. This is
     * currently only used in testing so a httpClient can be
     * mocked.
     * @param httpClient HttpClient to set
     */
    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Searches for songs related to given query. Using the Deezer API, this
     * searches for anything related to the query on Deezer. The API returns a
     * HttpResponse, and this function takes the body of it, and parses the data into song objects,
     * and returns a list of songs found related to the query.
     * @param query Query to search for
     * @return Set of songs related to query
     */
    @Transactional
    public Set<Song> searchForSongs(String query) {

        //Make sure there is text in query
        if (query == null || query.trim().isEmpty() || query.length() < 3) {
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
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

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
                log.error("searchForSongs: Error response from Deezer API: Status Code {}", response.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            log.error("searchForSongs: Network error occurred during search with query {}", query, e);
        } catch (JSONException e) {
            log.error("searchForSongs: JSON parsing error occurred with query {}", query, e);
        }

        createCache(query,songsFound);

        log.info("searchForSongs: Found {} songs for query '{}'",songsFound.size(),query);
        return songsFound;
    }

    /**
     * Gets the local cache with the given query, if there is one
     * @param query Query to search for
     * @return Set of songs related to given query, null if cache wasn't found
     *
     * @see QueryCache
     */
    @Override
    public Set<Song> getLocalCache(String query) {
        if(query == null || query.isEmpty()){
            return null;
        }
        query = query.strip().toLowerCase();
        final List<QueryCache> queryCacheList = queryCacheRepository.findQueryCacheByQueryIgnoreCase(query);
        if (queryCacheList != null && !queryCacheList.isEmpty()) {
            log.info("getLocalCache: Found query cache for {} with {} songs found", query, queryCacheList.getFirst().getResults().size());
            return queryCacheList.getFirst().getResults();
        }

        return null;
    }

    /**
     * Creates a new QueryCache with the given query and songs
     * @param query Query String
     * @param songs Songs found related to query
     *
     * @see QueryCache
     */
    public boolean createCache(String query, Set<Song> songs){
        if(query == null || query.isEmpty() || songs == null){
            return false;
        }
        if(queryCacheRepository == null){
            return false;
        }
        final QueryCache newCache;

        //Check if there is already a cache with the given query, if so, rewrite its data
        final List<QueryCache> queryCacheList = queryCacheRepository.findQueryCacheByQueryIgnoreCase(query);
        if (queryCacheList.size() == 1) {
            //cache found
            log.info("createCache: Editing found query cache for {} with {} songs found", query, queryCacheList.getFirst().getResults().size());
            newCache = queryCacheList.getFirst();
        }else{
            //No cache found
            log.info("createCache: Creating new cache object for query {}", query);
            newCache = new QueryCache();
        }

        query = query.strip().toLowerCase();
        newCache.setQuery(query);

        // Separate new and existing songs
        final Set<Song> newSongs = new HashSet<>();
        final Set<Song> allSongsForCache = new HashSet<>();

        //Go through each song and check if its in repository yet
        for (Song song : songs) {
            final List<Song> songFound = songRepository.findByDeezerID(song.getDeezerID());

            if (songFound.isEmpty()) {  //Song is new
                newSongs.add(song);
                song.addQueryCache(newCache);  //Link new song to new cache
            } else {  //Song exists in DB
                final Song existingSong = songFound.getFirst();
                existingSong.addQueryCache(newCache);  //Link existing song to new cache
                allSongsForCache.add(existingSong);  //Add existing song to final set for cache
            }
        }

        //Save the new cache, have to do this here as well
        //as below so each song doesn't try to save a new queryCache with the same data
        queryCacheRepository.save(newCache);

        //Persist only new songs
        if (!newSongs.isEmpty()) {
            songRepository.saveAll(newSongs);
        }

        //Add both new and old songs to the cache results
        allSongsForCache.addAll(newSongs);  //Combine new and existing songs for cache
        newCache.setResults(allSongsForCache);
        queryCacheRepository.save(newCache);

        log.info("Saved new cache with associated songs {}", newCache);
        return true;
    }
}
