package edu.carroll.initMusic.jpa.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

/**
 * <p>
 * This class is used to test the User class
 * </p>
 */
@SpringBootTest
public class UserTests {
    /**
     * Fake username
     */
    private static final String username = "username";

    /**
     * Fake password
     */
    private static final String password = "password";

    /**
     * Fake first name
     */
    private static final String firstName = "John";

    /**
     * Fake last name
     */
    private static final String lastName = "Doe";

    /**
     * Fake email
     */
    private static final String email = "john.doe@example.com";

    /**
     * fakeUser used for testing
     */
    private final User fakeUser = new User(username, password, firstName, lastName, email,
            "What is your favorite color?", "What is your pet's name?",
            "Blue", "Fluffy");

    /**
     * Testing user creation and getters
     */
    @Test
    public void verifyCreationOfUserAndGetters() {
        final String setUsername = fakeUser.getUsername();
        final String setPassword = fakeUser.getHashedPassword();
        final String setFirstName = fakeUser.getFirstName();
        final String setLastName = fakeUser.getLastName();
        final String setEmail = fakeUser.getEmail();
        assertTrue("Set username should match username from getter", setUsername.equals(username));
        assertTrue("Set password should match password from getter", setPassword.equals(password));
        assertTrue("Set firstName should match first name from getter", setFirstName.equals(firstName));
        assertTrue("Set lastName should match last name from getter", setLastName.equals(lastName));
        assertTrue("Set email should match email from getter", setEmail.equals(email));
    }

    /**
     * Testing functions related to playlists
     */
    @Test
    public void verifyPlaylistFunctions() {
        final Playlist playlist = new Playlist();
        playlist.setPlaylistName("playlist");
        fakeUser.addPlaylist(playlist);
        //Testing getPlaylist and getPlaylists
        assertTrue("getPlaylists should be one playlist linked to user", fakeUser.getPlaylists().size() == 1);
        assertTrue("getPlaylist should return playlist", fakeUser.getPlaylist("playlist").equals(playlist));

        //Testing playlist removal
        fakeUser.removePlaylist(playlist);
        assertTrue("Should be no playlists linked to user", fakeUser.getPlaylists().isEmpty());
    }

    /**
     * Testing equals, hashCode, and two setters.
     */
    @Test
    public void verifyMiscFunctions() {
        //Testing equals
        final User fakeUserTwo = new User(username, password, firstName, lastName, email,
                "What is your favorite color?", "What is your pet's name?",
                "Blue", "Fluffy");
        assertTrue("Fake users should be equal", fakeUser.equals(fakeUserTwo));
        fakeUserTwo.setUsername("random");

        final LocalDateTime now = LocalDateTime.now();
        fakeUserTwo.setAccountCreationDate(now);

        //Test equals function and setAccountCreationDate work
        assertFalse("Fake users should not be equal", fakeUser.equals(fakeUserTwo));

        //Testing hashCode
        final int userHash = fakeUser.hashCode();
        final int userTwoHash = fakeUserTwo.hashCode();
        assertFalse("Fake users hash should not be equal", userHash == userTwoHash);
    }
}
