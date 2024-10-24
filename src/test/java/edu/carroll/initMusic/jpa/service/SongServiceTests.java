package edu.carroll.initMusic.jpa.service;

import edu.carroll.initMusic.jpa.model.Song;
import edu.carroll.initMusic.service.SongServiceDeezerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class SongServiceTests {

    @InjectMocks
    private SongServiceDeezerImpl songService;

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private HttpResponse<String> mockResponse;

    private static final String VALID_QUERY = "Test Song";
    private static final String INVALID_QUERY = "AB"; // less than 3 characters
    private static final String EMPTY_QUERY = "";

    @BeforeEach
    public void setUp() {
        //Reset the mocks before each test
        reset(mockHttpClient, mockResponse);
        songService.setHttpClient(mockHttpClient);
    }

    @Test
    public void testSearchForSongs_ValidQuery() throws Exception {
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

        assertNotNull(songs);
        assertEquals(1, songs.size());

        verify(mockHttpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    public void testSearchForSongs_EmptyQuery() {
        //won't return any songs because query is empty
        final Set<Song> songs = songService.searchForSongs(EMPTY_QUERY);

        assertNotNull(songs);
        assertTrue(songs.isEmpty());
    }

    @Test
    public void testSearchForSongs_ShortQuery() {
        //won't return any songs because query is too short
        final Set<Song> songs = songService.searchForSongs(INVALID_QUERY);

        assertNotNull(songs);
        assertTrue(songs.isEmpty());
    }

    @Test
    public void testSearchForSongs_ErrorResponse() throws Exception {
        //mock a error response
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);
        when(mockResponse.statusCode()).thenReturn(404); // Simulate not found

        //error response should cause the method to return 0 songs
        final Set<Song> songs = songService.searchForSongs(VALID_QUERY);

        assertNotNull(songs);
        assertTrue(songs.isEmpty());
        verify(mockHttpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    public void testSearchForSongs_NetworkError() throws Exception {
        //mock a network error
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException("Network error"));

        //network error should cause the method to return 0 songs
        final Set<Song> songs = songService.searchForSongs(VALID_QUERY);

        assertNotNull(songs);
        assertTrue(songs.isEmpty());
        verify(mockHttpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    public void testSearchForSongs_SuccessfulJsonParsing() throws Exception {
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
        assertNotNull(songs);
        assertEquals(1, songs.size());
        verify(mockHttpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    public void testSearchForSongs_JsonParsingError() throws Exception {
        //mock a valid HTTP response but simulate a JSON parsing error
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);
        when(mockResponse.statusCode()).thenReturn(200);
        //simulate that the response body causes a JSON parsing error inside the service method
        when(mockResponse.body()).thenReturn("Invalid JSON String");

        //since there was a json error, the method shouldn't return any songs
        final Set<Song> songs = songService.searchForSongs(VALID_QUERY);
        assertEquals(0, songs.size());

        //verify that the send method was called once
        verify(mockHttpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }
}
