package edu.carroll.initMusic.jpa.service;

import edu.carroll.initMusic.service.CustomUserDetailsService;
import edu.carroll.initMusic.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

/**
 *Tests the CustomUserDetailsService class
 *
 * @author Nick Clouse
 *
 * @since November 6, 2024
 */
@SpringBootTest
public class UserDetailsServiceTests {

    /** Class we are tested */
    @Autowired
    private CustomUserDetailsService userDetailsService;

    /** Used to add user objects to database when needed */
    @Autowired
    private UserServiceImpl userService;

    @Test
    public void loadUserByUsernameFoundUser() {
        final String username = "username";
        userService.saveUser(username,"password","email@email.com","first","last");
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        assertEquals(username, userDetails.getUsername(), "User should be found since user is in database!");
    }

    @Test
    public void loadUserByUsernameFoundUserLeadingTrailingSpaces() {
        final String username = "   usernameTwo   ";
        userService.saveUser(username,"password","emailTwo@email.com","first","last");
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        assertEquals(username.strip(), userDetails.getUsername(), "User should be found since user is in database!");
    }

    @Test
    public void loadUserByUsernameNotFoundUser() {
        final String username = "unknownUser";

        assertThrowsExactly(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username), "Exception should be thrown since no user is in database!");
    }

    @Test
    public void loadUserByUsernameEmptyUsername() {
        final String username = "";

        assertThrowsExactly(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username), "Exception should be thrown since username is empty when loading by username!");
    }

    @Test
    public void loadUserByUsernameNullUsername() {
        assertThrowsExactly(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(null), "Exception should be thrown since username is null when loading by username!");
    }

    @Test
    public void loadUserByUsernameNotFoundUserSpaceInUserName() {
        final String username = "user name";

        assertThrowsExactly(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username), "Exception should be thrown since username is not in database!");
    }
}
