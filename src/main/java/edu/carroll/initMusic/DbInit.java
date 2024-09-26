package edu.carroll.initMusic;

import java.util.List;

import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.jpa.repo.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

// This class optionally pre-populates the database with login data.  In
// a real application, this would be done with a completely different
// method.
@Component
public class DbInit {
    /**
     * Bcrypt password encoder, used for hashing passwords.
     */
    private final BCryptPasswordEncoder passwordEncoder;
    // XXX - This is wrong on so many levels....
    private static final String defaultUsername = "cs341user";
    private static final String defaultPass = "supersecret";

    private final UserRepository loginRepo;

    public DbInit(UserRepository loginRepo, BCryptPasswordEncoder passwordEncoder) {
        this.loginRepo = loginRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // invoked during application startup
    @PostConstruct
    public void loadData() {
        // If the user doesn't exist in the database, populate it
        final List<User> defaultUsers = loginRepo.findByUsernameIgnoreCase(defaultUsername);
        if (defaultUsers.isEmpty()) {
            User defaultUser = new User(defaultUsername,passwordEncoder.encode(defaultPass), "cs341","user","cs341User@gmail.com","null", "null", "null", "null");

            loginRepo.save(defaultUser);
        }
    }
}
