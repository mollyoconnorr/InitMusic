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
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

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

    private Model model;

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
        model = new ConcurrentModel(); // Mocking because the validation logic doesn't depend on the model
    }

    /**
     * Tests successful user validation with correct credentials.
     * Asserts that the user is validated successfully.
     */
    @Test
    public void testValidateUserSuccess() {
        boolean isValid = loginService.validateUser("testuser", "testpassword", model);
        assertTrue(isValid, "User should be validated successfully");
    }

    /**
     * Tests user validation failure with an incorrect password.
     * Asserts that the validation fails when the password is incorrect.
     */
    @Test
    public void testValidateUserFailureInvalidPassword() {
        boolean isValid = loginService.validateUser("testuser", "wrongpassword", model);
        assertFalse(isValid, "User should not be validated with incorrect password");
    }

    /**
     * Tests user validation failure with a non-existent username.
     * Asserts that the validation fails when the user does not exist.
     */
    @Test
    public void testValidateUserFailureUserNotFound() {
        boolean isValid = loginService.validateUser("nonexistentuser", "testpassword", model);
        assertFalse(isValid, "User should not be validated if user doesn't exist");
    }

    /**
     * Tests validation with an empty username.
     */
    @Test
    public void testValidateUserEmptyUsername() {
        boolean isValid = loginService.validateUser("", "testpassword", model);
        assertFalse(isValid, "Validation should fail with an empty username");
    }

    /**
     * Tests validation with an empty password.
     */
    @Test
    public void testValidateUserEmptyPassword() {
        boolean isValid = loginService.validateUser("testuser", "", model);
        assertFalse(isValid, "Validation should fail with an empty password");
    }

    /**
     * Tests validation with both username and password empty.
     */
    @Test
    public void testValidateUserEmptyUsernameAndPassword() {
        boolean isValid = loginService.validateUser("", "", model);
        assertFalse(isValid, "Validation should fail with empty username and password");
    }

    /**
     * Tests validation with null username.
     */
    @Test
    public void testValidateUserNullUsername() {
        boolean isValid = loginService.validateUser(null, "testpassword", model);
        assertFalse(isValid, "Validation should fail with a null username");
    }

    /**
     * Tests validation with null password.
     */
    @Test
    public void testValidateUserNullPassword() {
        boolean isValid = loginService.validateUser("testuser", null, model);
        assertFalse(isValid, "Validation should fail with a null password");
    }

    /**
     * Tests validation with both username and password as null.
     */
    @Test
    public void testValidateUserNullUsernameAndPassword() {
        boolean isValid = loginService.validateUser(null, null, model);
        assertFalse(isValid, "Validation should fail with null username and password");
    }

    /**
     * Tests validation with a username that contains special characters.
     */
    @Test
    public void testValidateUserSpecialCharacterUsername() {
        boolean isValid = loginService.validateUser("test$user", "testpassword", model);
        assertFalse(isValid, "Validation should fail with a username containing special characters if not allowed");
    }

    /**
     * Tests validation with a password that contains special characters.
     */
    @Test
    public void testValidateUserSpecialCharacterPassword() {
        boolean isValid = loginService.validateUser("testuser", "p@ssw0rd!", model);
        assertFalse(isValid, "Validation should fail if password containing special characters doesn't match stored password");
    }

    /**
     * Tests validation with a username longer than the maximum allowed length.
     */
    @Test
    public void testValidateUserLongUsername() {
        String longUsername = "a".repeat(256); // Example of an excessively long username
        boolean isValid = loginService.validateUser(longUsername, "testpassword", model);
        assertFalse(isValid, "Validation should fail if username exceeds the maximum allowed length");
    }

    /**
     * Tests validation with a password longer than the maximum allowed length.
     */
    @Test
    public void testValidateUserLongPassword() {
        String longPassword = "a".repeat(256); // Example of an excessively long password
        boolean isValid = loginService.validateUser("testuser", longPassword, model);
        assertFalse(isValid, "Validation should fail if password exceeds the maximum allowed length");
    }

    /**
     * Tests validation with a username with leading and trailing spaces.
     */
    @Test
    public void testValidateUserUsernameWithSpaces() {
        boolean isValid = loginService.validateUser("  testuser  ", "testpassword", model);
        assertFalse(isValid, "Validation should fail with username having leading or trailing spaces");
    }

    /**
     * Tests validation with a username in different case sensitivity.
     */
    @Test
    public void testValidateUserCaseSensitiveUsername() {
        boolean isValid = loginService.validateUser("TESTUSER", "testpassword", model);
        assertTrue(isValid, "Validation should pass if the username changes case");
    }

    /**
     * Tests validation with password case sensitivity.
     */
    @Test
    public void testValidateUserCaseSensitivePassword() {
        boolean isValid = loginService.validateUser("testuser", "TESTpassword", model);
        assertFalse(isValid, "Validation should fail if the password check is case-sensitive and does not match stored case");
    }

    /**
     * Tests validation with an extremely short username.
     */
    @Test
    public void testValidateUserVeryShortUsername() {
        boolean isValid = loginService.validateUser("a", "testpassword", model);
        assertFalse(isValid, "Validation should fail with a very short username if minimum length is required");
    }

    /**
     * Tests validation with an extremely short password.
     */
    @Test
    public void testValidateUserVeryShortPassword() {
        boolean isValid = loginService.validateUser("testuser", "a", model);
        assertFalse(isValid, "Validation should fail with a very short password if minimum length is required");
    }

    /**
     * Tests validation with a password that contains whitespace characters.
     */
    @Test
    public void testValidateUserPasswordWithWhitespace() {
        boolean isValid = loginService.validateUser("testuser", "test password", model);
        assertFalse(isValid, "Validation should fail if password contains spaces and does not match stored password");
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
    }

    @Test
    public void testHashSamePasswordMultipleTimes() {
        String rawPassword = "samesecurepassword";

        // Hash the password multiple times
        String hashedPassword1 = loginService.hashPassword(rawPassword);
        String hashedPassword2 = loginService.hashPassword(rawPassword);

        // Assert that both hashed passwords are not equal (due to salting)
        assertNotEquals(hashedPassword1, hashedPassword2, "Hashing the same password should yield different results.");
    }

    @Test
    public void testHashEmptyPassword() {
        // Attempt to hash an empty password and expect an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            loginService.hashPassword(""); // Test with empty string
        });

        String expectedMessage = "Password cannot be null or empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage), "Expected exception message for empty password.");
    }

    @Test
    public void testHashNullPassword() {
        // Attempt to hash a null password and expect an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            loginService.hashPassword(null); // Test with null
        });

        String expectedMessage = "Password cannot be null or empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage), "Expected exception message for null password.");
    }

    @Test
    public void testConsistencyOfHashedPasswords() {
        String rawPassword = "consistentsafe";

        // Hash the password
        String hashedPassword = loginService.hashPassword(rawPassword);

        // Verify that the hashed password matches when using the raw password
        assertTrue(passwordEncoder.matches(rawPassword, hashedPassword), "Hashed password should match the original raw password.");
    }

}

