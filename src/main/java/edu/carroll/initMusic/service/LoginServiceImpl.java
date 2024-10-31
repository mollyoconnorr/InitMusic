package edu.carroll.initMusic.service;

import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.jpa.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;


import java.util.List;

/**
 * Login Service Implementation, used for handling user login and account creation.
 * <p>
 * This service interacts with the {@link UserRepository} to validate users during login and
 * manages password hashing using {@link BCryptPasswordEncoder}.
 * </p>
 */
@Service
public class LoginServiceImpl implements LoginService {

    /**
     * Logger object used for logging activities within the LoginServiceImpl.
     */
    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

    /**
     * User repository for interacting with the user data in the database.
     */
    private final UserRepository userRepo;

    /**
     * BCrypt password encoder for securely hashing passwords.
     */
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Constructor to initialize the LoginServiceImpl with the required dependencies.
     *
     * @param userRepo the repository used for managing user data
     * @param passwordEncoder the password encoder used for hashing passwords
     */
    public LoginServiceImpl(UserRepository userRepo, BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Validates the provided username and password by checking the user data stored in the repository.
     *
     * @param username the username provided by the user during login
     * @param password the password provided by the user during login
     * @return true if the user exists and the password matches; false otherwise
     */
    @Override
    public boolean validateUser(String username, String password, Model model) {
        log.info("validateUser: Attempting to validate user '{}' for login", username);

        if (username == null || password == null) {
            model.addAttribute("errorMessage", "Username and password must be provided.");
            log.info("validateUser: Null username or password provided");
            return false;
        }

        // Perform a case-insensitive search for the user in the repository.
        List<User> users = userRepo.findByUsernameIgnoreCase(username);

        // Check if exactly one user is found. If zero or more than one are found, return false.
        if (users.size() != 1) {
            log.info("validateUser: Found {} users with username '{}'", users.size(), username);
            model.addAttribute("errorMessage", "No users exist with that username. Do you need to register?");
            return false;
        }

        User u = users.get(0);

        // Check if the provided password matches the hashed password stored in the database.
        if (!passwordEncoder.matches(password, u.getHashedPassword())) {
            log.info("validateUser: Password does not match for user '{}'", username);
            model.addAttribute("errorMessage", "That username and password don't match.");
            return false;
        }

        // User exists, and the password matches the stored hash.
        log.info("validateUser: Successful login for user '{}'", username);
        return true;
    }

    /**
     * Hashes the provided password using the BCrypt algorithm.
     *
     * @param password the raw password to hash
     * @return the hashed password
     */
    @Override
    public String hashPassword(String password) {
        log.info("hashPassword: Hashing password using BCrypt");

        // Check for null or empty password and throw an exception
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        String hashedPassword = passwordEncoder.encode(password);
        log.info("hashPassword: Password successfully hashed");
        return hashedPassword;
    }
}