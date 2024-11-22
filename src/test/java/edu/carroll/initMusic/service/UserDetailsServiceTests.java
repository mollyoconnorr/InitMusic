package edu.carroll.initMusic.service;

import edu.carroll.initMusic.config.CustomUserDetails;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.service.userManagement.CustomUserDetailsService;
import edu.carroll.initMusic.service.userManagement.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the CustomUserDetailsService class
 */
@SpringBootTest
public class UserDetailsServiceTests {

    /**
     * Class we are tested
     */
    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * Used to add/edit/delete user objects in database when needed
     */
    @Autowired
    private UserService userService;

    @Test
    public void loadUserByUsernameFoundUser() {
        final String username = "username";
        final User savedUser = userService.saveUser(username, "password", "email@email.com", "first", "last");
        assertNotNull(savedUser, "User should've been saved in database when testing loadUserByUsernameFoundUser");

        final CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

        assertEquals(savedUser, userDetails.getUser(), "User object stored in CustomUserDetails should match the object returned from saveUser");
        assertEquals(savedUser.getUsername(), userDetails.getUsername(), "User should be found since user is in database!");
        assertEquals(savedUser.getHashedPassword(), userDetails.getPassword(), "Users hashed password should be stored in customUserDetails!");
        assertEquals(userDetails.getAuthorities().size(), 0, "User should have no authorities in CustomUserDetails object!");
        assertEquals(savedUser, userDetails.getUser(), "Returned user object should match the saved user object");
        assertTrue(userDetails.isAccountNonExpired(), "User's account should not be expired!");
        assertTrue(userDetails.isAccountNonLocked(), "User's account should not be locked!");
        assertTrue(userDetails.isCredentialsNonExpired(), "User's credentials should not be expired!");
        assertTrue(userDetails.isEnabled(), "User's enabled should be enabled!");
    }

    @Test
    public void loadUserByUsernameFoundUserLeadingTrailingSpaces() {
        final String username = "   usernameTwo   ";
        final User savedUser = userService.saveUser(username, "password", "emailTwo@email.com", "first", "last");
        assertNotEquals(savedUser, null, "User should've been saved in database when testing loadUserByUsernameFoundUserLeadingTrailingSpaces");

        final CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

        assertEquals(savedUser, userDetails.getUser(), "User object stored in CustomUserDetails should match the object returned from saveUser");
        assertEquals(savedUser.getUsername(), userDetails.getUsername(), "User should be found since user is in database!");
        assertEquals(savedUser.getHashedPassword(), userDetails.getPassword(), "Users hashed password should be stored in customUserDetails!");
        assertEquals(userDetails.getAuthorities().size(), 0, "User should have no authorities in CustomUserDetails object!");
        assertEquals(savedUser, userDetails.getUser(), "Returned user object should match the saved user object");
        assertTrue(userDetails.isAccountNonExpired(), "User's account should not be expired!");
        assertTrue(userDetails.isAccountNonLocked(), "User's account should not be locked!");
        assertTrue(userDetails.isCredentialsNonExpired(), "User's credentials should not be expired!");
        assertTrue(userDetails.isEnabled(), "User's enabled should be enabled!");
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

    @Test
    public void loadUserByUsernameDeletedUserNotFound() {
        final String username = "usernameThree";
        final User savedUser = userService.saveUser(username, "password", "emailThree@email.com", "first", "last");
        assertNotEquals(savedUser, null, "User should've been saved in database when testing loadUserByUsernameDeletedUserNotFound");

        final boolean deletedUser = userService.deleteByEmail(savedUser.getEmail());
        assertTrue(deletedUser, "User should be deleted since user is in database and a valid email was passed!");

        assertThrowsExactly(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username), "Exception should be thrown since username no longer exists in database!");
    }

