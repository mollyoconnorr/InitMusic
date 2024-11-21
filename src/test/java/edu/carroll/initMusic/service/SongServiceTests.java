package edu.carroll.initMusic.service;

import edu.carroll.initMusic.jpa.model.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests methods in the SongServiceImpl class
 *
 * <p>
 *     {@link SongService#getSongPreview(Long)} is not tested because all it does is calls the
 *     {@link SongSearchService#getSongPreview(Long)} method, which uses an external api to get
 *     the song link and we can not control the output it gives us.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Transactional
class SongServiceTests {
    /** Service we are testing */
    @Autowired
    private SongService songService;

    /** Mock the searchService since we can't control what it returns from the api*/
    @MockBean
    private SongSearchService searchService;

    /** Set of songs to return when needed */
    private static final Set<Song> songs = new HashSet<>();

    @BeforeEach
    public void setUp() {
        final Random r = new Random();
        songs.clear();
        for(int i = 1; i <= 5; i++){
            final Song song = new Song();
            song.setSongName("song".repeat(i));
            song.setArtistName("artist".repeat(i));
            song.setAlbumName("album".repeat(i));
            song.setArtistDeezerID((long) i*i);
            song.setAlbumDeezerID((long) i*i);
            song.setDeezerID(r.nextLong(1,Long.MAX_VALUE));
            song.setLength(0);
            song.setSongPreview("");
            song.setSongImg("");

            songs.add(song);
        }
    }

    // Testing searchForSongs first

    @Test
    public void testSearchForSongsEmptyQueries(){
        final String songName = "";
        final String artistName = "";
        when(searchService.externalSearchForSongs(songName,artistName)).thenReturn(new HashSet<>());

        final Set<Song> result = songService.searchForSongs(songName,artistName);
        assertTrue(result.isEmpty(),"Search for songs should return a empty set when both queries are empty!");

        //SearchService should never get called, bc 2 empty strings will cause the searchForSongs method to return early
        verify(searchService, never()).externalSearchForSongs(songName,artistName);
    }

    @Test
    public void testSearchForSongsNullQueries(){
        final String songName = null;
        final String artistName = null;
        when(searchService.externalSearchForSongs(songName,artistName)).thenReturn(new HashSet<>());

        final Set<Song> result = songService.searchForSongs(songName,artistName);
        assertTrue(result.isEmpty(),"Search for songs should return a empty set when both queries are null!");

        //SearchService should never get called, bc 2 null strings will cause the searchForSongs method to return early
        verify(searchService, never()).externalSearchForSongs(songName,artistName);
    }

    @Test
    public void testSearchForSongsBothQueriesTooShort(){
        final String songName = "12";
        final String artistName = "12";
        when(searchService.externalSearchForSongs(songName,artistName)).thenReturn(new HashSet<>());

        final Set<Song> result = songService.searchForSongs(songName,artistName);
        assertTrue(result.isEmpty(),"Search for songs should return a empty set when both queries are too short!");

        //SearchService should never get called, bc 2 strings that are too short will cause the searchForSongs method to return early
        verify(searchService, never()).externalSearchForSongs(songName,artistName);
    }

    @Test
    public void testSearchForSongsBothQueriesTooLong(){
        final String songName = "a".repeat(51);
        final String artistName = "a".repeat(51);
        when(searchService.externalSearchForSongs(songName,artistName)).thenReturn(new HashSet<>());

        final Set<Song> result = songService.searchForSongs(songName,artistName);
        assertTrue(result.isEmpty(),"Search for songs should return a empty set when both queries are too long!");

        //SearchService should never get called, bc 2 strings that are too long will cause the searchForSongs method to return early
        verify(searchService, never()).externalSearchForSongs(songName,artistName);
    }

    @Test
    public void testSearchForSongsBothValidReturnsSetCreatesCache() {
        final String songName = "songName";
        final String artistName = "artist";
        when(searchService.externalSearchForSongs(songName, artistName)).thenReturn(songs);

        final Set<Song> result = songService.searchForSongs(songName, artistName);
        assertFalse(result.isEmpty(), "Search for songs should return a set of songs when both queries are valid!");
        //Since there is no cache, external search should be called once
        verify(searchService, times(1)).externalSearchForSongs(songName, artistName);

        assertTrue(result.containsAll(songs),"Songs found should match those mocked in externalSearchForSongs!");
    }

    @Test
    public void testSearchForSongsBothValidReturnsSetCacheFound(){
        final String songName = "songNameCool";
        final String artistName = "artistName";
        when(searchService.externalSearchForSongs(songName, artistName)).thenReturn(songs);

        Set<Song> result = songService.searchForSongs(songName, artistName);
        assertEquals(result,songs,"Songs should match those mocked in externalSearchForSongs!");

        //Now if we call searchForSongs again, externalSearch should never be called bc there is now a cache
        result = songService.searchForSongs(songName, artistName);
        assertEquals(result,songs,"Songs should match those saved in cache!");

        //We called searchForSongs twice, but externalSearchForSongs should only be called once
        verify(searchService, times(1)).externalSearchForSongs(songName, artistName);
    }

    @Test
    public void testSearchForSongsOnlySongValidReturnsSetCreatesCache() {
        final String songName = "newSongName";
        final String artistName = "";
        when(searchService.externalSearchForSongs(songName, artistName)).thenReturn(songs);

        final Set<Song> result = songService.searchForSongs(songName, artistName);
        assertFalse(result.isEmpty(), "Search for songs should return a set of songs when only song name is valid!");
        verify(searchService, times(1)).externalSearchForSongs(songName, artistName);
        assertTrue(result.containsAll(songs),"Songs found should match those mocked in externalSearchForSongs!");
    }

    @Test
    public void testSearchForSongsOnlyArtistValidReturnsSetCreatesCache() {
        final String songName = "";
        final String artistName = "newArtistName";
        when(searchService.externalSearchForSongs(songName, artistName)).thenReturn(songs);

        final Set<Song> result = songService.searchForSongs(songName, artistName);
        assertFalse(result.isEmpty(), "Search for songs should return a set of songs when only artist name is valid!");
        verify(searchService, times(1)).externalSearchForSongs(songName, artistName);
        assertTrue(result.containsAll(songs),"Songs found should match those mocked in externalSearchForSongs!");
    }

    @Test
    public void testSearchForSongsSearchTwiceUniqueCaches(){
        final String songName = "favSong";
        final String artistName = "favArtist";
        when(searchService.externalSearchForSongs(songName, artistName)).thenReturn(songs);

        final Set<Song> result = songService.searchForSongs(songName, artistName);
        assertFalse(result.isEmpty(), "Search for songs should return a set of songs when both queries are valid!");
        //Since there is no cache, external search should be called once
        verify(searchService, times(1)).externalSearchForSongs(songName, artistName);

        assertTrue(result.containsAll(songs),"Songs found should match those mocked in externalSearchForSongs!");

        //Generate new values for songs set
        setUp();
        final String songNameTwo = "2ndfavSong";
        final String artistNameTwo = "2ndfavArtist";
        when(searchService.externalSearchForSongs(songNameTwo, artistNameTwo)).thenReturn(songs);

        final Set<Song> resultTwo = songService.searchForSongs(songNameTwo, artistNameTwo);
        assertFalse(resultTwo.isEmpty(), "Search for songs should return a set of songs when both queries are valid!");
        //Since there is no cache, external search should be called once
        verify(searchService, times(1)).externalSearchForSongs(songNameTwo, artistNameTwo);

        assertTrue(resultTwo.containsAll(songs),"Songs found should match those mocked in externalSearchForSongs!");
    }


    //Testing isValidQuery

    @Test
    public void testIsValidQueryNullQuery() {
        assertFalse(songService.isValidQuery(null), "Null query should be invalid");
    }

    @Test
    public void testIsValidQueryEmptyQuery() {
        assertFalse(songService.isValidQuery(""), "Empty query should be invalid");
    }

    @Test
    public void testIsValidQueryShortQuery() {
        assertFalse(songService.isValidQuery("ab"), "Query with less than 3 characters should be invalid");
    }

    @Test
    public void testIsValidQueryLongQuery() {
        final String longQuery = "a".repeat(41);
        assertFalse(songService.isValidQuery(longQuery), "Query with more than 50 characters should be invalid");
    }

    @Test
    public void testIsValidQueryValidQuery() {
        assertTrue(songService.isValidQuery("abc"), "Query with 3 characters should be valid");
        assertTrue(songService.isValidQuery("a valid query"), "Typical valid query should be valid");
        final String maxLengthQuery = "a".repeat(40);
        assertTrue(songService.isValidQuery(maxLengthQuery), "Query with exactly 49 characters should be valid");
    }
}
