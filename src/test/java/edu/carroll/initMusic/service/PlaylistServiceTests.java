package edu.carroll.initMusic.service;

import edu.carroll.initMusic.MethodOutcome;
import edu.carroll.initMusic.jpa.model.Playlist;
import edu.carroll.initMusic.jpa.model.Song;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.jpa.repo.PlaylistRepository;
import edu.carroll.initMusic.jpa.repo.SongRepository;
import edu.carroll.initMusic.jpa.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
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
    private UserService userService;

    @Autowired
    private SongService songService;

    User testUser;

    /**
     * Sets up a test user before each test.
     * This method is executed before each test case to ensure a clean state.
     */
    @BeforeEach
    void setUp() {
        songService.clearRepo();
        playlistService.clearRepo();;
        userService.clearRepo();
        // Use other services
        // Create a test user and save it in the repository
        testUser = new User("username", "hashedPassword", "firstName", "lastName", "email@example.com", "question1", "question2", "answer1", "answer2");
        testUser.setAccountCreationDate(LocalDateTime.of(2024, 11, 20, 15, 30, 0));
        testUser.setuserID(1L);
        userService.updateUserSecurityQuestions(testUser, "q1", "a1", "q2", "a2");
    }

    // Happy Paths for CreatePlaylist

    /**
     * Tests the successful creation of a playlist.
     */
    @Transactional
    @Test
    void testCreatePlaylistSuccess() {
        testUser = userService.findByUsername("username");
        MethodOutcome status = playlistService.createPlaylist("Test Playlist", testUser);
        assertEquals(MethodOutcome.SUCCESS, status, "Playlist should be created successfully");
        assertNotNull(testUser.getPlaylist("Test Playlist"), "The user's playlist collection should include the new playlist");
        assertEquals(1, playlistService.getRepoSize(), "Should be one playlist in the repository");
    }

    /**
     * Tests the successful creation of two playlists.
     */
    @Transactional
    @Test
    void testCreateTwoPlaylistsSuccess() {
        testUser = userService.findByUsername("username");
        MethodOutcome status = playlistService.createPlaylist("Test Playlist", testUser);
        MethodOutcome status2 = playlistService.createPlaylist("Test Playlist2", testUser);
        assertEquals(MethodOutcome.SUCCESS, status, "First Playlist should be created successfully");
        assertEquals(MethodOutcome.SUCCESS, status2, "Second Playlist should be created successfully");
        assertNotNull(testUser.getPlaylist("Test Playlist"), "The user's playlist collection should include the new playlist");
        assertNotNull(testUser.getPlaylist("Test Playlist2"), "The user's playlist collection should include the second new playlist");
        assertEquals(2, playlistService.getRepoSize(), "Should be two playlists in the repository");

    }

    /**
     * Tests the successful creation of playlists with special character.
     */
    @Transactional
    @Test
    void testCreatePlaylistsCharSuccess() {
        testUser = userService.findByUsername("username");
        MethodOutcome status = playlistService.createPlaylist("Test Playlist!!!", testUser);
        assertEquals(MethodOutcome.SUCCESS, status, "First Playlist should be created successfully");
        assertNotNull(testUser.getPlaylist("Test Playlist!!!"), "The user's playlist collection should include the second new playlist");
        assertEquals(1, playlistService.getRepoSize(), "Should be one playlist in the repository");
    }

    /**
     * Tests the successful creation of playlists with space before name.
     */
    @Transactional
    @Test
    void testCreatePlaylistsWithSpaceSuccess() {
        testUser = userService.findByUsername("username");
        MethodOutcome status = playlistService.createPlaylist("   Test Playlist", testUser);
        assertEquals(MethodOutcome.SUCCESS, status, "First Playlist should be created successfully");
        assertNotNull(testUser.getPlaylist("Test Playlist"), "The user's playlist collection should include the second new playlist");
        assertEquals(1, playlistService.getRepoSize(), "Should be one playlist in the repository");
    }

    // Crappy Paths for CreatePlaylist

    /**
     * Tests the unsuccessful creation of playlists when name is already taken.
     */
    @Transactional
    @Test
    void testDuplicateNameFailure() {
        testUser = userService.findByUsername("username");
        MethodOutcome status = playlistService.createPlaylist("Test Playlist", testUser);
        MethodOutcome status2 = playlistService.createPlaylist("Test Playlist", testUser);
        assertEquals(MethodOutcome.SUCCESS, status, "First Playlist should be created successfully");
        assertEquals(MethodOutcome.PLAYLIST_NAME_EXISTS, status2, "Second Playlist should not be created successfully");
        assertNotNull(testUser.getPlaylist("Test Playlist"), "The user's playlist collection should include the new playlist");
        assertEquals(1, playlistService.getRepoSize(), "Should be one playlist in the repository, because second one wasn't created");
    }

    /**
     * Tests the unsuccessful creation of playlists when name is null.
     */
    @Transactional
    @Test
    void testNullNameFailure() {
        testUser = userService.findByUsername("username");
        MethodOutcome status = playlistService.createPlaylist(null, testUser);
        assertEquals(MethodOutcome.PLAYLIST_NAME_INVALID, status, "Null playlist can't exist");
        assertNull(testUser.getPlaylist("null"), "The user's playlist collection should not include a null playlist");
        assertEquals(0, playlistService.getRepoSize(), "Should be no playlists in the repository");
    }

    /**
     * Tests the unsuccessful creation of playlists when name is just space.
     */
    @Transactional
    @Test
    void testNameWithSpaceFailure() {
        testUser = userService.findByUsername("username");
        MethodOutcome status = playlistService.createPlaylist(" ", testUser);
        assertEquals(MethodOutcome.PLAYLIST_NAME_INVALID, status, "Blank playlist can't exist");
        assertNull(testUser.getPlaylist(" "), "The user's playlist collection should not include a blank playlist");
        assertEquals(0, playlistService.getRepoSize(), "Should be no playlists in the repository");
    }

    /**
     * Tests the unsuccessful creation of playlists when name is empty.
     */
    @Transactional
    @Test
    void testBlankNameFailure() {
        testUser = userService.findByUsername("username");
        MethodOutcome status = playlistService.createPlaylist("", testUser);
        assertEquals(MethodOutcome.PLAYLIST_NAME_INVALID, status, "Blank playlist can't exist");
        assertNull(testUser.getPlaylist(""), "The user's playlist collection should not include a blank playlist");
        assertEquals(0, playlistService.getRepoSize(), "Should be no playlists in the repository");
    }

    // Crazy Paths for CreatePlaylist

    /**
     * Tests the successful creation of playlists when name is extremely large.
     */
    @Transactional
    @Test
    void testExcessivelyLongNameFailure() {
        testUser = userService.findByUsername("username");
        String longName = "A".repeat(5000);
        MethodOutcome status = playlistService.createPlaylist(longName, testUser);
        assertEquals(MethodOutcome.PLAYLIST_NAME_INVALID, status, "Playlist should not be created successfully");
        assertNull(testUser.getPlaylist("A".repeat(5000)), "The user's playlist collection should not include a long name playlist");
        assertEquals(0, playlistService.getRepoSize(), "Should be no playlists in the repository");
    }

    /**
     * Tests the successful creation of playlists when name contains lots of special characters.
     */
    @Transactional
    @Test
    void testSpecialCharacterNameFailure() {
        testUser = userService.findByUsername("username");
        String specialCharName = "!@#$%^&*()_+-=<>?/[]{}|";
        MethodOutcome status = playlistService.createPlaylist(specialCharName, testUser);
        assertEquals(MethodOutcome.SUCCESS, status, "Playlist with special characters should not be created successfully");
        assertNotNull(testUser.getPlaylist(specialCharName), "The user's playlist collection should include a playlist with special characters");
        assertEquals(1, playlistService.getRepoSize(), "Should be one playlists in the repository");
    }

    // Happy Paths for addSongToPlaylist

    /**
     * Tests the successful addition of a song to a playlist.
     */
    @Transactional
    @Test
    void testAddSongToPlaylistSuccess() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Test Playlist", testUser);
        Playlist testPlaylist = testUser.getPlaylist("Test Playlist");
        Song testSong = new Song(1L, "Song Title", 3, "Artist", 2021L, "Album", 0L);
        MethodOutcome status = playlistService.addSongToPlaylist(testPlaylist, testSong);
        assertEquals(MethodOutcome.SUCCESS, status, "Song should be added to playlist");
        assertEquals(1, songService.getRepoSize(), "Should be one song in the repository");
    }

    /**
     * Tests the successful addition of two songs to a playlist.
     */
    @Transactional
    @Test
    void testAddTwoSongsToPlaylistSuccess() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Test Playlist", testUser);
        Playlist testPlaylist = testUser.getPlaylist("Test Playlist");
        Song testSong = new Song(1L, "Song Title", 3, "Artist", 2021L, "Album", 0L);
        Song testSong2 = new Song(2L, "Song Title2", 3, "Artist", 2022L, "Album", 1L);
        MethodOutcome status = playlistService.addSongToPlaylist(testPlaylist, testSong);
        MethodOutcome status2 = playlistService.addSongToPlaylist(testPlaylist, testSong2);
        assertEquals(MethodOutcome.SUCCESS, status, "Song should be added to playlist");
        assertEquals(MethodOutcome.SUCCESS, status2, "Second song should be added to playlist");
    }

    /**
     * Tests the successful addition of the same song to two different playlists.
     */
    @Transactional
    @Test
    void testAddSameSongToTwoPlaylistsSuccess() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Test Playlist", testUser);
        Playlist testPlaylist = testUser.getPlaylist("Test Playlist");
        playlistService.createPlaylist("Test Playlist2", testUser);
        Playlist testPlaylist2 = testUser.getPlaylist("Test Playlist2");
        Song testSong = new Song(1L, "Song Title", 3, "Artist", 2021L, "Album", 0L);
        MethodOutcome status = playlistService.addSongToPlaylist(testPlaylist, testSong);
        MethodOutcome status2 = playlistService.addSongToPlaylist(testPlaylist2, testSong);
        assertEquals(MethodOutcome.SUCCESS, status, "Song should be added to first playlist");
        assertEquals(MethodOutcome.SUCCESS, status2, "Second song should be added to second playlist");
        assertEquals(1, songService.getRepoSize(), "Should be one song in the repository");
    }

    // Crappy Tests for addSongToPlaylist

    /**
     * Tests the failure scenario when trying to add a song to a deleted playlist.
     */
    @Transactional
    @Test
    void testAddSongToDeletedPlaylistFailure() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Test Playlist", testUser);
        Playlist testPlaylist = testUser.getPlaylist("Test Playlist");

        // Delete the playlist
        playlistService.deletePlaylist("Test Playlist", testPlaylist.getPlaylistID(), testUser);

        Song testSong = new Song(1L, "Song Title", 3, "Artist", 2021L, "Album", 0L);
        MethodOutcome status = playlistService.addSongToPlaylist(testPlaylist, testSong); // Attempt to add to deleted playlist
        assertEquals(MethodOutcome.PLAYLIST_NOT_FOUND, status, "Should fail when attempting to add a song to a deleted playlist");
        assertEquals(0, songService.getRepoSize(), "No songs should be added to the repository");
    }

    /**
     * Tests the failure scenario when trying to add a song to a nonexistent playlist.
     */
    @Transactional
    @Test
    void testAddSongToPlaylistNonExistFailure() {
        testUser = userService.findByUsername("username");
        Playlist testPlaylist = new Playlist(testUser, "Test Playlist");
        Song testSong = new Song(1L, "Song Title", 3, "Artist", 2021L, "Album", 0L);
        MethodOutcome status = playlistService.addSongToPlaylist(testPlaylist, testSong);
        assertEquals(MethodOutcome.PLAYLIST_NOT_FOUND, status, "Should fail when attempting to add a song to a nonexistent playlist");
        assertEquals(0, songService.getRepoSize(), "No songs should be added to the repository");
    }

    // Crazy Tests for addSongToPlaylist

    /**
     * Tests the failure scenario when trying to add a null song ot a playlist.
     */
    @Transactional
    @Test
    void testAddNullSongToPlaylistFailure() {
        testUser = userService.findByUsername("username");
        Playlist testPlaylist = new Playlist(testUser, "Test Playlist");
        MethodOutcome status = playlistService.addSongToPlaylist(testPlaylist, null);
        assertEquals(MethodOutcome.INVALID_SONG, status, "Should fail when attempting to add a null song.");
        assertEquals(0, songService.getRepoSize(), "No songs should be added to the repository");
    }

    /**
     * Tests the failure scenario when trying to add a song ot a null playlist.
     */
    @Transactional
    @Test
    void testAddSongToNullPlaylistFailure() {
        testUser = userService.findByUsername("username");
        Song testSong = new Song(1L, "Song Title", 3, "Artist", 2021L, "Album", 0L);
        MethodOutcome status = playlistService.addSongToPlaylist(null, testSong);
        assertEquals(MethodOutcome.PLAYLIST_NOT_FOUND, status, "Should fail when attempting to add song to null playlist.");
        assertEquals(0, songService.getRepoSize(), "No songs should be added to the repository");
    }

