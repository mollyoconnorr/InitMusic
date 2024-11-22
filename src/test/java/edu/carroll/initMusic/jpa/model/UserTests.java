package edu.carroll.initMusic.jpa.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the basic methods in the user class
 */
@SpringBootTest
public class UserTests {

    /** User object to use for testing */
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("testUser", "hashedPassword123", "First", "Last", "testuser@example.com",
                "What is your favorite color?", "What is your pet's name?",
                "Blue", "Fluffy");
    }

    @Test
    public void testConstructor() {
        assertNotNull(user, "User should be created successfully.");
        assertEquals("testUser", user.getUsername(), "Username should match.");
        assertEquals("First", user.getFirstName(), "First name should match.");
        assertEquals("Last", user.getLastName(), "Last name should match.");
        assertEquals("testuser@example.com", user.getEmail(), "Email should match.");
    }

    @Test
    public void testSettersAndGetters() {
        user.setUsername("newUser");
        user.setFirstName("NewFirst");
        user.setLastName("NewLast");
        user.setEmail("newuser@example.com");

        assertEquals("newUser", user.getUsername(), "Username should be updated.");
        assertEquals("NewFirst", user.getFirstName(), "First name should be updated.");
        assertEquals("NewLast", user.getLastName(), "Last name should be updated.");
        assertEquals("newuser@example.com", user.getEmail(), "Email should be updated.");
    }

    @Test
    public void testAddAndRemovePlaylists() {
        final Playlist playlist = new Playlist("Test Playlist");

        user.addPlaylist(playlist);
        final Set<Playlist> playlists = user.getPlaylists();
        assertTrue(playlists.contains(playlist), "Playlist should be added.");

        user.removePlaylist(playlist);
        assertFalse(playlists.contains(playlist), "Playlist should be removed.");
    }

    @Test
    public void testEqualsAndHashCode() {
        final User user1 = new User("testUser", "hashedPassword123", "First", "Last", "testuser@example.com",
                "What is your favorite color?", "What is your pet's name?",
                "Blue", "Fluffy");
        final User user2 = new User("testUser", "hashedPassword123", "First", "Last", "testuser@example.com",
                "What is your favorite color?", "What is your pet's name?",
                "Blue", "Fluffy");

        assertEquals(user1, user2, "Users should be equal.");
        assertEquals(user1.hashCode(), user2.hashCode(), "Hash codes should be equal.");
    }

    @Test
    public void testToString() {
        final String expectedString = "Login @ User{id=null, username='testUser', hashedPassword='*****', " +
                "firstName='First', lastName='Last', email='testuser@example.com'}";
        assertEquals(expectedString, user.toString(), "toString should return the correct format.");
    }

    @Test
    public void testAccountCreationDate() {
        final LocalDateTime now = LocalDateTime.now();
        user.setAccountCreationDate(now);

        assertNotNull(user.getAccountCreationDate(), "Account creation date should not be null.");
        assertTrue(user.getAccountCreationDate().isAfter(now.minusSeconds(1)) &&
                        user.getAccountCreationDate().isBefore(now.plusSeconds(1)),
                "Account creation date should be close to the current time.");
    }
}