    @Test
    public void loadUserByUsernameDeletedUserThenAddedAgainFound() {
        final String username = "usernameFour";
        //save user
        User savedUser = userService.saveUser(username, "password", "emailFour@email.com", "first", "last");
        assertNotEquals(savedUser, null, "User should've been saved in database when testing loadUserByUsernameDeletedUserThenAddedAgainFound");

        //delete user
        final boolean deletedUser = userService.deleteByEmail(savedUser.getEmail());
        assertTrue(deletedUser, "User should be deleted since user is in database and a valid email was passed!");

        //resave user
        savedUser = userService.saveUser(username, "password", "emailFour@email.com", "first", "last");
        assertNotEquals(savedUser, null, "User should've been resaved in database when testing loadUserByUsernameDeletedUserThenAddedAgainFound");

        final CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

        assertEquals(savedUser, userDetails.getUser(), "User object stored in CustomUserDetails should match the object returned from saveUser");
        assertEquals(savedUser.getUsername(), userDetails.getUsername(), "User should be found since user is in database!");
        assertEquals(savedUser.getHashedPassword(), userDetails.getPassword(), "Users hashed password should be stored in customUserDetails!");
        assertEquals(userDetails.getAuthorities().size(), 0, "User should have no authorities in CustomUserDetails object!");
        assertEquals(savedUser, userDetails.getUser(), "Returned user object should match the saved user object");
        assertTrue(userDetails.isAccountNonExpired(), "User's account should not be expired!");
        assertTrue(userDetails.isAccountNonLocked(), "User's account should not be locked!");
        assertTrue(userDetails.isCredentialsNonExpired(), "User's credentials should not be expired!");
        assertTrue(userDetails.isEnabled(), "User's enabled should be enabled!");
    }

    @Test
    public void loadUserByUsernameWhenTwoSavedUsersReturnsCorrectUser() {
        final String username = "usernameFive";
        User savedUser = userService.saveUser(username, "password", "emailFive@email.com", "first", "last");
        assertNotEquals(savedUser, null, "User should've been saved in database when testing loadUserByUsernameWhenTwoSavedUsersReturnsCorrectUser");

        final String usernameTwo = "usernameSix";
        User savedUserTwo = userService.saveUser(usernameTwo, "password", "emailSix@email.com", "first", "last");
        assertNotEquals(savedUserTwo, null, "User should've been saved in database when testing loadUserByUsernameWhenTwoSavedUsersReturnsCorrectUser");

        //Search for the first saved user, should return its info
        final CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

        assertEquals(savedUser, userDetails.getUser(), "User object stored in CustomUserDetails should match the object returned from saveUser");
        assertEquals(savedUser.getUsername(), userDetails.getUsername(), "User should be found since user is in database!");
        assertEquals(savedUser.getHashedPassword(), userDetails.getPassword(), "Users hashed password should be stored in customUserDetails!");
        assertEquals(userDetails.getAuthorities().size(), 0, "User should have no authorities in CustomUserDetails object!");
        assertEquals(savedUser, userDetails.getUser(), "Returned user object should match the saved user object");
        assertTrue(userDetails.isAccountNonExpired(), "User's account should not be expired!");
        assertTrue(userDetails.isAccountNonLocked(), "User's account should not be locked!");
        assertTrue(userDetails.isCredentialsNonExpired(), "User's credentials should not be expired!");
        assertTrue(userDetails.isEnabled(), "User's enabled should be enabled!");

        //Search for the second saved user, should return its info
        final CustomUserDetails userDetailsTwo = (CustomUserDetails) userDetailsService.loadUserByUsername(usernameTwo);

        assertEquals(savedUserTwo, userDetailsTwo.getUser(), "User object stored in CustomUserDetails should match the object returned from saveUser");
        assertEquals(savedUserTwo.getUsername(), userDetailsTwo.getUsername(), "User should be found since user is in database!");
        assertEquals(savedUserTwo.getHashedPassword(), userDetailsTwo.getPassword(), "Users hashed password should be stored in customUserDetails!");
        assertEquals(userDetailsTwo.getAuthorities().size(), 0, "User should have no authorities in CustomUserDetails object!");
        assertEquals(savedUserTwo, userDetailsTwo.getUser(), "Returned user object should match the saved user object");
        assertTrue(userDetailsTwo.isAccountNonExpired(), "User's account should not be expired!");
        assertTrue(userDetailsTwo.isAccountNonLocked(), "User's account should not be locked!");
        assertTrue(userDetailsTwo.isCredentialsNonExpired(), "User's credentials should not be expired!");
        assertTrue(userDetailsTwo.isEnabled(), "User's enabled should be enabled!");
    }
}
