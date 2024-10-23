package edu.carroll.initMusic;

import java.util.List;

import edu.carroll.initMusic.jpa.model.Playlist;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.jpa.repo.PlaylistRepository;
import edu.carroll.initMusic.jpa.repo.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for optionally pre-populating the database with
 * initial user and playlist data upon application startup.
 * In a real-world scenario, this would likely be handled differently,
 * such as through migration tools.
 */
@Component
public class DbInit {

    /**
     * BCryptPasswordEncoder used for hashing user passwords.
     */
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Default username used for pre-populating the database.
     */
    private static final String defaultUsername = "cs341user";

    /**
     * Default password used for pre-populating the database.
     */
    private static final String defaultPass = "supersecret";

    /**
     * User repository to perform CRUD operations on the User entity.
     */
    private final UserRepository loginRepo;

    /**
     * Playlist repository to perform CRUD operations on the Playlist entity.
     */
    private final PlaylistRepository playlistRepo;

    /**
     * Constructor for DbInit class.
     *
     * @param loginRepo       the UserRepository for interacting with User data
     * @param playlistRepo    the PlaylistRepository for interacting with Playlist data
     * @param passwordEncoder the BCryptPasswordEncoder for password hashing
     */
    public DbInit(UserRepository loginRepo, PlaylistRepository playlistRepo, BCryptPasswordEncoder passwordEncoder) {
        this.loginRepo = loginRepo;
        this.passwordEncoder = passwordEncoder;
        this.playlistRepo = playlistRepo;
    }

    /**
     * This method is called after the application starts and checks whether the
     * default user exists. If not, it creates a new user with associated playlists.
     */
    @PostConstruct
    public void loadData() {
        // Check if the default user already exists in the database
        final List<User> defaultUsers = loginRepo.findByUsernameIgnoreCase(defaultUsername);

        // If the default user doesn't exist, create the user and associated playlists
        if (defaultUsers.isEmpty()) {
            User defaultUser = new User(defaultUsername, passwordEncoder.encode(defaultPass),
                    "cs341", "user", "cs341User@gmail.com",
                    "null", "null", "null", "null");
            loginRepo.save(defaultUser);

            // Create and save the first playlist
            Playlist p1 = new Playlist(defaultUser, "Playlist 1");
            playlistRepo.save(p1);
            defaultUser.addPlaylist(p1);

            // Create and save the second playlist
            Playlist p2 = new Playlist(defaultUser, "Playlist 2");
            playlistRepo.save(p2);
            defaultUser.addPlaylist(p2);

            // Save the user with the playlists added
            loginRepo.save(defaultUser);
        }
    }
}
