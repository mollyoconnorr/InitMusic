package edu.carroll.initMusic.jpa.service;

import edu.carroll.initMusic.ResponseStatus;
import edu.carroll.initMusic.jpa.model.Playlist;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.jpa.model.Song;
import edu.carroll.initMusic.jpa.repo.PlaylistRepository;
import edu.carroll.initMusic.jpa.repo.UserRepository;
import edu.carroll.initMusic.jpa.repo.SongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import edu.carroll.initMusic.service.PlaylistService;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.transaction.annotation.Transactional;



/**
 * Unit tests for the PlaylistService class.
 * This class tests the functionality of playlist creation, renaming, and deletion.
 */
@Transactional
@SpringBootTest
public class PlaylistServiceTests {

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private SongRepository songRepository;

    private User testUser;

    /**
     * Sets up a test user and an initial playlist before each test.
     * This method is executed before each test case to ensure a clean state.
     */
    @BeforeEach
    void setUp() {
        // Create a test user and save it in the repository
        userRepository.deleteAll();
        testUser = new User("username", "hashedPassword", "firstName", "lastName", "email", "question1", "question2", "answer1", "answer2");
        Playlist playlist = new Playlist(testUser, "My Playlist"); // Assuming playlist has a constructor that takes a user
        testUser.addPlaylist(playlist);
        userRepository.save(testUser); // Save user to the test database
    }

    /**
     * Tests the successful creation of a playlist.
     */
    @Test
    void testCreatePlaylistSuccess() {
        ResponseStatus status = playlistService.createPlaylist("Test Playlist", testUser);
        assertEquals(ResponseStatus.SUCCESS, status, "Playlist should be created successfully");
    }

    /**
     * Tests that the created playlist is retrievable from the repository.
     */
    @Test
    void testRetrieveCreatedPlaylist() {
        playlistService.createPlaylist("Test Playlist", testUser);
        List<Playlist> playlists = playlistRepository.findAll();
        assertEquals(1, playlists.size(), "There should be one playlist in the repository");
        assertEquals("Test Playlist", playlists.get(0).getPlaylistName(), "The playlist name should match the created name");
    }

    /**
     * Tests that the count of playlists is correct after creation.
     */
    @Test
    void testPlaylistCountAfterCreation() {
        playlistService.createPlaylist("Test Playlist", testUser);
        long count = playlistRepository.count();
        assertEquals(1, count, "There should be one playlist in the repository after creation");
    }

    /**
     * Tests that the count of playlists is correct after creation of two playlists.
     */
    @Test
    void testDoublePlaylistCountAfterCreation() {
        playlistService.createPlaylist("Test Playlist", testUser);
        playlistService.createPlaylist("Second Test Playlist", testUser);
        long count = playlistRepository.count();
        assertEquals(2, count, "There should be two playlists in the repository after creation");
    }

    /**
     * Tests that creating a duplicate playlist name doesn't work.
     */
    @Test
    void testCreateDuplicatePlaylistName() {
        // Create the initial playlist
        ResponseStatus status = playlistService.createPlaylist("Test Playlist", testUser);
        assertEquals(ResponseStatus.SUCCESS, status); // Ensure the first creation is successful

        // Attempt to create a duplicate playlist
        ResponseStatus duplicateStatus = playlistService.createPlaylist("Test Playlist", testUser);

        // Assert that the response status is PLAYLIST_NAME_EXISTS
        assertEquals(ResponseStatus.PLAYLIST_NAME_EXISTS, duplicateStatus,
                "Creating a duplicate playlist should return PLAYLIST_NAME_EXISTS");
    }

