package edu.carroll.initMusic.jpa.service;

import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.jpa.repo.UserRepository;
import edu.carroll.initMusic.service.UserService;
import edu.carroll.initMusic.service.UserServiceImpl;
import edu.carroll.initMusic.web.form.RegistrationForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTests {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Optional with @MockBean; kept for clarity
    }

    /** Tests that when a unique username is given, userService should return true*/
    @Test
    public void checkUniqueUsernameTrue() {
        when(userRepository.findByUsernameIgnoreCase("newUser")).thenReturn(Collections.emptyList());
        assertTrue(userService.uniqueUserName("newUser"), "Username should be available");
    }

    /**Tests that when a in use username is given, userService should return false*/
    @Test
    public void checkUniqueUsernameFalse() {
        when(userRepository.findByUsernameIgnoreCase("existingUser")).thenReturn(List.of(new User()));
        assertFalse(userService.uniqueUserName("existingUser"), "Username should already exist");
    }




    @Test
    public void checkUniqueEmailTrue() {
        when(userRepository.findByEmailIgnoreCase("new@example.com")).thenReturn(Collections.emptyList());
        assertTrue(userService.uniqueEmail("new@example.com"), "Email should be available");
    }

    @Test
    public void checkUniqueEmailFalse() {
        when(userRepository.findByEmailIgnoreCase("existing@example.com")).thenReturn(List.of(new User()));
        assertFalse(userService.uniqueEmail("existing@example.com"), "Email should already exist");
    }

    @Test
    public void testSaveUser() {
        RegistrationForm form = new RegistrationForm();
        form.setUsername("newUser");
        form.setEmail("new@example.com");
        form.setPassword("password");
        form.setFirstName("First");
        form.setLastName("Last");

        when(passwordEncoder.encode("password")).thenReturn("hashedPassword");

        // Mock the user to be returned from the repository
        User mockUser = new User();
        mockUser.setUsername("newUser");
        mockUser.setHashedPassword("hashedPassword");
        mockUser.setEmail("new@example.com");
        mockUser.setFirstName("First");
        mockUser.setLastName("Last");

        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User savedUser = userService.saveUser(form);
        assertTrue(savedUser.getUsername().equals("newUser"), "User should have been saved with the correct username");
        assertTrue(savedUser.getHashedPassword().equals("hashedPassword"), "Password should be hashed");
    }

    @Test
    public void testUpdatePassword() {
        User user = new User();
        user.setHashedPassword("oldHashedPassword");

        when(passwordEncoder.encode("newPassword")).thenReturn("newHashedPassword");

        userService.updatePassword(user, "newPassword");
        assertTrue(user.getHashedPassword().equals("newHashedPassword"), "Password should be updated with new hash");
    }
}