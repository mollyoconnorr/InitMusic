package edu.carroll.initMusic.jpa.repo;

import java.util.List;

import edu.carroll.initMusic.jpa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository used for performing CRUD actions on the user Entity
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
}
