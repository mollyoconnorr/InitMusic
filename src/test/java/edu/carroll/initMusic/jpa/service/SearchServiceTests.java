package edu.carroll.initMusic.jpa.service;

import edu.carroll.initMusic.jpa.model.Playlist;
import edu.carroll.initMusic.jpa.model.Song;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.jpa.repo.PlaylistRepository;
import edu.carroll.initMusic.jpa.repo.SongRepository;
import edu.carroll.initMusic.jpa.repo.UserRepository;
import edu.carroll.initMusic.service.SongService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
public class SearchServiceTests {

    @Autowired
    private SongService songService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private SongRepository songRepository;

    @BeforeEach
    public void setUp() {
        playlistRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void verifySearchForSongs(){
        final Set<Song> songs = songService.searchForSongs("Zach Bryan");
        assertTrue("There should be at least one song found for query 'Zach Bryan'", !songs.isEmpty());
    }

    @Test
    @Transactional
    public void verifyAddSongToPlaylist(){
        final User user = new User("username","password","user","name","us@gmail.com","q1","q2","a1","a2");
        final Playlist p = new Playlist(user,"Playlist");
        userRepository.save(user);
        playlistRepository.save(p);


        final Object[] songs = songService.searchForSongs("Zach Bryan").toArray();
        assertTrue("Song should've been added to the playlist", songService.addSongToPlaylist(p.getPlaylistID(),(Song) songs[0]));
        assertFalse("Song should not have been added to the playlist because it is already in the playlist", songService.addSongToPlaylist(p.getPlaylistID(),(Song) songs[0]));
        assertFalse("Song should not have been added to the playlist because the playlist doesn't exist", songService.addSongToPlaylist(123123L,(Song) songs[0]));
    }

    @Test
    @Transactional
    public void verifyGetUser(){
        final User user = new User("username","password","user","name","usa@gmail.com","q1","q2","a1","a2");
        userRepository.save(user);

        User foundUser = songService.getUser(user.getUsername());
        assertTrue("User should have been found", foundUser.equals(user));
        assertTrue("User should not have been found because there is no user with the username given", songService.getUser("fakename") == null);
        assertTrue("User should not be found because a empty string was passed", songService.getUser("") == null);

    }
}