    /**
     * Tests the successful renaming of a playlist.
     * Asserts that the playlist is renamed successfully and the change is reflected in the repository.
     */
    @Test
    void testRenamePlaylistSuccess() {
        playlistService.createPlaylist("Old Playlist", testUser);

        Playlist playlist = testUser.getPlaylists().iterator().next();
        ResponseStatus status = playlistService.renamePlaylist("New Playlist", playlist.getPlaylistID(), testUser);
        assertEquals(ResponseStatus.SUCCESS, status);

        Playlist renamedPlaylist = playlistRepository.findById(playlist.getPlaylistID()).orElse(null);
        assertNotNull(renamedPlaylist);
        assertEquals("New Playlist", renamedPlaylist.getPlaylistName());
    }

    /**
     * Tests the renaming of a playlist to a name that already exists.
     * Asserts that the appropriate response status is returned when attempting to rename to an existing name.
     */
    @Test
    void testRenamePlaylistNameExists() {
        playlistService.createPlaylist("Playlist One", testUser);
        playlistService.createPlaylist("Playlist Two", testUser);

        Playlist playlistOne = testUser.getPlaylist("Playlist One");
        ResponseStatus status = playlistService.renamePlaylist("Playlist Two", playlistOne.getPlaylistID(), testUser);
        assertEquals(ResponseStatus.PLAYLIST_NAME_EXISTS, status);
    }

    /**
     * Tests the successful deletion of a playlist.
     * Asserts that the playlist is deleted successfully and is no longer found in the repository.
     */
    @Test
    void testDeletePlaylistSuccess() {
        // Create and save the playlist in the database
        ResponseStatus status = playlistService.createPlaylist("Delete Me", testUser);
        assertEquals(ResponseStatus.SUCCESS, status);

        // Retrieve the playlist from the user's list or directly from the repository
        Playlist playlist = testUser.getPlaylists().stream()
                .filter(p -> "Delete Me".equals(p.getPlaylistName()))
                .findFirst()
                .orElse(null);
        assertNotNull(playlist, "Playlist should not be null after creation");

        // Perform the delete operation using the actual persisted playlist ID
        status = playlistService.deletePlaylist("Delete Me", playlist.getPlaylistID(), testUser);
        assertEquals(ResponseStatus.SUCCESS, status);

        // Assert that the playlist is no longer found in the repository
        assertTrue(playlistRepository.findById(playlist.getPlaylistID()).isEmpty());
    }

    /**
     * Tests the deletion of a playlist that does not exist.
     * Asserts that the appropriate response status is returned when attempting to delete a non-existent playlist.
     */
    @Test
    void testDeletePlaylistNotFound() {
        ResponseStatus status = playlistService.deletePlaylist("Nonexistent Playlist", 1L, testUser);
        assertEquals(ResponseStatus.PLAYLIST_NOT_FOUND, status);
    }

    /**
     * Tests that attempting to create a playlist with an empty name returns the appropriate error status.
     */
    @Test
    void testCreatePlaylistEmptyName() {
        ResponseStatus status = playlistService.createPlaylist("", testUser);
        assertEquals(ResponseStatus.PLAYLIST_NAME_INVALID, status, "Empty playlist name should return PLAYLIST_NAME_INVALID");
    }

    /**
     * Tests that attempting to create a playlist with a null name returns the appropriate error status.
     */
    @Test
    void testCreatePlaylistNullName() {
        ResponseStatus status = playlistService.createPlaylist(null, testUser);
        assertEquals(ResponseStatus.PLAYLIST_NAME_INVALID, status, "Null playlist name should return PLAYLIST_NAME_INVALID");
    }

    /**
     * Tests that renaming a playlist to an empty name returns the appropriate error status.
     * Assumes an existing playlist is present for renaming.
     */
    @Test
    void testRenamePlaylistEmptyName() {
        playlistService.createPlaylist("Existing Playlist", testUser);
        Playlist playlist = testUser.getPlaylists().iterator().next();
        ResponseStatus status = playlistService.renamePlaylist("", playlist.getPlaylistID(), testUser);
        assertEquals(ResponseStatus.PLAYLIST_NAME_INVALID, status, "Renaming to an empty name should return PLAYLIST_NAME_INVALID");
    }

