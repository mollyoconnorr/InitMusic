package edu.carroll.initMusic.service;

import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.jpa.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Login Service Implementation, used for handling logging in and account creation
 */
@Service
public class LoginServiceImpl implements LoginService {
    /**
     * Logger object used for logging
     */
    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

    /**
     * User repository
     */
    private final UserRepository userRepo;

    /**
     * Bcrypt password encoder, used for hashing passwords.
     */
    private final BCryptPasswordEncoder passwordEncoder;
    /**
     * Constructor, makes a new loginServiceImpl object
     * @param userRepo UserRepository
     */
    public LoginServiceImpl(UserRepository userRepo, BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Given a loginForm, determine if the information provided is valid, and the user exists in the system.
     *
     * @param username Users username
     * @param password Users password
     * @return true if data exists and matches what's on record, false otherwise
     */
    @Override
    public boolean validateUser(String username, String password) {
        log.debug("validateUser: user '{}' attempted login", username);
        // Always do the lookup in a case-insensitive manner (lower-casing the data).
        List<User> users = userRepo.findByUsernameIgnoreCase(username);

        // We expect 0 or 1, so if we get more than 1, bail out as this is an error we don't deal with properly.
        if (users.size() != 1) {
            log.debug("validateUser: found {} users", users.size());
            return false;
        }
        User u = users.getFirst();

        log.info("username:  {} password: {}", u.getUsername(), u.getHashedPassword());
        //If password is incorrect
        if(!passwordEncoder.matches(password, u.getHashedPassword())) {
            log.debug("validateUser: password !match");
            return false;
        }

        // User exists, and the provided password matches the hashed password in the database.
        log.info("validateUser: successful login for {}", username);
        return true;
    }

    /**
     * Hashes password using Bcrypt algorithm
     * @param password Password to hash
     * @return Hashed password
     */
    @Override
    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }
}