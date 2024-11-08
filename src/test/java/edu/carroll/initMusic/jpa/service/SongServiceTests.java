package edu.carroll.initMusic.jpa.service;

import edu.carroll.initMusic.jpa.model.Song;
import edu.carroll.initMusic.service.SongServiceDeezerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests methods in the SongServiceDeezerImpl class
 *
 * @author Nick Clouse
 *
 * @since October 4, 2024
 */
@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class SongServiceTests {
    /** Valid query string (More than 3 characters) */
    private static final String VALID_QUERY = "Test Song";

    /** invalid query string (Less than 3 characters) */
    private static final String INVALID_QUERY = "AB"; // less than 3 characters

    /** invalid query string (Empty String) */
    private static final String EMPTY_QUERY = "";

    /** Service we are testing */
    @InjectMocks
    private SongServiceDeezerImpl songService;

    @Autowired
    private SongServiceDeezerImpl songServiceDeezer;

    /** Mock Http Client */
    @Mock
    private HttpClient mockHttpClient;

    /** Mock Http Responses */
    @Mock
    private HttpResponse<String> mockResponse;

    @BeforeEach
    public void setUp() {
        //Reset the mocks before each test
        reset(mockHttpClient, mockResponse);
        songService.setHttpClient(mockHttpClient);
    }

    @Test
    public void testSearchForSongsValidQuery() throws Exception {
        final Song expectedSong = new Song(123456789L, "Sample Song Title", 300, "Sample Artist", 98765L, "Sample Album Title", 54321L);
        expectedSong.setSongImg("https://api.deezer.com/album/54321/image");
        expectedSong.setSongPreview("https://cdn-preview-5.dzcdn.net/stream/c-samplepreview.mp3");

        //Prepare the mocked response with the JSON
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn("{\"data\":[{\"id\":123456789,\"readable\":true,\"title\":\"Sample Song Title\"," +
                "\"title_short\":\"Sample Song\",\"title_version\":\"(Demo Version)\",\"link\":\"https://www.deezer.com/track/123456789\"," +
                "\"duration\":300,\"rank\":100000,\"explicit_lyrics\":false,\"explicit_content_lyrics\":0,\"explicit_content_cover\":0," +
                "\"preview\":\"https://cdn-preview-5.dzcdn.net/stream/c-samplepreview.mp3\",\"md5_image\":\"sampleimagehash123456\"," +
                "\"artist\":{\"id\":98765,\"name\":\"Sample Artist\",\"link\":\"https://www.deezer.com/artist/98765\"," +
                "\"picture\":\"https://api.deezer.com/artist/98765/image\",\"picture_small\":\"https://e-cdns-images.dzcdn.net/images/artist/sampleartist_small.jpg\"," +
                "\"picture_medium\":\"https://e-cdns-images.dzcdn.net/images/artist/sampleartist_medium.jpg\"," +
                "\"picture_big\":\"https://e-cdns-images.dzcdn.net/images/artist/sampleartist_big.jpg\"," +
                "\"picture_xl\":\"https://e-cdns-images.dzcdn.net/images/artist/sampleartist_xl.jpg\",\"tracklist\":\"https://api.deezer.com/artist/98765/top?limit=50\"," +
                "\"type\":\"artist\"}," +
                "\"album\":{\"id\":54321,\"title\":\"Sample Album Title\",\"cover\":\"https://api.deezer.com/album/54321/image\"," +
                "\"cover_small\":\"https://e-cdns-images.dzcdn.net/images/cover/samplealbum_small.jpg\"," +
                "\"cover_medium\":\"https://e-cdns-images.dzcdn.net/images/cover/samplealbum_medium.jpg\"," +
                "\"cover_big\":\"https://e-cdns-images.dzcdn.net/images/cover/samplealbum_big.jpg\"," +
                "\"cover_xl\":\"https://e-cdns-images.dzcdn.net/images/cover/samplealbum_xl.jpg\"," +
                "\"md5_image\":\"samplealbumhash123456\",\"tracklist\":\"https://api.deezer.com/album/54321/tracks\",\"type\":\"album\"}," +
                "\"type\":\"track\"}]}");

        /*
         * Should parse the data and return it as a list of songs, in this case
         * there should only be one song.
         */
        final Set<Song> songs = songService.searchForSongs(VALID_QUERY);

        assertNotNull(songs, "SearchForSongs should return at least one song with a valid query!");
        assertEquals(1, songs.size(), "Sample JSON should cause searchForSongs to create 1 song, but it created" + songs.size());

        verify(mockHttpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    public void testSearchForSongsEmptyQuery() {
        //won't return any songs because query is empty
        final Set<Song> songs = songService.searchForSongs(EMPTY_QUERY);

        assertNotNull(songs, "SearchForSongs should return a not not set with a empty query!");
        assertTrue(songs.isEmpty(), "SearchForSongs should return a empty set with a empty query!");
    }

    @Test
    public void testSearchForSongsNullQuery() {
        //won't return any songs because query is empty
        final Set<Song> songs = songService.searchForSongs(null);

        assertNotNull(songs, "SearchForSongs should return a not not set with a null query!");
        assertTrue(songs.isEmpty(), "SearchForSongs should return a empty set with a null query!");
    }

    @Test
    public void testSearchForSongsShortQuery() {
        //won't return any songs because query is too short
        final Set<Song> songs = songService.searchForSongs(INVALID_QUERY);

        assertNotNull(songs,"SearchForSongs should return a not not set with a short query!");
        assertTrue(songs.isEmpty(),"SearchForSongs should return a empty set with a short query!");
    }

    @Test
    public void testSearchForSongsErrorResponse() throws Exception {
        //mock a error response
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);
        when(mockResponse.statusCode()).thenReturn(404); // Simulate not found

        //error response should cause the method to return 0 songs
        final Set<Song> songs = songService.searchForSongs(VALID_QUERY);

        assertNotNull(songs,"SearchForSongs should return a not not set when a Error Response Occurs!");
        assertTrue(songs.isEmpty(),"SearchForSongs should return a empty set when a Error Response Occurs!");
        verify(mockHttpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    public void testSearchForSongsNetworkError() throws Exception {
        //mock a network error
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException("Network error"));

        //network error should cause the method to return 0 songs
        final Set<Song> songs = songService.searchForSongs(VALID_QUERY);

        assertNotNull(songs,"SearchForSongs should return a not not set when a Network Error Occurs!");
        assertTrue(songs.isEmpty(),"SearchForSongs should return a empty set when a Network Error Occurs!");
        verify(mockHttpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    //Test a basic but valid json structure
    @Test
    public void testSearchForSongsSuccessfulJsonParsing() throws Exception {
        //mock a valid JSON response
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn("{\"data\":[{\"id\":123,\"title\":\"Test Song\",\"duration\":210," +
                "\"artist\":{\"name\":\"Test Artist\",\"id\":456}," +
                "\"album\":{\"title\":\"Test Album\",\"id\":789,\"cover\":\"cover_url\"}," +
                "\"preview\":\"preview_url\"}]}"); // Valid JSON

        //call searchForSongs
        final Set<Song> songs = songService.searchForSongs(VALID_QUERY);

        //check that the parsing is correct and the result is not null
        assertNotNull(songs, "SearchForSongs should return at least one song with a valid query, even with a basic JSON structure!");
        assertEquals(1, songs.size(), "Simple JSON Structure should cause searchForSongs to create 1 song, but it created" + songs.size());
        verify(mockHttpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    public void testSearchForSongsJsonParsingError() throws Exception {
        //mock a valid HTTP response but simulate a JSON parsing error
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);
        when(mockResponse.statusCode()).thenReturn(200);
        //simulate that the response body causes a JSON parsing error inside the service method
        when(mockResponse.body()).thenReturn("Invalid JSON String");

        //since there was a json error, the method shouldn't return any songs
        final Set<Song> songs = songService.searchForSongs(VALID_QUERY);

        assertEquals(0, songs.size(), "SearchForSongs should return a empty set when a JSON Parsing Error occurs!");

        //verify that the send method was called once
        verify(mockHttpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    /*
     * Testing getLocalCache
     */
    @Test
    public void checkGetLocalCacheReturnsNullWhenNoCacheInDBWithQuery(){
        final Set<Song> songsFound = songServiceDeezer.getLocalCache("query");
        assertNull(songsFound, "songsFound should be null when no cache is found after trying to get cache!");
    }

    @Test
    public void checkGetLocalCacheReturnsNullWhenNullQueryPassed(){
        final Set<Song> songsFound = songServiceDeezer.getLocalCache(null);
        assertNull(songsFound, "songsFound should be null when a null query passed!");
    }

    @Test
    public void checkGetLocalCacheReturnsNullWhenEmptyQueryPassed(){
        final Set<Song> songsFound = songServiceDeezer.getLocalCache("");
        assertNull(songsFound, "songsFound should be null when a empty query passed!");
    }

    @Test
    @Transactional
    public void checkGetLocalCacheReturnsCacheWithValidQueryPassed(){
        final String query = "query";
        final Song song = new Song(2323L,"Name",232,"Nick",2323L,"Album",45454L);
        final Set<Song> songs = new HashSet<>();
        songs.add(song);
        songServiceDeezer.createCache(query,songs);

        final Set<Song> songsFound = songServiceDeezer.getLocalCache(query);
        final Song firstSong = (Song)songsFound.toArray()[0];
        assertNotNull(songsFound, "songsFound should not be null when a valid query is passed and there is a local cache in DB!");
        assertEquals(songsFound.size(), 1, "songsFound should have a size of 1 when a valid query is passed and there is a local cache with one song in DB!");
        assertEquals(firstSong.getSongID(),song.getSongID(),"Cache song set should contain given song after getting Cache!");
    }

    @Test
    @Transactional
    public void checkGetLocalCacheReturnsNoCacheWithValidQueryPassedButNoCacheWithPassedQueryInDB(){
        final String query = "query2";
        final Song song = new Song(2323L,"Name",232,"Nick",2323L,"Album",45454L);
        final Set<Song> songs = new HashSet<>();
        songs.add(song);
        songServiceDeezer.createCache(query,songs);

        final Set<Song> songsFound = songServiceDeezer.getLocalCache(query + 1);
        assertNull(songsFound, "songsFound should be null when a valid query is passed and but no cache with that query in DB!");
    }

    /*
     * Testing CreateCache
     */

    @Test
    public void checkCreateCacheNullQuery(){
        final Set<Song> songs = new HashSet<>();

        assertFalse(songServiceDeezer.createCache(null,songs),"No Cache should be created when query is null!");
    }

    @Test
    public void checkCreateCacheEmptyQuery(){
        final Set<Song> songs = new HashSet<>();

        assertFalse(songServiceDeezer.createCache("",songs),"No Cache should be created when query is empty!");
    }

    @Test
    public void checkCreateCacheNullResults(){
        assertFalse(songServiceDeezer.createCache("Query",null),"No Cache should be created when query is empty!");
    }

    @Test
    @Transactional
    public void checkCreateCacheSucceedsWhenQueryAndResultsValid(){
        final String query = "query3";
        final Song song = new Song(2323L,"Name",232,"Nick",2323L,"Album",45454L);
        final Set<Song> songs = new HashSet<>();
        songs.add(song);
        songServiceDeezer.createCache(query,songs);

        final Set<Song> songsFound = songServiceDeezer.getLocalCache(query);
        assertNotNull(songsFound, "songsFound should not be null when a valid query and results set is passed!");
        assertEquals(songsFound,songs,"Songs in cache should match those passed in createCache!");
    }

    @Test
    @Transactional
    public void checkCreateCacheSucceedsWhenOldCacheIsOverwritten(){
        final String query = "query3";
        final Song song = new Song(2323L,"Name",232,"Nick",2323L,"Album",45454L);
        final Song song2 = new Song(2324L,"Name",232,"Nick",2323L,"Album",45454L);
        final Set<Song> songs = new HashSet<>();
        songs.add(song);

        //Assume this worked this one time
        songServiceDeezer.createCache(query,songs);
        final Set<Song> songsFoundOldCache = songServiceDeezer.getLocalCache(query);

        //Add a song to set songs so its different from first caches song set
        final Set<Song> songs2 = new HashSet<>();
        songs2.add(song2);
        songs2.add(song);

        songServiceDeezer.createCache(query,songs2);

        final Set<Song> songsFound = songServiceDeezer.getLocalCache(query);
        assertNotNull(songsFound, "songsFound should not be null when a valid query and results set is passed!");
        assertEquals(songsFoundOldCache.size(),songsFound.size(),"Old cache with same query should have same size set as updated cache!");
    }


}
