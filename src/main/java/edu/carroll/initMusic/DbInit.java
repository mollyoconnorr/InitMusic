package edu.carroll.initMusic;

import java.util.List;

import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.jpa.repo.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

// This class optionally pre-populates the database with login data.  In
// a real application, this would be done with a completely different
// method.
@Component
public class DbInit {
    // XXX - This is wrong on so many levels....
    private static final String defaultUsername = "cs341user";
    private static final String defaultPassHash = "-1164577301";

    private final UserRepository loginRepo;

    public DbInit(UserRepository loginRepo) {
        this.loginRepo = loginRepo;
    }

    // invoked during application startup
    @PostConstruct
    public void loadData() {
        // If the user doesn't exist in the database, populate it
        final List<User> defaultUsers = loginRepo.findByUsernameIgnoreCase(defaultUsername);
        if (defaultUsers.isEmpty()) {
            User defaultUser = new User();
            defaultUser.setUsername(defaultUsername);
            defaultUser.setHashedPassword(defaultPassHash);
            defaultUser.setAccountCreationDate("10/20/1002");
            defaultUser.setCountry("America");
            defaultUser.setEmail("cs341User@gmail.com");
            defaultUser.setFirstName("cs341");
            defaultUser.setLastName("User");
            defaultUser.setUsername("cs341User");

            loginRepo.save(defaultUser);
        }
    }
}
