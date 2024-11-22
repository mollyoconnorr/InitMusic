package edu.carroll.initMusic.service;

import edu.carroll.initMusic.config.CustomUserDetails;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.jpa.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Creates our own implementation of UserDetailsService, so spring security knows
 * how to get/load a user's information.
 *
 * @author Nick Clouse
 * @see UserDetailsService
 * @since October 30, 2024
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    /** Logger for logging */
    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    /** User repository for looking up user */
    private final UserRepository userRepository;

    /**
     * Injects dependencies
     *
     * @param userRepository User repository to inject
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads user using given username
     *
     * @param username Username to search by
     * @return UserDetails object with user information
     * @throws UsernameNotFoundException Thrown if username is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.isEmpty()) {
            throw new UsernameNotFoundException("Username cannot be empty");
        }
        username = username.strip();
        final List<User> user = userRepository.findByUsernameIgnoreCase(username);
        if (user.size() != 1) {
            log.info("loadUserByUsername: username={} not found", username);
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(user.getFirst());
    }
}