///**
// * Unit tests for the PlaylistService class.
// * This class tests the functionality of playlist creation, renaming, and deletion.
// */
//@Transactional
//@SpringBootTest
//public class PlaylistServiceTests {
//
//    @Autowired
//    private PlaylistService playlistService;
//
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PlaylistRepository playlistRepository;
//
//    @Autowired
//    private SongRepository songRepository;
//
//    private User testUser;
//
//    /**
//     * Sets up a test user and an initial playlist before each test.
//     * This method is executed before each test case to ensure a clean state.
//     */
//    @BeforeEach
//    void setUp() {
//        // Create a test user and save it in the repository
//        userRepository.deleteAll();
//        testUser = new User("username", "hashedPassword", "firstName", "lastName", "email", "question1", "question2", "answer1", "answer2");
//        Playlist playlist = new Playlist(testUser, "My Playlist"); // Assuming playlist has a constructor that takes a user
//        testUser.addPlaylist(playlist);
//        userRepository.save(testUser); // Save user to the test database
//    }
//
//    /**
//     * Tests the successful creation of a playlist.
//     */
//    @Test
//    void testCreatePlaylistSuccess() {
//        MethodOutcome status = playlistService.createPlaylist("Test Playlist", testUser);
//        assertEquals(MethodOutcome.SUCCESS, status, "Playlist should be created successfully");
//    }
//
//    /**
//     * Tests that the created playlist is retrievable from the repository.
//     */
//    @Test
//    void testRetrieveCreatedPlaylist() {
//        playlistService.createPlaylist("Test Playlist", testUser);
//        List<Playlist> playlists = playlistRepository.findAll();
//        assertEquals(1, playlists.size(), "There should be one playlist in the repository");
//        assertEquals("Test Playlist", playlists.get(0).getPlaylistName(), "The playlist name should match the created name");
//    }
//
//    /**
//     * Tests that the count of playlists is correct after creation.
//     */
//    @Test
//    void testPlaylistCountAfterCreation() {
//        playlistService.createPlaylist("Test Playlist", testUser);
//        long count = playlistRepository.count();
//        assertEquals(1, count, "There should be one playlist in the repository after creation");
//    }
//
//    /**
//     * Tests that the count of playlists is correct after creation of two playlists.
//     */
//    @Test
//    void testDoublePlaylistCountAfterCreation() {
//        playlistService.createPlaylist("Test Playlist", testUser);
//        playlistService.createPlaylist("Second Test Playlist", testUser);
//        long count = playlistRepository.count();
//        assertEquals(2, count, "There should be two playlists in the repository after creation");
//    }
//
//    /**
//     * Tests that creating a duplicate playlist name doesn't work.
//     */
//    @Test
//    void testCreateDuplicatePlaylistName() {
//        // Create the initial playlist
//        MethodOutcome status = playlistService.createPlaylist("Test Playlist", testUser);
//        assertEquals(MethodOutcome.SUCCESS, status); // Ensure the first creation is successful
//
//        // Attempt to create a duplicate playlist
//        MethodOutcome duplicateStatus = playlistService.createPlaylist("Test Playlist", testUser);
//
//        // Assert that the response status is PLAYLIST_NAME_EXISTS
//        assertEquals(MethodOutcome.PLAYLIST_NAME_EXISTS, duplicateStatus,
//                "Creating a duplicate playlist should return PLAYLIST_NAME_EXISTS");
//    }
//
//    /**
//     * Tests the successful renaming of a playlist.
//     * Asserts that the playlist is renamed successfully and the change is reflected in the repository.
//     */
//    @Test
//    void testRenamePlaylistSuccess() {
//        playlistService.createPlaylist("Old Playlist", testUser);
//
//        Playlist playlist = testUser.getPlaylists().iterator().next();
//        MethodOutcome status = playlistService.renamePlaylist("New Playlist", playlist.getPlaylistID(), testUser);
//        assertEquals(MethodOutcome.SUCCESS, status);
//
//        Playlist renamedPlaylist = playlistRepository.findById(playlist.getPlaylistID()).orElse(null);
//        assertNotNull(renamedPlaylist);
//        assertEquals("New Playlist", renamedPlaylist.getPlaylistName());
//    }
//
//    /**
//     * Tests the renaming of a playlist to a name that already exists.
//     * Asserts that the appropriate response status is returned when attempting to rename to an existing name.
//     */
//    @Test
//    void testRenamePlaylistNameExists() {
//        playlistService.createPlaylist("Playlist One", testUser);
//        playlistService.createPlaylist("Playlist Two", testUser);
//
//        Playlist playlistOne = testUser.getPlaylist("Playlist One");
//        MethodOutcome status = playlistService.renamePlaylist("Playlist Two", playlistOne.getPlaylistID(), testUser);
//        assertEquals(MethodOutcome.PLAYLIST_NAME_EXISTS, status);
//    }
//
//    /**
//     * Tests the successful deletion of a playlist.
//     * Asserts that the playlist is deleted successfully and is no longer found in the repository.
//     */
//    @Test
//    void testDeletePlaylistSuccess() {
//        // Create and save the playlist in the database
//        MethodOutcome status = playlistService.createPlaylist("Delete Me", testUser);
//        assertEquals(MethodOutcome.SUCCESS, status);
//
//        // Retrieve the playlist from the user's list or directly from the repository
//        Playlist playlist = testUser.getPlaylists().stream()
//                .filter(p -> "Delete Me".equals(p.getPlaylistName()))
//                .findFirst()
//                .orElse(null);
//        assertNotNull(playlist, "Playlist should not be null after creation");
//
//        // Perform the delete operation using the actual persisted playlist ID
//        status = playlistService.deletePlaylist("Delete Me", playlist.getPlaylistID(), testUser);
//        assertEquals(MethodOutcome.SUCCESS, status);
//
//        // Assert that the playlist is no longer found in the repository
//        assertTrue(playlistRepository.findById(playlist.getPlaylistID()).isEmpty());
//    }
//
//    /**
//     * Tests the deletion of a playlist that does not exist.
//     * Asserts that the appropriate response status is returned when attempting to delete a non-existent playlist.
//     */
//    @Test
//    void testDeletePlaylistNotFound() {
//        MethodOutcome status = playlistService.deletePlaylist("Nonexistent Playlist", 1L, testUser);
//        assertEquals(MethodOutcome.PLAYLIST_NOT_FOUND, status);
//    }
//
//    /**
//     * Tests that attempting to create a playlist with an empty name returns the appropriate error status.
//     */
//    @Test
//    void testCreatePlaylistEmptyName() {
//        MethodOutcome status = playlistService.createPlaylist("", testUser);
//        assertEquals(MethodOutcome.PLAYLIST_NAME_INVALID, status, "Empty playlist name should return PLAYLIST_NAME_INVALID");
//    }
//
//    /**
//     * Tests that attempting to create a playlist with a null name returns the appropriate error status.
//     */
//    @Test
//    void testCreatePlaylistNullName() {
//        MethodOutcome status = playlistService.createPlaylist(null, testUser);
//        assertEquals(MethodOutcome.PLAYLIST_NAME_INVALID, status, "Null playlist name should return PLAYLIST_NAME_INVALID");
//    }
//
//    /**
//     * Tests that renaming a playlist to an empty name returns the appropriate error status.
//     * Assumes an existing playlist is present for renaming.
//     */
//    @Test
//    void testRenamePlaylistEmptyName() {
//        playlistService.createPlaylist("Existing Playlist", testUser);
//        Playlist playlist = testUser.getPlaylists().iterator().next();
//        MethodOutcome status = playlistService.renamePlaylist("", playlist.getPlaylistID(), testUser);
//        assertEquals(MethodOutcome.PLAYLIST_NAME_INVALID, status, "Renaming to an empty name should return PLAYLIST_NAME_INVALID");
//    }
//
//    /**
//     * Tests that renaming a playlist to a null name returns the appropriate error status.
//     * Assumes an existing playlist is present for renaming.
//     */
//    @Test
//    void testRenamePlaylistNullName() {
//        playlistService.createPlaylist("Existing Playlist", testUser);
//        Playlist playlist = testUser.getPlaylists().iterator().next();
//        MethodOutcome status = playlistService.renamePlaylist(null, playlist.getPlaylistID(), testUser);
//        assertEquals(MethodOutcome.PLAYLIST_NAME_INVALID, status, "Renaming to a null name should return PLAYLIST_NAME_INVALID");
//    }
//
//    /**
//     * Tests that attempting to delete a playlist with an invalid ID returns the appropriate error status.
//     */
//    @Test
//    void testDeletePlaylistInvalidID() {
//        MethodOutcome status = playlistService.deletePlaylist("Invalid Playlist", 999L, testUser);
//        assertEquals(MethodOutcome.PLAYLIST_NOT_FOUND, status, "Deleting with an invalid playlist ID should return PLAYLIST_NOT_FOUND");
//    }
//
//    /**
//     * Tests that initially there are no playlists for the user in the repository.
//     */
//    @Test
//    void testRetrievePlaylistsNoPlaylists() {
//        List<Playlist> playlists = playlistRepository.findAll();
//        assertTrue(playlists.isEmpty(), "There should be no playlists for the user initially");
//    }
//
//    /**
//     * Tests that multiple playlists can be created and verifies that they are retrievable from the repository.
//     */
//    @Test
//    void testCreateMultiplePlaylistsAndCheckRetrieval() {
//        playlistService.createPlaylist("Playlist One", testUser);
//        playlistService.createPlaylist("Playlist Two", testUser);
//
//        List<Playlist> playlists = playlistRepository.findAll();
//        assertEquals(2, playlists.size(), "There should be two playlists in the repository");
//
//        assertTrue(playlists.stream().anyMatch(p -> "Playlist One".equals(p.getPlaylistName())), "Playlist One should exist");
//        assertTrue(playlists.stream().anyMatch(p -> "Playlist Two".equals(p.getPlaylistName())), "Playlist Two should exist");
//    }
//
//    /**
//     * Tests the successful removal of a song from a playlist.
//     * Assumes the song and playlist already exist in the repository.
//     */
//    @Test
//    void testRemoveSongFromPlaylistSuccess() {
//        // Create and save the song to the repository
//        Song song = new Song(1L, "Song Title", 3, "Album", 2021L, "Genre", 0L);
//        songRepository.save(song);
//
//        // Create the playlist and add the song
//        playlistService.createPlaylist("Test Playlist", testUser);
//        Playlist playlist = testUser.getPlaylists().iterator().next();
//
//        // Add the song to the playlist
//        playlistService.addSongToPlaylist(playlist, song);
//        playlistRepository.save(playlist); // Ensure the playlist is saved
//
//        // Verify the song was added
//        assertTrue(playlist.getSongs().contains(song), "The song should be present in the playlist before removal");
//
//        // Remove the song
//        MethodOutcome status = playlistService.removeSongFromPlaylist(playlist.getPlaylistID(), song.getDeezerID());
//        assertEquals(MethodOutcome.SUCCESS, status, "Song should be removed successfully");
//
//        // Refresh the playlist from the repository to ensure we have the latest state
//        Playlist updatedPlaylist = playlistRepository.findById(playlist.getPlaylistID()).orElseThrow();
//
//        // Assert that the song is no longer in the playlist
//        assertFalse(updatedPlaylist.getSongs().contains(song), "The song should be removed from the playlist");
//    }
//
//    /**
//     * Tests that attempting to remove a song from a non-existent playlist returns the appropriate error status.
//     */
//    @Test
//    void testRemoveSongFromNonExistentPlaylist() {
//        MethodOutcome status = playlistService.removeSongFromPlaylist(999L, 1L);
//        assertEquals(MethodOutcome.PLAYLIST_NOT_FOUND, status, "Removing a song from a non-existent playlist should return PLAYLIST_NOT_FOUND");
//    }
}
