package edu.carroll.initMusic.service;

import edu.carroll.initMusic.MethodOutcome;
import edu.carroll.initMusic.jpa.model.Playlist;
import edu.carroll.initMusic.jpa.model.Song;
import edu.carroll.initMusic.jpa.model.User;
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

    // Happy Paths for getPlaylist

    /**
     * Tests the success scenario when retrieving a playlist by ID.
     */
    @Transactional
    @Test
    void testGetPlaylistSuccess() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Test Playlist", testUser);
        // This calls getPlaylist, but the function is part of UserService not the one we are currently testing
        Playlist testPlaylist = testUser.getPlaylist("Test Playlist");
        Playlist getPlaylist = playlistService.getPlaylist(testPlaylist.getPlaylistID());
        assertNotNull( getPlaylist, "Should successfully retrieve the same playlist.");
    }

    /**
     * Tests the success scenario when retrieving two playlists by ID.
     */
    @Transactional
    @Test
    void testGetPlaylistForTwoPlaylistsSuccess() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Test Playlist", testUser);
        Playlist testPlaylist = testUser.getPlaylist("Test Playlist");
        playlistService.createPlaylist("Test Playlist2", testUser);
        Playlist testPlaylist2 = testUser.getPlaylist("Test Playlist");
        Playlist getPlaylist = playlistService.getPlaylist(testPlaylist.getPlaylistID());
        assertEquals(testPlaylist, getPlaylist, "Should successfully retrieve the same playlist.");
        Playlist getPlaylist2 = playlistService.getPlaylist(testPlaylist2.getPlaylistID());
        assertEquals(testPlaylist2, getPlaylist2, "Should successfully retrieve the second playlist.");
    }


    // Crappy Paths for getPlaylist

    /**
     * Tests the scenario where the user has no playlists.
     */
    @Transactional
    @Test
    void testGetPlaylistForUserWithNoPlaylists() {
        // Create a test user but do not add any playlists
        testUser = userService.findByUsername("username");
        // Attempt to retrieve a playlist for a user with no playlists
        Playlist playlist = playlistService.getPlaylist(1L);
        assertNull(playlist, "Should return null when attempting to get a playlist from a user with no playlists.");
    }

    /**
     * Tests the scenario where the user doesn't have the playlist that is trying to be retrieved.
     */
    @Transactional
    @Test
    void testGetPlaylistForUserWithNonExistentPlaylists() {
        // Create a test user but do not add any playlists
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Test Playlist", testUser);
        Playlist playlist = playlistService.getPlaylist(8000L);
        assertNull(playlist, "Should return null for a playlists ID that doesn't exist even if there are other playlists there");
    }

    // Crazy Paths for getPlaylist

    /**
     * Tests the scenario where the user retrieves 'null'.
     */
    @Transactional
    @Test
    void testGetPlaylistWithNullNameFailure() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Test Playlist", testUser);
        Playlist getPlaylist = playlistService.getPlaylist(null);
        assertNull(getPlaylist, "Should successfully return null for retrieving a 'null' playlist");
    }

    // Happy Path for renamePlaylist

    /**
     * Tests the successful renaming of a playlist.
     */
    @Transactional
    @Test
    void testRenamePlaylistSuccess() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Old Playlist", testUser);
        Playlist playlist = testUser.getPlaylist("Old Playlist");
        MethodOutcome status = playlistService.renamePlaylist("New Playlist", playlist.getPlaylistID(), testUser);
        assertEquals(MethodOutcome.SUCCESS, status, "Renaming should be successful.");
        assertNotNull(testUser.getPlaylist("New Playlist"), "User should have new playlist");
        assertEquals("New Playlist", playlist.getPlaylistName(), "Playlist name should be updated.");
    }

    /**
     * Tests the successful renaming of two playlists.
     */
    @Transactional
    @Test
    void testRenameTwoPlaylistsSuccess() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Old Playlist", testUser);
        Playlist playlist = testUser.getPlaylist("Old Playlist");
        playlistService.createPlaylist("Old Playlist2", testUser);
        Playlist playlist2 = testUser.getPlaylist("Old Playlist2");
        MethodOutcome status = playlistService.renamePlaylist("New Playlist", playlist.getPlaylistID(), testUser);
        MethodOutcome status2 = playlistService.renamePlaylist("New Playlist2", playlist2.getPlaylistID(), testUser);
        assertEquals(MethodOutcome.SUCCESS, status, "Renaming should be successful.");
        assertEquals(MethodOutcome.SUCCESS, status2, "Renaming of second playlist should be successful.");
        assertNotNull(testUser.getPlaylist("New Playlist"), "User should have new playlist");
        assertNotNull(testUser.getPlaylist("New Playlist2"), "User should have second new playlist");
        assertEquals("New Playlist", playlist.getPlaylistName(), "Playlist name should be updated.");
        assertEquals("New Playlist2", playlist2.getPlaylistName(), "Second playlist name should be updated.");
    }

    // Crappy path for renamePlaylist

    /**
     * Tests renaming a playlist to a duplicate name.
     */
    @Transactional
    @Test
    void testRenamePlaylistToDuplicateNameFailure() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Playlist One", testUser);
        playlistService.createPlaylist("Playlist Two", testUser);
        Playlist playlist = testUser.getPlaylist("Playlist One");
        MethodOutcome status = playlistService.renamePlaylist("Playlist Two", playlist.getPlaylistID(), testUser);
        assertEquals(MethodOutcome.PLAYLIST_NAME_EXISTS, status, "Renaming should fail when using a duplicate name.");
        assertNotNull(testUser.getPlaylist("Playlist One"), "User should have original playlist");
        assertNotNull(testUser.getPlaylist("Playlist Two"), "User should have still have second playlist that was unable to be renamed");
    }

    /**
     * Tests renaming a playlist that does not exist.
     */
    @Transactional
    @Test
    void testRenameNonExistentPlaylistFailure() {
        testUser = userService.findByUsername("username");
        MethodOutcome status = playlistService.renamePlaylist("Nonexistent Playlist", 9999L, testUser);
        assertEquals(MethodOutcome.PLAYLIST_NOT_FOUND, status, "Renaming should fail for a non-existent playlist.");
        assertNull(testUser.getPlaylist("NonexistentPlaylist"), "User shouldn't have any playlists.");
    }

    /**
     * Tests renaming a playlist for a user that does not exist.
     */
    @Transactional
    @Test
    void testRenameNonExistentUserFailure() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Playlist One", testUser);
        // playlistService.createPlaylist("Playlist Two", testUser);
        Playlist playlist = testUser.getPlaylist("Playlist One");
        User fakeUser = new User("username", "hashedPassword", "firstName", "lastName", "email@example.com", "question1", "question2", "answer1", "answer2");
        fakeUser.setAccountCreationDate(LocalDateTime.of(2024, 11, 20, 15, 30, 0));
        fakeUser.setuserID(6L);
        MethodOutcome status = playlistService.renamePlaylist("New Playlist", playlist.getPlaylistID(), fakeUser);
        assertEquals(MethodOutcome.USER_NOT_FOUND, status, "Renaming should fail for a non-existent user.");
        assertNull(fakeUser.getPlaylist("New Playlist"), "User shouldn't have any playlists.");
    }

    // Crazy paths for renamePlaylist

    /**
     * Tests renaming a playlist null.
     */
    @Transactional
    @Test
    void testRenamePlaylistNullFailure() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Old Playlist", testUser);
        Playlist playlist = testUser.getPlaylist("Old Playlist");
        MethodOutcome status = playlistService.renamePlaylist(null, playlist.getPlaylistID(), testUser);
        assertEquals(MethodOutcome.PLAYLIST_NAME_INVALID, status, "Renaming should be unsuccessful.");
        assertNull(testUser.getPlaylist(null), "User shouldn't have a new playlist");
    }

    /**
     * Tests renaming a playlist blank.
     */
    @Transactional
    @Test
    void testRenamePlaylistBlankFailure() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Old Playlist", testUser);
        Playlist playlist = testUser.getPlaylist("Old Playlist");
        MethodOutcome status = playlistService.renamePlaylist("", playlist.getPlaylistID(), testUser);
        assertEquals(MethodOutcome.PLAYLIST_NAME_INVALID, status, "Renaming should be unsuccessful.");
        assertNull(testUser.getPlaylist(""), "User shouldn't have a new playlist");
    }

    /**
     * Tests renaming an excessively long name.
     */
    @Transactional
    @Test
    void testRenameExcessivelyLongPlaylistNameFailure() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Old Playlist", testUser);
        Playlist playlist = testUser.getPlaylist("Old Playlist");
        String longName = "A".repeat(5000);
        MethodOutcome status = playlistService.renamePlaylist(longName, playlist.getPlaylistID(), testUser);
        assertEquals(MethodOutcome.PLAYLIST_NAME_INVALID, status, "Renaming should be unsuccessful.");
        assertNull(testUser.getPlaylist(longName), "User shouldn't have a new playlist");
    }

    // Happy Path for deletePlaylist

    /**
     * Tests the successful deletion of a playlist.
     */
    @Transactional
    @Test
    void testDeletePlaylistSuccess() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Test Playlist", testUser);
        Playlist playlist = testUser.getPlaylist("Test Playlist");
        assertEquals(1, playlistService.getRepoSize(), "Should be one playlist in repo before deleting.");
        MethodOutcome status = playlistService.deletePlaylist("Test Playlist", playlist.getPlaylistID(), testUser);
        assertEquals(MethodOutcome.SUCCESS, status, "Deleting should be successful.");
        assertNull(testUser.getPlaylist("Test Playlist"), "User should not have new playlist");
        assertEquals(0, playlistService.getRepoSize(), "Should be no more playlists in repo.");
    }

    /**
     * Tests the successful deletion of two playlists.
     */
    @Transactional
    @Test
    void testDeleteTwoPlaylistsSuccess() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Test Playlist", testUser);
        Playlist playlist = testUser.getPlaylist("Test Playlist");
        playlistService.createPlaylist("Test Playlist2", testUser);
        Playlist playlist2 = testUser.getPlaylist("Test Playlist2");
        assertEquals(2, playlistService.getRepoSize(), "Should be two playlists in repo before deleting.");
        MethodOutcome status = playlistService.deletePlaylist("Test Playlist", playlist.getPlaylistID(), testUser);
        assertEquals(1, playlistService.getRepoSize(), "Should be one playlist in repo.");
        MethodOutcome status2 = playlistService.deletePlaylist("Test Playlist2", playlist2.getPlaylistID(), testUser);
        assertEquals(MethodOutcome.SUCCESS, status, "Deleting should be successful.");
        assertEquals(MethodOutcome.SUCCESS, status, "Deleting for second playlist should be successful.");
        assertNull(testUser.getPlaylist("Test Playlist"), "User should not have deleted playlist");
        assertNull(testUser.getPlaylist("Test Playlist2"), "User should not have deleted playlist");
        assertEquals(0, playlistService.getRepoSize(), "Should be no more playlists in repo.");
    }

    // Crappy path for renamePlaylist


    /**
     * Tests deleting a playlist that does not exist.
     */
    @Transactional
    @Test
    void testDeleteNonExistentPlaylistFailure() {
        testUser = userService.findByUsername("username");
        MethodOutcome status = playlistService.deletePlaylist("Nonexistent Playlist", 9999L, testUser);
        assertEquals(MethodOutcome.PLAYLIST_NOT_FOUND, status, "Deleting should fail for a non-existent playlist.");
    }

    /**
     * Tests deleting a playlist for a user that does not exist.
     */
    @Transactional
    @Test
    void testDeletePlaylistNonExistentUserFailure() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Playlist One", testUser);
        Playlist playlist = testUser.getPlaylist("Playlist One");
        User fakeUser = new User("username", "hashedPassword", "firstName", "lastName", "email@example.com", "question1", "question2", "answer1", "answer2");
        fakeUser.setAccountCreationDate(LocalDateTime.of(2024, 11, 20, 15, 30, 0));
        fakeUser.setuserID(6L);
        MethodOutcome status = playlistService.deletePlaylist("New Playlist", playlist.getPlaylistID(), fakeUser);
        assertEquals(MethodOutcome.USER_NOT_FOUND, status, "Renaming should fail for a non-existent user.");
    }

    // Crazy paths for renamePlaylist

    /**
     * Tests deleting a playlist null.
     */
    @Transactional
    @Test
    void testDeletePlaylistNullFailure() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Test Playlist", testUser);
        Playlist playlist = testUser.getPlaylist("Test Playlist");
        assertEquals(1, playlistService.getRepoSize(), "Playlist should be added successfully, there should be one playlist.");
        MethodOutcome status = playlistService.deletePlaylist(null, playlist.getPlaylistID(), testUser);
        assertEquals(MethodOutcome.PLAYLIST_NAME_INVALID, status, "Deleting should be unsuccessful.");
        assertNotNull(testUser.getPlaylist("Test Playlist"), "User shouldn still have a playlist");
        assertEquals(1, playlistService.getRepoSize(), "Deleting should be unsuccessful, there should still be one playlist.");
    }

    /**
     * Tests renaming a playlist blank.
     */
    @Transactional
    @Test
    void testDeletePlaylistBlankFailure() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Test Playlist", testUser);
        Playlist playlist = testUser.getPlaylist("Test Playlist");
        assertEquals(1, playlistService.getRepoSize(), "Playlist should be added successfully, there should be one playlist.");
        MethodOutcome status = playlistService.deletePlaylist("", playlist.getPlaylistID(), testUser);
        assertEquals(MethodOutcome.PLAYLIST_NAME_INVALID, status, "Deleting should be unsuccessful.");
        assertNotNull(testUser.getPlaylist("Test Playlist"), "User should still have one playlist");
        assertEquals(1, playlistService.getRepoSize(), "Deleting should be unsuccessful, there should still be one song.");
    }

    /**
     * Tests deleting an excessively long name.
     */
    @Transactional
    @Test
    void testDeleteExcessivelyLongPlaylistNameFailure() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Test Playlist", testUser);
        Playlist playlist = testUser.getPlaylist("Test Playlist");
        assertEquals(1, playlistService.getRepoSize(), "Playlist should be added successfully, there should be one playlist.");
        String longName = "A".repeat(5000);
        MethodOutcome status = playlistService.deletePlaylist(longName, playlist.getPlaylistID(), testUser);
        assertEquals(MethodOutcome.PLAYLIST_NAME_INVALID, status, "Deleting should be unsuccessful.");
        assertNotNull(testUser.getPlaylist("Test Playlist"), "User should still have one playlist");
        assertEquals(1, playlistService.getRepoSize(), "Deleting should be unsuccessful, there should still be one song.");
    }

    // Happy Paths for removeSongToPlaylist

    /**
     * Tests the successful removal of a song from a playlist.
     */
    @Transactional
    @Test
    void testRemoveSongToPlaylistSuccess() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Test Playlist", testUser);
        Playlist testPlaylist = testUser.getPlaylist("Test Playlist");
        Song testSong = new Song(1L, "Song Title", 3, "Artist", 2021L, "Album", 0L);
        playlistService.addSongToPlaylist(testPlaylist, testSong);
        assertEquals(1, testPlaylist.getNumberOfSongs(), "Should be one song in the playlist.");
        MethodOutcome status = playlistService.removeSongFromPlaylist(testPlaylist.getPlaylistID(), testSong.getDeezerID());
        assertEquals(MethodOutcome.SUCCESS, status, "Song should be removed to playlist");
        assertEquals(0, testPlaylist.getNumberOfSongs(), "Should be no more songs in the playlist.");
    }

    /**
     * Tests the successful removal of two songs from a playlist.
     */
    @Transactional
    @Test
    void testRemoveTwoSongsToPlaylistSuccess() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Test Playlist", testUser);
        Playlist testPlaylist = testUser.getPlaylist("Test Playlist");
        Song testSong = new Song(1L, "Song Title", 3, "Artist", 2021L, "Album", 0L);
        Song testSong2 = new Song(2L, "Song Title2", 3, "Artist", 2022L, "Album", 1L);
        playlistService.addSongToPlaylist(testPlaylist, testSong);
        playlistService.addSongToPlaylist(testPlaylist, testSong2);
        assertEquals(2, testPlaylist.getNumberOfSongs(), "Should be two songs in the playlist.");
        MethodOutcome status = playlistService.removeSongFromPlaylist(testPlaylist.getPlaylistID(), testSong.getDeezerID());
        assertEquals(1, testPlaylist.getNumberOfSongs(), "One song should be removed from the playlist.");
        MethodOutcome status2 = playlistService.removeSongFromPlaylist(testPlaylist.getPlaylistID(), testSong2.getDeezerID());
        assertEquals(0, testPlaylist.getNumberOfSongs(), "Should be no songs in the playlist.");
        assertEquals(MethodOutcome.SUCCESS, status, "Song should be removed from playlist");
        assertEquals(MethodOutcome.SUCCESS, status2, "Second song should be removed from playlist");
    }

    /**
     * Tests the successful removal of the same song from two different playlists.
     */
    @Transactional
    @Test
    void testRemoveSameSongFromTwoPlaylistsSuccess() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Test Playlist", testUser);
        Playlist testPlaylist = testUser.getPlaylist("Test Playlist");
        playlistService.createPlaylist("Test Playlist2", testUser);
        Playlist testPlaylist2 = testUser.getPlaylist("Test Playlist2");
        Song testSong = new Song(1L, "Song Title", 3, "Artist", 2021L, "Album", 0L);
        playlistService.addSongToPlaylist(testPlaylist, testSong);
        playlistService.addSongToPlaylist(testPlaylist2, testSong);
        assertEquals(1, testPlaylist.getNumberOfSongs(), "Should be one song in the first playlist.");
        assertEquals(1, testPlaylist2.getNumberOfSongs(), "Should be one song in the second playlist.");
        MethodOutcome status = playlistService.removeSongFromPlaylist(testPlaylist.getPlaylistID(), testSong.getDeezerID());
        assertEquals(0, testPlaylist.getNumberOfSongs(), "Song should be removed from the first playlist.");
        MethodOutcome status2 = playlistService.removeSongFromPlaylist(testPlaylist2.getPlaylistID(), testSong.getDeezerID());
        assertEquals(0, testPlaylist2.getNumberOfSongs(), "Song should be removed from the second playlist.");
        assertEquals(MethodOutcome.SUCCESS, status, "Song should be removed from playlist");
        assertEquals(MethodOutcome.SUCCESS, status2, "Second song should be removed from playlist");
    }

    // Crazy Tests for addSongToPlaylist

    /**
     * Tests the failure scenario when trying to remove a null song from a playlist.
     */
    @Transactional
    @Test
    void testRemoveNullSongFromPlaylistFailure() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Test Playlist", testUser);
        Playlist testPlaylist = testUser.getPlaylist("Test Playlist");
        Song testSong = new Song(1L, "Song Title", 3, "Artist", 2021L, "Album", 0L);
        playlistService.addSongToPlaylist(testPlaylist, testSong);
        assertEquals(1, testPlaylist.getNumberOfSongs(), "Should be one song in the playlist.");
        MethodOutcome status = playlistService.removeSongFromPlaylist(testPlaylist.getPlaylistID(), null);
        assertEquals(MethodOutcome.INVALID_SONG, status, "Should fail when attempting to remove a null song.");
        assertEquals(1, testPlaylist.getNumberOfSongs(), "Should still be a song in the playlist (nothing got removed).");
    }

    /**
     * Tests the failure scenario when trying to remove a song from a null playlist.
     */
    @Transactional
    @Test
    void testRemoveSongFromNullPlaylistFailure() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Test Playlist", testUser);
        Playlist testPlaylist = testUser.getPlaylist("Test Playlist");
        Song testSong = new Song(1L, "Song Title", 3, "Artist", 2021L, "Album", 0L);
        playlistService.addSongToPlaylist(testPlaylist, testSong);
        assertEquals(1, testPlaylist.getNumberOfSongs(), "Should be one song in the playlist.");
        MethodOutcome status = playlistService.removeSongFromPlaylist(null, testSong.getDeezerID());
        assertEquals(MethodOutcome.PLAYLIST_NAME_INVALID, status, "Should fail when attempting to remove from null playlist.");
        assertEquals(1, testPlaylist.getNumberOfSongs(), "Should still be a song in the non-null playlist.");
    }

    // Tests for getRepoSize

    /**
     * Tests that repo size is 0 when nothing has been added
     */
    @Test
    @Transactional
    void testGetRepoSizeEmpty() {
        playlistService.clearRepo(); // Ensure the repository is empty
        long size = playlistService.getRepoSize();
        assertEquals(0, size, "Repository size should be 0 when no playlists are present.");
    }

    /**
     * Tests that repo size is 1 when we add one playlist
     */
    @Test
    @Transactional
    void testGetRepoSizeSinglePlaylist() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Test Playlist", testUser);
        long size = playlistService.getRepoSize();
        assertEquals(1, size, "Repository size should be 1 after adding one playlist.");
    }

    /**
     * Tests the repository size after adding multiple playlists.
     * It creates three playlists for the user and verifies that the repository size is 3.
     */
    @Test
    @Transactional
    void testGetRepoSizeMultiplePlaylists() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Playlist 1", testUser);
        playlistService.createPlaylist("Playlist 2", testUser);
        playlistService.createPlaylist("Playlist 3", testUser);
        long size = playlistService.getRepoSize();
        assertEquals(3, size, "Repository size should be 3 after adding three playlists.");
    }

    /**
     * Tests the repository size after removing a playlist.
     * It creates a playlist, removes it, and then verifies that the repository size is 0.
     */
    @Test
    @Transactional
    void testGetRepoSizeAfterRemove() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Playlist 1", testUser);
        Playlist playlist = testUser.getPlaylist("Playlist 1");
        playlistService.deletePlaylist("Playlist 1", playlist.getPlaylistID(), testUser);
        long size = playlistService.getRepoSize();
        assertEquals(0, size, "Repository size should be 0 after removing the playlist.");
    }

    /**
     * Tests the repository size after clearing all playlists from the repository.
     * It creates two playlists, clears the repository, and verifies that the repository size is 0.
     */
    @Test
    @Transactional
    void testGetRepoSizeAfterClearRepo() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Playlist 1", testUser);
        playlistService.createPlaylist("Playlist 2", testUser);
        playlistService.clearRepo(); // Clear all playlists
        long size = playlistService.getRepoSize();
        assertEquals(0, size, "Repository size should be 0 after clearing the repository.");
    }

    /**
     * Tests the repository size after attempting to create an invalid playlist (null name).
     * It verifies that the repository size remains 0 after an invalid playlist creation attempt.
     */
    @Test
    @Transactional
    void testGetRepoSizeAfterInvalidPlaylist() {
        testUser = userService.findByUsername("username");
        MethodOutcome outcome = playlistService.createPlaylist(null, testUser); // Invalid playlist creation
        assertEquals(MethodOutcome.PLAYLIST_NAME_INVALID, outcome, "Playlist creation should fail with a null name.");

        long size = playlistService.getRepoSize();
        assertEquals(0, size, "Repository size should remain 0 after an invalid playlist creation attempt.");
    }

    // Tests for clearRepo

    /**
     * Tests the behavior of clearRepo() when the repository is empty.
     * Verifies that the repository size remains 0 after calling clearRepo() on an empty repository.
     */
    @Test
    @Transactional
    void testClearRepoWhenEmpty() {
        playlistService.clearRepo(); // Clear the repository
        long size = playlistService.getRepoSize();
        assertEquals(0, size, "Repository size should remain 0 when clearing an empty repository.");
    }

    /**
     * Tests the behavior of clearRepo() when the repository contains a single playlist.
     * Verifies that the repository size becomes 0 after clearing the repository with one playlist.
     */
    @Test
    @Transactional
    void testClearRepoWithSinglePlaylist() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Test Playlist", testUser);

        playlistService.clearRepo(); // Clear the repository
        long size = playlistService.getRepoSize();
        assertEquals(0, size, "Repository size should be 0 after clearing a repository with one playlist.");
    }

    /**
     * Tests the behavior of clearRepo() when the repository contains multiple playlists.
     * Verifies that the repository size becomes 0 after clearing the repository with multiple playlists.
     */
    @Test
    @Transactional
    void testClearRepoWithMultiplePlaylists() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Playlist 1", testUser);
        playlistService.createPlaylist("Playlist 2", testUser);

        playlistService.clearRepo(); // Clear the repository
        long size = playlistService.getRepoSize();
        assertEquals(0, size, "Repository size should be 0 after clearing a repository with multiple playlists.");
    }

    /**
     * Tests the behavior of clearRepo() when called multiple times.
     * Verifies that calling clearRepo() consecutively doesn't cause any issues and the repository size remains 0.
     */
    @Test
    @Transactional
    void testClearRepoMultipleCalls() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Playlist 1", testUser);
        playlistService.clearRepo(); // First clear
        long size = playlistService.getRepoSize();
        assertEquals(0, size, "Repository size should remain 0 after clearRepo() calls.");
        playlistService.createPlaylist("Playlist 1", testUser);
        playlistService.clearRepo(); // Second clear
        assertEquals(0, size, "Repository size should remain 0 after multiple clearRepo() calls.");
    }

    /**
     * Tests the behavior of clearRepo() after adding and removing playlists.
     * Verifies that after adding a playlist, removing it, and calling clearRepo(), the repository size is 0.
     */
    @Test
    @Transactional
    void testClearRepoAfterAddAndRemove() {
        testUser = userService.findByUsername("username");
        playlistService.createPlaylist("Playlist 1", testUser);
        Playlist playlist = testUser.getPlaylist("Playlist 1");
        playlistService.deletePlaylist("Playlist 1", playlist.getPlaylistID(), testUser);
        playlistService.clearRepo(); // Clear the repository
        long size = playlistService.getRepoSize();
        assertEquals(0, size, "Repository size should be 0 after clearing a repository where playlists were added and removed.");
    }
}