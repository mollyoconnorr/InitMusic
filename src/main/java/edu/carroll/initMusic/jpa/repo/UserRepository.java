package edu.carroll.initMusic.jpa.repo;

import java.util.List;

import edu.carroll.initMusic.jpa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * <p>
 * Repository used for performing CRUD actions on the User Entity
 * </p>
 *
 * @author Nick Clouse
 *
 * @since September 11, 2024
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Searches for a user by username
     * @param username Username to search for
     * @return Username, JPA throws an exception if we attempt to return a
     * single object that doesn't exist, so return a list even though we only expect
     * either an empty list or a single element.
     */
    List<User> findByUsernameIgnoreCase(String username);

    /**
     * Searches for a user by email (case-insensitive)
     * @param email Email to search for
     * @return Optional<User>, which is empty if no user is found
     */
    List<User> findByEmailIgnoreCase(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.playlists WHERE u.userID = :id")
    User findByIdWithPlaylists(@Param("id") Long id);
}
