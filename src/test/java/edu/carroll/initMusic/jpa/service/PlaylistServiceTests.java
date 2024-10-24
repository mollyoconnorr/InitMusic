package edu.carroll.initMusic.jpa.service;

import edu.carroll.initMusic.ResponseStatus;
import edu.carroll.initMusic.jpa.model.Playlist;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.jpa.repo.PlaylistRepository;
import edu.carroll.initMusic.jpa.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import edu.carroll.initMusic.service.PlaylistService;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the PlaylistService class.
 * This class tests the functionality of playlist creation, renaming, and deletion.
 */
@SpringBootTest
public class PlaylistServiceTests {

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

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
     * Asserts that the playlist is created successfully and is retrievable from the repository.
     */
    @Test
    void testCreatePlaylist_Success() {
        ResponseStatus status = playlistService.createPlaylist("Test Playlist", testUser);
        assertEquals(ResponseStatus.SUCCESS, status);

        List<Playlist> playlists = playlistRepository.findAll();
        assertEquals(1, playlists.size());
        assertEquals("Test Playlist", playlists.get(0).getPlaylistName());
    }

    /**
     * Tests the creation of a playlist with a name that already exists.
     * Asserts that the appropriate response status is returned when the playlist name exists.
     */
    @Test
    void testCreatePlaylist_NameExists() {
        playlistService.createPlaylist("Test Playlist", testUser);

        ResponseStatus status = playlistService.createPlaylist("Test Playlist", testUser);
        assertEquals(ResponseStatus.PLAYLIST_NAME_EXISTS, status);
    }

    /**
     * Tests the successful renaming of a playlist.
     * Asserts that the playlist is renamed successfully and the change is reflected in the repository.
     */
    @Test
    void testRenamePlaylist_Success() {
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
    void testRenamePlaylist_NameExists() {
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
    void testDeletePlaylist_Success() {
        playlistService.createPlaylist("Delete Me", testUser);

        Playlist playlist = testUser.getPlaylists().iterator().next();
        ResponseStatus status = playlistService.deletePlaylist("Delete Me", playlist.getPlaylistID(), testUser);
        assertEquals(ResponseStatus.SUCCESS, status);

        assertTrue(playlistRepository.findById(playlist.getPlaylistID()).isEmpty());
    }

    /**
     * Tests the deletion of a playlist that does not exist.
     * Asserts that the appropriate response status is returned when attempting to delete a non-existent playlist.
     */
    @Test
    void testDeletePlaylist_NotFound() {
        ResponseStatus status = playlistService.deletePlaylist("Nonexistent Playlist", 1L, testUser);
        assertEquals(ResponseStatus.PLAYLIST_NOT_FOUND, status);
    }
}
