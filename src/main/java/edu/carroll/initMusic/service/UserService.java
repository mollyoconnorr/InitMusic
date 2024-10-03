package edu.carroll.initMusic.service;

import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.jpa.repo.UserRepository;
import edu.carroll.initMusic.web.form.RegistrationForm;
import edu.carroll.initMusic.web.form.SecurityQuestionsForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Service class for handling user-related operations.
 * <p>
 * This class provides methods for saving user information and managing user data.
 * </p>
 */
@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    /**
     * User repository
     */
    private final UserRepository userRepository;

    /**
     * Bcrypt password encoder, used for hashing passwords.
     */
    private final BCryptPasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, UserRepository userRepo, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository; // This line will now work
        this.passwordEncoder = passwordEncoder;
    }

    User user = new User();

    /**
     * Checks to see if the username or email already exists in database
     *
     * @param registrationForm the form containing user registration details.
     */
    public boolean uniqueUserName(String username) {
        List<User> usersByName = userRepository.findByUsernameIgnoreCase(username);

        // If either username or email exists, return false
        if (!usersByName.isEmpty()) {
            log.info("Username already exists");
            return false;
        } else {
            log.info("User doesn't exist, registration is allowed");
            return true;
        }
    }

    /**
     * Checks to see if the username or email already exists in database
     *
     * @param registrationForm the form containing user registration details.
     */
    public boolean uniqueEmail(String email) {
        List<User> usersByEmail = userRepository.findByEmailIgnoreCase(email);

        // If either username or email exists, return false
        if ( !usersByEmail.isEmpty()) {
            log.info("Email already exists");
            return false;
        } else {
            log.info("User doesn't exist, registration is allowed");
            return true;
        }
    }

    /**
     * Saves a new user based on the provided registration form data.
     *
     * @param registrationForm the form containing user registration details.
     * @return
     */
    public User saveUser(RegistrationForm registrationForm) {
        user.setUsername(registrationForm.getUsername());
        user.setEmail(registrationForm.getEmail());
        String hashedPassword = passwordEncoder.encode(registrationForm.getPassword());
        user.setHashedPassword(hashedPassword);
        log.info("Hashed Password for user '{}': {}", user.getUsername(), hashedPassword);
        user.setFirstName(registrationForm.getFirstName());
        user.setLastName(registrationForm.getLastName());
        user.setQuestion1("null");
        user.setAnswer1("null");
        user.setQuestion2("null");
        user.setAnswer2("null");
        // Log user information
        log.info("User saved! Username: {}, Email: {}", user.getUsername(), user.getEmail());
        return userRepository.save(user);
    }

    public void updateUser(User user, SecurityQuestionsForm questionForm) {
        user.setQuestion1(questionForm.getQuestion1());
        user.setAnswer1(questionForm.getAnswer1());
        user.setQuestion2(questionForm.getQuestion2());
        user.setAnswer2(questionForm.getAnswer2());
        userRepository.save(user); // Save the user with updated security questions to the database
    }

    public User findByEmail(String email) {
        List<User> users = userRepository.findByEmailIgnoreCase(email);
        return users.isEmpty() ? null : users.getFirst(); // Return the first user or null if none found
    }

    public void updatePassword(User user, String newPassword) {
        user.setHashedPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user); // Save the user with updated password to the database
    }
}
