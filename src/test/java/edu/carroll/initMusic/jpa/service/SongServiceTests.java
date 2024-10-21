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

    /**
     * This test verifies the AddSongToPlaylist function
     */
    @Test
    @Transactional
    public void verifyAddSongToPlaylist(){
        playlistRepository.deleteAll();
        userRepository.deleteAll();
        final User user = new User("username1","password","user","name","us@gmail.com","q1","q2","a1","a2");
        final Playlist p = new Playlist(user,"Playlist");

        userRepository.save(user);
        playlistRepository.save(p);

        final Object[] songs = songService.searchForSongs("Zach Bryan").toArray();

        //Checking that a song should've been added to the playlist
        assertTrue("Song should've been added to the playlist", songService.addSongToPlaylist(p.getPlaylistID(),(Song) songs[0]));

        //Checking that a song won't be added twice
        assertFalse("Song should not have been added to the playlist because it is already in the playlist", songService.addSongToPlaylist(p.getPlaylistID(),(Song) songs[0]));

        //Checking that song won't be added to an unknown playlist
        assertFalse("Song should not have been added to the playlist because the playlist doesn't exist", songService.addSongToPlaylist(123123L,(Song) songs[0]));

        //Create a song object without saving it to the repository
        final Song unsavedSong = new Song(123L,"name",100,"artist",1234L,"Album",23323L);

        //Call the method
        final boolean result = songService.addSongToPlaylist(p.getPlaylistID(), unsavedSong);

        //Verify the song was added and saved
        assertTrue("Song should've been added to the playlist",result);

        //Verify the song is now saved in the repository
        assertTrue("Song should be saved in repository",songRepository.existsById(unsavedSong.getSongID()));

        //Verify the playlist contains the new song
        final Playlist updatedPlaylist = playlistRepository.findById(p.getPlaylistID()).orElse(null);
        assertNotNull("Updated playlist should not be null",updatedPlaylist);
        assert updatedPlaylist != null;
        assertTrue("Updated playlist should contain a song",updatedPlaylist.containsSong(unsavedSong));
    }

    /**
     * This test verifies the GetUser function.
     */
    @Test
    @Transactional
    public void verifyGetUser(){
        playlistRepository.deleteAll();
        userRepository.deleteAll();
        final User user = new User("username","password","user","name","usa@gmail.com","q1","q2","a1","a2");
        userRepository.save(user);

        //Test finding user, and not finding, and searching with empty string
        User foundUser = userService.getUser(user.getUsername());
        assertEquals("User should have been found", foundUser,user);
        assertNull("User should not have been found because there is no user with the username given", userService.getUser("fakename"));
        assertNull("User should not be found because a empty string was passed", userService.getUser(""));

        //Add a user to the repository with mixed case username
        final User testUser = new User("TestUser", "password", "first", "last", "email@test.com", "q1", "q2", "a1", "a2");
        userRepository.save(testUser);

        //Call the method with different case
        final User result = userService.getUser("testuser");

        //Verify the correct user is returned regardless of case
        assertNotNull("A user should be found, regardless of String case",result);
        assertEquals("Fake username should match username of fake user","TestUser", result.getUsername());
    }
}
