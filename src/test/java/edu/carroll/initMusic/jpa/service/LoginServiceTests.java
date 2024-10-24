package edu.carroll.initMusic.jpa.service;

import edu.carroll.initMusic.service.LoginService;
import edu.carroll.initMusic.jpa.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import edu.carroll.initMusic.jpa.model.User;
import static org.junit.jupiter.api.Assertions.*;



/**
 * Unit tests for the LoginService class.
 * This class tests the user validation and password hashing functionalities.
 */
@SpringBootTest
public class LoginServiceTests {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Sets up a test user in the repository before each test.
     * This method is executed before each test case to ensure a clean state.
     */
    @BeforeEach
    public void setUp() {
        // Create a test user and save it in the repository
        userRepository.deleteAll();
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setFirstName("test");
        testUser.setLastName("user");
        testUser.setEmail("testuser@example.com");
        String hashedPassword = passwordEncoder.encode("testpassword");
        testUser.setHashedPassword(hashedPassword);
        userRepository.save(testUser);
    }

    /**
     * Tests successful user validation with correct credentials.
     * Asserts that the user is validated successfully.
     */
    @Test
    public void testValidateUser_Success() {
        // Validate with correct username and password
        boolean isValid = loginService.validateUser("testuser", "testpassword");
        assertTrue(isValid, "User should be validated successfully");
    }

    /**
     * Tests user validation failure with an incorrect password.
     * Asserts that the validation fails when the password is incorrect.
     */
    @Test
    public void testValidateUser_Failure_InvalidPassword() {
        // Validate with correct username but incorrect password
        boolean isValid = loginService.validateUser("testuser", "wrongpassword");
        assertFalse(isValid, "User should not be validated with incorrect password");
    }

    /**
     * Tests user validation failure with a non-existent username.
     * Asserts that the validation fails when the user does not exist.
     */
    @Test
    public void testValidateUser_Failure_UserNotFound() {
        // Validate with non-existent username
        boolean isValid = loginService.validateUser("nonexistentuser", "testpassword");
        assertFalse(isValid, "User should not be validated if user doesn't exist");
    }

    /**
     * Tests the password hashing functionality.
     * Asserts that the hashed password does not match the raw password
     * and that the hashed password matches when re-encoded.
     */
    @Test
    public void testHashPassword() {
        // Hash a password and verify it's not equal to the raw password
        String rawPassword = "newpassword";
        String hashedPassword = loginService.hashPassword(rawPassword);
        assertNotEquals(rawPassword, hashedPassword, "Hashed password should not match the raw password");

        // Verify the hashed password matches when encoded again
        assertTrue(passwordEncoder.matches(rawPassword, hashedPassword), "Hashed password should match when re-encoded");
    }
}
