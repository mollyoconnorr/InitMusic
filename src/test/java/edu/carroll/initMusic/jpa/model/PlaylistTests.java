package edu.carroll.initMusic.jpa.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests the basic functions related to playlist model
 */
@SpringBootTest
public class PlaylistTests {

    /** Test playlist object */
    private Playlist playlist;

    /** Test user object */
    private User user;

    /** Test song object */
    private Song song;

    @BeforeEach
    public void setUp() {
        //Initialize the User and Song objects for testing
        user = new User("username", "password", "first", "last", "email", "q1", "q1", "a1", "a2");
        song = new Song(1L, "song", 23, "artist", 2L, "album name", 3L);

        //Initialize the Playlist object with a User and a playlist name
        playlist = new Playlist(user, "My Playlist");
    }

    @Test
    public void testAddSong() {
        playlist.addSong(song);

        //Check that the song was added to the playlist
        assertTrue(playlist.getSongs().contains(song), "The song should be added to the playlist.");
        assertEquals(1, playlist.getNumberOfSongs(), "The number of songs should be 1 after adding the song.");
        assertEquals(song.getLength(), playlist.getTotalSongLength(), "The total length should be 3 minutes (180 seconds).");
    }

    @Test
    public void testRemoveSong() {
        playlist.addSong(song);

        //Remove the song from the playlist
        final boolean removed = playlist.removeSong(song);

        //Check that the song was removed
        assertTrue(removed, "The song should be removed from the playlist.");
        assertFalse(playlist.getSongs().contains(song), "The song should no longer be in the playlist.");
        assertEquals(0, playlist.getNumberOfSongs(), "The number of songs should be 0 after removal.");
        assertEquals(0, playlist.getTotalSongLength(), "The total length should be 0 after removal.");
    }

    @Test
    public void testRemoveSongById() {
        playlist.addSong(song);

        //Remove the song from the playlist by its ID
        final boolean removed = playlist.removeSong(song.getDeezerID());

        //Check that the song was removed
        assertTrue(removed, "The song should be removed by its ID.");
        assertFalse(playlist.getSongs().contains(song), "The song should no longer be in the playlist.");
        assertEquals(0, playlist.getNumberOfSongs(), "The number of songs should be 0 after removal.");
        assertEquals(0, playlist.getTotalSongLength(), "The total length should be 0 after removal.");
    }

    @Test
    public void testContainsSong() {
        playlist.addSong(song);

        //Check if the song is in the playlist
        assertTrue(playlist.containsSong(song), "The playlist should contain the song.");
    }

    @Test
    public void testEquals() {
        final Playlist anotherPlaylist = new Playlist(user, "My Playlist");

        //Check that playlists with the same user and name are equal
        assertEquals(playlist, anotherPlaylist, "Playlists with the same user and name should be equal.");
    }

    @Test
    public void testEqualsWithDifferentData() {
        final Playlist anotherPlaylist = new Playlist(user, "Another Playlist");

        //Check that playlists with different names are not equal
        assertNotEquals(playlist, anotherPlaylist, "Playlists with different names should not be equal.");
    }

    @Test
    public void testGetPlaylistID() {
        //Check that the playlist ID is initially null (before persistence)
        assertNull(playlist.getPlaylistID(), "The playlist ID should be null before it is saved to the database.");
    }

    @Test
    public void testGetDateCreated() {
        //Ensure that the dateCreated field is null (not set by AuditingEntityListener yet)
        assertNull(playlist.getDateCreated(), "The dateCreated field should not be null.");
    }

    @Test
    public void testToString() {
        String expectedString = "Playlist{" +
                "playlistID=null, " +
                "author=" + user +
                ", playlistName='My Playlist', " +
                "dateCreated='" + playlist.getDateCreated() + "', " +
                "numberOfSongs=0, " +
                "totalSongLength=0" +
                "}";

        //Check that the toString method returns the expected string representation
        assertEquals(expectedString, playlist.toString(), "The toString() method should return the correct string representation.");
    }
}
