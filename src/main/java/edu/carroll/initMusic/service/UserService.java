package edu.carroll.initMusic.service;

import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.jpa.repo.UserRepository;
import edu.carroll.initMusic.web.form.RegistrationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service class for handling user-related operations.
 * <p>
 * This class provides methods for saving user information and managing user data.
 * </p>
 */
@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Constructs a UserService with the specified UserRepository and BCryptPasswordEncoder.
     *
     * @param userRepository the repository used for user data access.
     * @param passwordEncoder the password encoder used for hashing user passwords.
     */
    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Saves a new user based on the provided registration form data.
     *
     * @param registrationForm the form containing user registration details.
     */
    public void saveUser(RegistrationForm registrationForm) {
        User user = new User();
        user.setUsername(registrationForm.getUsername());
        user.setEmail(registrationForm.getEmail());
        user.setHashedPassword(passwordEncoder.encode(registrationForm.getPassword())); // Hash the password
        user.setFirstName(registrationForm.getFirstName());
        user.setLastName(registrationForm.getLastName());
        user.setCountry(registrationForm.getCountry());

        // Log user information
        log.info("User saved! Username: {}, Email: {}", user.getUsername(), user.getEmail());
        userRepository.save(user); // Save the user to the database
    }
}
