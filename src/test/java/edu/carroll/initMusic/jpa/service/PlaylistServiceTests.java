package edu.carroll.initMusic.jpa.service;

import edu.carroll.initMusic.ResponseStatus;
import edu.carroll.initMusic.jpa.model.Playlist;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.jpa.repo.PlaylistRepository;
import edu.carroll.initMusic.jpa.repo.SongRepository;
import edu.carroll.initMusic.jpa.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import edu.carroll.initMusic.service.PlaylistService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Create a test user and save it in the repository
        userRepository.deleteAll();
        testUser = new User("username", "hashedPassword", "firstName", "lastName", "email", "question1", "question2", "answer1", "answer2");
        Playlist playlist = new Playlist(testUser, "My Playlist"); // Assuming playlist has a constructor that takes a user
        testUser.addPlaylist(playlist);
        userRepository.save(testUser); // Save user to the test database
    }

    @Test
    void testCreatePlaylist_Success() {
        ResponseStatus status = playlistService.createPlaylist("Test Playlist", testUser);
        assertEquals(ResponseStatus.SUCCESS, status);

        List<Playlist> playlists = playlistRepository.findAll();
        assertEquals(1, playlists.size());
        assertEquals("Test Playlist", playlists.get(0).getPlaylistName());
    }

    @Test
    void testCreatePlaylist_NameExists() {
        playlistService.createPlaylist("Test Playlist", testUser);

        ResponseStatus status = playlistService.createPlaylist("Test Playlist", testUser);
        assertEquals(ResponseStatus.PLAYLIST_NAME_EXISTS, status);
    }

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

    @Test
    void testRenamePlaylist_NameExists() {
        playlistService.createPlaylist("Playlist One", testUser);
        playlistService.createPlaylist("Playlist Two", testUser);

        Playlist playlistOne = testUser.getPlaylist("Playlist One");
        ResponseStatus status = playlistService.renamePlaylist("Playlist Two", playlistOne.getPlaylistID(), testUser);
        assertEquals(ResponseStatus.PLAYLIST_NAME_EXISTS, status);
    }

    @Test
    void testDeletePlaylist_Success() {
        playlistService.createPlaylist("Delete Me", testUser);

        Playlist playlist = testUser.getPlaylists().iterator().next();
        ResponseStatus status = playlistService.deletePlaylist("Delete Me", playlist.getPlaylistID(), testUser);
        assertEquals(ResponseStatus.SUCCESS, status);

        assertTrue(playlistRepository.findById(playlist.getPlaylistID()).isEmpty());
    }

    @Test
    void testDeletePlaylist_NotFound() {
        ResponseStatus status = playlistService.deletePlaylist("Nonexistent Playlist", 1L, testUser);
        assertEquals(ResponseStatus.PLAYLIST_NOT_FOUND, status);
    }
}