    /**
     * Tests that renaming a playlist to a null name returns the appropriate error status.
     * Assumes an existing playlist is present for renaming.
     */
    @Test
    void testRenamePlaylistNullName() {
        playlistService.createPlaylist("Existing Playlist", testUser);
        Playlist playlist = testUser.getPlaylists().iterator().next();
        ResponseStatus status = playlistService.renamePlaylist(null, playlist.getPlaylistID(), testUser);
        assertEquals(ResponseStatus.PLAYLIST_NAME_INVALID, status, "Renaming to a null name should return PLAYLIST_NAME_INVALID");
    }

    /**
     * Tests that attempting to delete a playlist with an invalid ID returns the appropriate error status.
     */
    @Test
    void testDeletePlaylistInvalidID() {
        ResponseStatus status = playlistService.deletePlaylist("Invalid Playlist", 999L, testUser);
        assertEquals(ResponseStatus.PLAYLIST_NOT_FOUND, status, "Deleting with an invalid playlist ID should return PLAYLIST_NOT_FOUND");
    }

    /**
     * Tests that initially there are no playlists for the user in the repository.
     */
    @Test
    void testRetrievePlaylistsNoPlaylists() {
        List<Playlist> playlists = playlistRepository.findAll();
        assertTrue(playlists.isEmpty(), "There should be no playlists for the user initially");
    }

    /**
     * Tests that multiple playlists can be created and verifies that they are retrievable from the repository.
     */
    @Test
    void testCreateMultiplePlaylistsAndCheckRetrieval() {
        playlistService.createPlaylist("Playlist One", testUser);
        playlistService.createPlaylist("Playlist Two", testUser);

        List<Playlist> playlists = playlistRepository.findAll();
        assertEquals(2, playlists.size(), "There should be two playlists in the repository");

        assertTrue(playlists.stream().anyMatch(p -> "Playlist One".equals(p.getPlaylistName())), "Playlist One should exist");
        assertTrue(playlists.stream().anyMatch(p -> "Playlist Two".equals(p.getPlaylistName())), "Playlist Two should exist");
    }

    /**
     * Tests the successful removal of a song from a playlist.
     * Assumes the song and playlist already exist in the repository.
     */
    @Test
    void testRemoveSongFromPlaylistSuccess() {
        // Create and save the song to the repository
        Song song = new Song(1L, "Song Title", 3, "Album", 2021L, "Genre", 0L);
        songRepository.save(song);

        // Create the playlist and add the song
        playlistService.createPlaylist("Test Playlist", testUser);
        Playlist playlist = testUser.getPlaylists().iterator().next();

        // Add the song to the playlist
        playlistService.addSongToPlaylist(playlist.getPlaylistID(), song);
        playlistRepository.save(playlist); // Ensure the playlist is saved

        // Verify the song was added
        assertTrue(playlist.getSongs().contains(song), "The song should be present in the playlist before removal");

        // Remove the song
        ResponseStatus status = playlistService.removeSongFromPlaylist(playlist.getPlaylistID(), song.getSongID());
        assertEquals(ResponseStatus.SUCCESS, status, "Song should be removed successfully");

        // Refresh the playlist from the repository to ensure we have the latest state
        Playlist updatedPlaylist = playlistRepository.findById(playlist.getPlaylistID()).orElseThrow();

        // Assert that the song is no longer in the playlist
        assertFalse(updatedPlaylist.getSongs().contains(song), "The song should be removed from the playlist");
    }

    /**
     * Tests that attempting to remove a song from a non-existent playlist returns the appropriate error status.
     */
    @Test
    void testRemoveSongFromNonExistentPlaylist() {
        ResponseStatus status = playlistService.removeSongFromPlaylist(999L, 1L);
        assertEquals(ResponseStatus.PLAYLIST_NOT_FOUND, status, "Removing a song from a non-existent playlist should return PLAYLIST_NOT_FOUND");
    }
}
