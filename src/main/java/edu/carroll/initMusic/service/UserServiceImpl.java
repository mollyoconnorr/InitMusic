package edu.carroll.initMusic.service;

import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.jpa.repo.UserRepository;
import edu.carroll.initMusic.web.form.RegistrationForm;
import edu.carroll.initMusic.web.form.SecurityQuestionsForm;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Service class for handling user-related operations.
 * <p>
 * This class provides methods for saving user information, checking unique usernames/emails,
 * updating security questions, and managing passwords.
 * </p>
 */
@Service
public class UserServiceImpl implements UserService {

    /** Logger for logging */
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    /** User repository for interacting with the user database. */
    private final UserRepository userRepository;

    /** BCrypt password encoder used for hashing passwords.*/
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Constructor to initialize the UserService with the required dependencies.
     *
     * @param userRepository   the repository for interacting with the user data
     * @param passwordEncoder  the encoder used to hash passwords
     */
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Checks whether the provided username is unique (i.e., not already present in the database).
     *
     * @param username the username to check for uniqueness
     * @return true if the username is unique, false otherwise
     */
    public boolean uniqueUserName(String username) {
        log.info("Checking if username '{}' is unique", username);
        List<User> usersByName = userRepository.findByUsernameIgnoreCase(username);

        if (!usersByName.isEmpty()) {
            log.info("Username '{}' already exists", username);
            return false;
        } else {
            log.info("Username '{}' is available", username);
            return true;
        }
    }

    /**
     * Checks whether the provided email is unique (i.e., not already present in the database).
     *
     * @param email the email to check for uniqueness
     * @return true if the email is unique, false otherwise
     */
    public boolean uniqueEmail(String email) {
        log.info("Checking if email '{}' is unique", email);
        List<User> usersByEmail = userRepository.findByEmailIgnoreCase(email);

        if (!usersByEmail.isEmpty()) {
            log.info("Email '{}' already exists", email);
            return false;
        } else {
            log.info("Email '{}' is available", email);
            return true;
        }
    }

    /**
     * Saves a new user based on the provided registration form data.
     *
     * @param registrationForm the form containing user registration details
     * @return the saved {@link User} object
     */
    public User saveUser(RegistrationForm registrationForm) {
        log.info("Saving new user with username '{}'", registrationForm.getUsername());
        final User newUser = new User();  // Create a new User object inside the method
        newUser.setUsername(registrationForm.getUsername());
        newUser.setEmail(registrationForm.getEmail());
        // Validate that password is not null
        final String hashedPassword = passwordEncoder.encode(registrationForm.getPassword());
        newUser.setHashedPassword(hashedPassword);
        log.info("Password for user '{}' has been hashed", newUser.getUsername());

        newUser.setFirstName(registrationForm.getFirstName());
        newUser.setLastName(registrationForm.getLastName());
        newUser.setQuestion1("null");
        newUser.setAnswer1("null");
        newUser.setQuestion2("null");
        newUser.setAnswer2("null");

        log.info("User '{}' saved with email '{}'", newUser.getUsername(), newUser.getEmail());
        return userRepository.save(newUser);
    }

    /**
     * Updates the security questions and answers for the specified user.
     *
     * @param user         the user whose security questions are being updated
     * @param questionForm the form containing the new security questions and answers
     */
    public void updateUser(User user, SecurityQuestionsForm questionForm) {
        log.info("Updating security questions for user id#{}", user.getuserID());
        user.setQuestion1(questionForm.getQuestion1());
        user.setAnswer1(questionForm.getAnswer1());
        user.setQuestion2(questionForm.getQuestion2());
        user.setAnswer2(questionForm.getAnswer2());
        userRepository.save(user); // Save the user with updated security questions to the database
        log.info("Security questions updated for user id#{}", user.getuserID());
    }

    /**
     * Finds a user by email address, ignoring case sensitivity.
     *
     * @param email the email address to search for
     * @return the user if found, otherwise null
     */
    public User findByEmail(String email) {
        log.info("Finding user by email '{}'", email);
        final List<User> users = userRepository.findByEmailIgnoreCase(email);
        final User foundUser = users.isEmpty() ? null : users.getFirst();  // Return the first user or null if none found

        if (foundUser != null) {
            log.info("User found with email '{}'", email);
        } else {
            log.info("No user found with email '{}'", email);
        }
        return foundUser;
    }

    /**
     * Updates the password for the specified user.
     *
     * @param user        the user whose password is being updated
     * @param newPassword the new password to be hashed and saved
     */
    public void updatePassword(User user, String newPassword) {
        log.info("Updating password for user id#{}", user.getuserID());
        user.setHashedPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user); // Save the user with updated password to the database
        log.info("Password updated for user id#{}", user.getuserID());
    }

    /**
     * Gets a user object by user id, also eagerly loads the
     * playlists the user has so operations can be performed on them
     * if needed.
     * @param userId ID to search by
     * @return The user object found, if any
     */
    public User findByIdWithPlaylists(Long userId) {
        return userRepository.findByIdWithPlaylists(userId);
    }

    /**
     * Gets user object from inputted username. All usernames are unique, so
     * there should only be 1 username found
     * @param username Username to use for search
     * @return User object found, null if nothing found, or too many were found.
     */
    public User getUser(String username){
        final List<User> user = userRepository.findByUsernameIgnoreCase(username);
        if(user.size() != 1){
            return null;
        }
        return user.getFirst();
    }
}
