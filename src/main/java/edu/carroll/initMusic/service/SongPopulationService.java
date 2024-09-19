package edu.carroll.initMusic.service;

import edu.carroll.initMusic.jpa.model.Song;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SongPopulationService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String DEEZER_API_URL = "https://api.deezer.com/";

    public List<Song> getSongsFromDeezer(String query, int limit, int index) {
        String url = DEEZER_API_URL + "search?q=" + query + "&limit=" + limit + "&index=" + index;
        ResponseEntity<DeezerResponse> response = restTemplate.getForEntity(url, DeezerResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody().getData();  // DeezerResponse should map the API response
        } else {
            return Collections.emptyList();
        }
    }

    public List<Song> getMultipleSongs(String query) {
        List<Song> allSongs = new ArrayList<>();
        int limit = 50;
        int index = 0;

        // Keep fetching until we have 1,000 songs
        while (allSongs.size() < 1000) {
            List<Song> fetchedSongs = getSongsFromDeezer(query, limit, index);
            if (fetchedSongs.isEmpty()) {
                break;  // Exit if no more songs are returned
            }
            allSongs.addAll(fetchedSongs);
            index += limit;  // Move to the next page
        }

        return allSongs;
    }
}
