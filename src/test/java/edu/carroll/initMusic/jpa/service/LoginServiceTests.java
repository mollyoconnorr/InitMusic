package edu.carroll.initMusic.jpa.service;

import edu.carroll.initMusic.service.LoginService; // Import your LoginService
import edu.carroll.initMusic.jpa.repo.UserRepository; // Import your UserRepository
import org.junit.jupiter.api.BeforeEach; // For the @BeforeEach annotation
import org.junit.jupiter.api.Test; // For the @Test annotation
import org.springframework.beans.factory.annotation.Autowired; // For @Autowired annotation
import org.springframework.boot.test.context.SpringBootTest; // For @SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // For BCryptPasswordEncoder
import edu.carroll.initMusic.jpa.model.User;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class LoginServiceTests {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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

    @Test
    public void testValidateUser_Success() {
        // Validate with correct username and password
        boolean isValid = loginService.validateUser("testuser", "testpassword");
        assertTrue(isValid, "User should be validated successfully");
    }

    @Test
    public void testValidateUser_Failure_InvalidPassword() {
        // Validate with correct username but incorrect password
        boolean isValid = loginService.validateUser("testuser", "wrongpassword");
        assertFalse(isValid, "User should not be validated with incorrect password");
    }

    @Test
    public void testValidateUser_Failure_UserNotFound() {
        // Validate with non-existent username
        boolean isValid = loginService.validateUser("nonexistentuser", "testpassword");
        assertFalse(isValid, "User should not be validated if user doesn't exist");
    }

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
