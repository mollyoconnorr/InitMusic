package edu.carroll.initMusic.service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import edu.carroll.initMusic.jpa.model.Song;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * This services handles everything related to songs, like
 * Searching for songs on Deezer and adding a song to a playlist
 *
 * @author Nick Clouse
 *
 * @since September 30, 2024
 */
@Service
public class SongServiceImpl implements SongService{
    /** Logger object used for logging */
    private static final Logger log = LoggerFactory.getLogger(SongServiceImpl.class);

    /**
     * Constructor
     */
    public SongServiceImpl() {
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
}
