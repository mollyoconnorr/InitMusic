package edu.carroll.initMusic.jpa.repo;

import java.util.List;

import edu.carroll.initMusic.jpa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // JPA throws an exception if we attempt to return a single object that doesn't exist, so return a list
    // even though we only expect either an empty list or a single element.
    List<User> findByUsernameIgnoreCase(String username);
}
