package edu.carroll.initMusic.service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import edu.carroll.initMusic.InitMusicApplication;
import edu.carroll.initMusic.jpa.model.Song;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@Service
public class SearchService {
    /**
     * Logger object used for logging
     */
    private static final Logger log = LoggerFactory.getLogger(SearchService.class);

    public SearchService() {

    }

    public String convertDate(String date){
        return date.replace('-','/');
    }

    public Set<Song> searchForSongs(String query) {
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

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject track = dataArray.getJSONObject(i);
//                    for(String key: track.keySet()){
//                        System.out.println(key + ": " + track.get(key) + ": " + track.get(key).getClass());
//                    }
                    final Long id = track.getLong("id");
                    final String title = track.getString("title");
                    final int duration = track.getInt("duration");
                    //final String release_date = convertDate(track.getString("release_date"));

                    final String artistName = track.getJSONObject("artist").getString("name");
                    final long artistID = track.getJSONObject("artist").getLong("id");

                    final String albumName = track.getJSONObject("album").getString("title");
                    final long albumID = track.getJSONObject("album").getLong("id");

                    final String songImg = track.getJSONObject("album").getString("cover");
                    final String songPreview = track.getString("preview");

                    log.info("Found Song: D ID: {} | Track: {} | Artist: {} -id: {}  | Album: {} -id:{}" +
                            "| Duration: {}", id,title,artistName,artistID,albumName,albumID,
                            duration);
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

        return songsFound;
    }

    public static void main(String[] args) {
        // Create the application context
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(InitMusicApplication.class)) {
            SearchService songService = context.getBean(SearchService.class);

            Set<Song> songsFound = songService.searchForSongs("Zach Bryan");
            for(Song song : songsFound) {
                System.out.println(song);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
