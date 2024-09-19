package edu.carroll.initMusic.jpa.repo;

import edu.carroll.initMusic.jpa.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository used for performing CRUD actions on the Playlist Entity
 */
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    /**
     * Finds playlist by authors username
     * @param username Username to search with
     * @return A list of playlists found that relate to username
     */
    @Query("SELECT p FROM Playlist p WHERE p.author.username LIKE %:username%")
    List<Playlist> findByAuthorUsernameContainingIgnoreCase(@Param("username") String username);

    /**
     * Searches for playlists with given name.
     * @param name Name to search for
     * @return List of all playlists found related to given name.
     */
    List<Playlist> findByPlaylistNameContainingIgnoreCase(String name);
}
