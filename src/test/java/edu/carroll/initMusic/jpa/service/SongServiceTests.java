package edu.carroll.initMusic.jpa.service;

import edu.carroll.initMusic.jpa.model.Playlist;
import edu.carroll.initMusic.jpa.model.Song;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.jpa.repo.PlaylistRepository;
import edu.carroll.initMusic.jpa.repo.SongRepository;
import edu.carroll.initMusic.jpa.repo.UserRepository;
import edu.carroll.initMusic.service.SongService;
import edu.carroll.initMusic.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
public class SongServiceTests {

    @Autowired
    private SongService songService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private SongRepository songRepository;

    /**
     * This test verifies the SearchForSongs function
     */
    @Test
    public void verifySearchForSongs(){
        Set<Song> songs = songService.searchForSongs("Zach Bryan");
        assertFalse("There should be at least one song found for query 'Zach Bryan'", songs.isEmpty());

        songs = songService.searchForSongs("");
        assertTrue("A empty search should return an empty set of songs", songs.isEmpty());

        songs = songService.searchForSongs("  ");
        assertTrue("A search of only whitespace should return an empty set of songs'", songs.isEmpty());

        songs = songService.searchForSongs("12");
        assertTrue("A search with length less than 3 should return an empty set of songs", songs.isEmpty());
    }
}
