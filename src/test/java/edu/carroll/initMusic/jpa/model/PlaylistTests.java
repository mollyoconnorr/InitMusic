package edu.carroll.initMusic.jpa.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

/**
 * <p>
 * This class is used to test the Playlist class
 * </p>
 *
 * @author Nick Clouse
 * @since September 19, 2024
 */
@SpringBootTest
public class PlaylistTests {
    /**
     * Name of playlist
     */
    private static final String playlistName = "AwesomePlaylist";

    /**
     * Author of playlist
     */
    private static final User author = new User();

    /**
     * Fake playlist for testing
     */
    private final Playlist fakePlaylist = new Playlist(author, playlistName);

    /**
     * Testing user creation and getters
     */
    @Test
    public void verifyCreationOfPlaylistAndGetters() {
        final String setName = fakePlaylist.getPlaylistName();
        final User setAuthor = fakePlaylist.getAuthor();
        final Set<Song> songs = fakePlaylist.getSongs();
        final int setNumSongs = fakePlaylist.getNumberOfSongs();
        final int setSongsLength = fakePlaylist.getTotalSongLength();

        assertTrue("Playlist name should match name from getter", playlistName.equals(setName));
        assertTrue("Author should match author from getter", setAuthor.equals(fakePlaylist.getAuthor()));
        assertTrue("There should be no songs in playlist", songs.size() == setNumSongs && setNumSongs == 0);
        assertTrue("There should be a total song length of 0", setSongsLength == 0);
    }

    /**
     * Testing playlist manipulation, like adding and removing songs.
     */
    @Test
    public void verifyPlaylistEditing() {
        //Populating playlist with songs
        final Set<Song> songs = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            //String songName, String genre, String releaseDate, int length, int numberOfStreams
            final Song tempSong = new Song(1L, "Random" + i, 60, "Name", 1, "Album", 1);
            fakePlaylist.addSong(tempSong);
            songs.add(tempSong);
        }

        //Making sure there are songs in playlist and the totalSongLength is correct
        assertTrue("Three songs should be in playlist", fakePlaylist.getSongs().size() == 3);
        assertTrue("Playlist total length should be 3*60=180", fakePlaylist.getTotalSongLength() == 3 * 60);
        //Now making sure there are actually three songs
        for (Song s : songs) {
            assertTrue("Song should be in playlist", fakePlaylist.getSongs().contains(s));
        }
        //Now lets remove a song and see if that works
        final Object songToRemove = songs.toArray()[0];
        fakePlaylist.removeSong((Song) songToRemove);
        songs.remove((Song) songToRemove);
        for (Song s : songs) {
            assertTrue("Should only be two songs in playlist", fakePlaylist.getSongs().contains(s));
        }
        assertTrue("Two songs should be in playlist", fakePlaylist.getSongs().size() == 2);
        assertTrue("Playlist total length should be 2*60=120", fakePlaylist.getTotalSongLength() == 2 * 60);
    }

    /**
     * Testing equals, hashCode, and two setters.
     */
    @Test
    public void verifyMiscFunctions() {
        //Testing equals
        final Playlist fakePlaylistTwo = new Playlist(author, playlistName);
        assertTrue("Fake playlists should be equal", fakePlaylist.equals(fakePlaylistTwo));
        fakePlaylistTwo.setPlaylistName("Random");

        final LocalDateTime now = LocalDateTime.now();
        fakePlaylistTwo.setDateCreated(now);

        //Test equals function and setDateCreated work
        assertFalse("Fake playlists should not be equal", fakePlaylist.equals(fakePlaylistTwo));

        //Testing hashCode
        final int playlistHash = fakePlaylist.hashCode();
        final int playlistTwoHash = fakePlaylistTwo.hashCode();
        assertFalse("Fake playlist hash should not be equal", playlistTwoHash == playlistHash);
    }
}
