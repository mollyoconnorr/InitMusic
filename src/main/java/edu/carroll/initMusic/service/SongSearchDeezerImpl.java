package edu.carroll.initMusic.service;

import edu.carroll.initMusic.jpa.model.Song;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.JaroWinklerDistance;
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
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Implementation of the SongSearchService Interface that uses the Deezer API
 * to search for songs.
 *
 * @see <a href="https://developers.deezer.com/api">Deezer API</a>
 */
@Service
public class SongSearchDeezerImpl implements SongSearchService {
    /**
     * Maximum length a query can be
     */
    private static final int MAX_QUERY_LENGTH = 40;

    /**
     * Minimum length a query can be
     */
    private static final int MIN_QUERY_LENGTH = 3;

    /**
     * Logger object used for logging
     */
    private static final Logger log = LoggerFactory.getLogger(SongSearchDeezerImpl.class);

    /**
     * JaroWinklerDistance object for calculating differences between strings. Used
     * when sorting songs so the songs are sorted by closest match to given parameters.
     */
    private final JaroWinklerDistance jaroWinkler = new JaroWinklerDistance();

    /**
     * Searches for songs related to given query. Using the Deezer API, this
     * searches for anything related to the query on Deezer. The API returns a
     * HttpResponse, and this function takes the body of it, and parses the data into song objects,
     * and returns a set of songs found related to the query. The method takes two parameters, the name
     * of the song and the name of the artist. At least one of them needs to be a non-empty string for the
     * search to be executed.
     *
     * <p>
     * If songs are found related to the given params, they are put into a tree set, which is a sorted
     * set. The set is sorted by the JaroWinklerDistance the song name or artist name is from the target song name or artist name.
     * If the distance is the same, it compares the Deezer ID of each song, which is always unique.
     * </p>
     *
     * @param songSearch   Name of song to search for (Target song name)
     * @param artistSearch Name of artist to search for (Target artist name)
     * @return Set of songs related to query, sorted either by song name or artist name. If a song name
     * was passed, sorted by song name. If just an artist name was passed, sorted by artist
     * @see TreeSet
     * @see JaroWinklerDistance
     */
    @Transactional
    public Set<Song> externalSearchForSongs(String songSearch, String artistSearch) {
        //Make sure there is text in query
        if ((!isValidQuery(songSearch) && !isValidQuery(artistSearch)) && (!StringUtils.isAlphanumeric(songSearch) || !StringUtils.isAlphanumeric(artistSearch))) {
            log.warn("externalSearchForSongs: Invalid query: Song:{} | Artist:{}", songSearch, artistSearch);
            return new HashSet<>();
        }
        final StringBuilder urlBuilder = new StringBuilder();

        //Only add song name to url if a song name was passed
        if (!songSearch.isEmpty()) {
            // Add the song name to the query
            urlBuilder.append("track:\"")
                    .append(songSearch.trim())
                    .append("\" ");
        }

        //Only add artist name to url if an artist name was passed
        if (!artistSearch.isEmpty()) {
            // Add the artist name to the query
            urlBuilder.append("artist:\"")
                    .append(artistSearch.trim())
                    .append("\"");
        }

        /*
         Makes the url and Encodes the given data so it can be converted ot a URI, '&strict=on' is included so
         the api returns results with exact matches in the song name or artist name. Manually replace all '+' with the encoding with
         a space just in case any were missed.

         This is the api call that will be used in the http request.
         */
        final String url = "https://api.deezer.com/search?q=" +
                URLEncoder.encode(urlBuilder.toString().trim() + "&strict=on", StandardCharsets.UTF_8).replace("+", "%20");

        /*
        Since the first url has 'strict=on', it doesn't return any matches if the given strings
        don't exactly match any songs/artists found. We call the api again here without 'strict=on' (Default is off)
        So it searches for any songs/artists with a close match to the given strings. It's not perfect,
        as some queries don't return results when we think they should, but it improves the functionality.
         */
        final String urlNoStrict = "https://api.deezer.com/search?q=" +
                URLEncoder.encode(urlBuilder.toString().trim(), StandardCharsets.UTF_8).replace("+", "%20");


        //String that contains query information, used for logging
        final String query = "Song: " + songSearch + " | Artist: " + artistSearch;

        // Build the HTTP request
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        // Build secondary HTTP request
        final HttpRequest requestNoStrict = HttpRequest.newBuilder()
                .uri(URI.create(urlNoStrict))
                .build();

        final HttpResponse<String> response;

        final HttpResponse<String> responseNoStrict;

        final SortedSet<Song> songsFound;

        //If at least a song name was given, sort by song name
        if (!songSearch.isEmpty()) {
            /*
            Creates a new tree set, which is a sorted set, and make a custom comparator that
            compares the song names and how different they are from the target name (songSearch)
            If they are the same, they are compared by deezerID, which should always be different
             */
            songsFound = new TreeSet<>((s1, s2) -> {
                int distanceComparison = Double.compare(jaroWinkler.apply(s1.getSongName(), songSearch), jaroWinkler.apply(s2.getSongName(), songSearch));
                if (distanceComparison != 0) {
                    return distanceComparison;
                }
                // If distances are the same, compare by deezer id (always unique)
                return s1.getDeezerID().compareTo(s2.getDeezerID());
            });
            log.info("externalSearchForSongs: Sorting songs for query Song: {} | Artist: {} by Song name", songSearch, artistSearch);
            //If just an artist name was given, sort by artist name
        } else if (!artistSearch.isEmpty()) {
            /*
            Creates a new tree set, which is a sorted set, and make a custom comparator that
            compares the artist names and how different they are from the target name (artistSearch)
            If they are the same, they are compared by deezerID, which should always be different
             */
            songsFound = new TreeSet<>((s1, s2) -> {
                int distanceComparison = Double.compare(jaroWinkler.apply(s1.getArtistName(), artistSearch), jaroWinkler.apply(s2.getArtistName(), artistSearch));
                if (distanceComparison != 0) {
                    return distanceComparison;
                }
                // If distances are the same, compare by deezer id (always unique)
                return s1.getDeezerID().compareTo(s2.getDeezerID());
            });
            log.info("externalSearchForSongs: Sorting songs for query Song: {} | Artist: {} by Artist name", songSearch, artistSearch);
            //If somehow no names were given (Should never happen here, but just in case) return
            //an empty set
        } else {
            log.error("externalSearchForSongs: No query was given to me and somehow that got passed the initial checks :( I don't work without a query...");
            return new HashSet<>();
        }

        try {
            // Send the HTTP request and get the response
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            responseNoStrict = HttpClient.newHttpClient().send(requestNoStrict, HttpResponse.BodyHandlers.ofString());

            // Check if the response status code is 200 (OK)
            if (response.statusCode() == 200 && responseNoStrict.statusCode() == 200) {
                final JSONObject jsonResponse = new JSONObject(response.body());
                final JSONObject jsonResponseNoStrict = new JSONObject(responseNoStrict.body());
                // Parse the JSON response
                final JSONArray dataArray = jsonResponse.getJSONArray("data");

                //Add the secondary response data
                dataArray.putAll(jsonResponseNoStrict.getJSONArray("data"));

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

                    final Song newSong = new Song(id, title, duration, artistName, artistID, albumName, albumID);
                    newSong.setSongImg(songImg);
                    newSong.setSongPreview(songPreview);
                    songsFound.add(newSong);
                }
            } else {
                log.error("externalSearchForSongs: Error response from Deezer API: Status Code {}", response.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            log.error("externalSearchForSongs: Network error occurred during search with query {}", query, e);
        } catch (JSONException e) {
            log.error("externalSearchForSongs: JSON parsing error occurred with query {}", query, e);
        }

        log.info("externalSearchForSongs: Found {} songs for query '{}'", songsFound.size(), query);

        return songsFound;
    }

    /**
     * Gets the link to the preview of the song with the given id. Deezer makes some links secure, and makes
     * them expire after a day, so we always have to reload the link to preview the song. We couldn't done a better implementation
     * with more time, but this was a last minute fix.
     *
     * @param deezerID ID to get link for
     * @return String of url
     */
    public String getSongPreview(Long deezerID) {
        if (deezerID == null || deezerID < 0) {
            return "";
        }
        final String url = "https://api.deezer.com/track/" + deezerID;
        URLEncoder.encode(url, StandardCharsets.UTF_8);

        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        final HttpResponse<String> response;

        try {
            // Send the HTTP request and get the response
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            // Check if the response status code is 200 (OK)
            if (response.statusCode() == 200) {
                final JSONObject track = new JSONObject(response.body());

                //Get the preview link
                final String preview = track.getString("preview");
                log.info("getSongPreview: Got link for song with id#{}", deezerID);
                return preview;
            } else {
                log.error("getSongPreview: Error response from Deezer API: Status Code {}", response.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            log.error("getSongPreview: Network error occurred during search with id# {}", deezerID, e);
        } catch (JSONException e) {
            log.error("getSongPreview: JSON parsing error occurred with id# {}", deezerID, e);
        }

        return "";
    }

    /**
     * Checks if the given query is valid.
     * <p>
     * A query is valid if:
     *     <ul>
     *         <li>It's not null</li>
     *         <li>It's not empty</li>
     *         <li>It's length is greater than MIN_QUERY_LENGTH</li>
     *         <li>It's length is less than MAX_QUERY_LENGTH</li>
     *     </ul>
     * </p>
     *
     * @param query Query to check if valid
     * @return {@code true} if query is valid, {@code false} otherwise.
     * @see #MAX_QUERY_LENGTH
     * @see #MIN_QUERY_LENGTH
     */
    public boolean isValidQuery(String query) {
        return query != null && !query.trim().isEmpty() && !(query.length() < MIN_QUERY_LENGTH || query.length() > MAX_QUERY_LENGTH);
    }
}
