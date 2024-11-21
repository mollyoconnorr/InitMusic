package edu.carroll.initMusic.jpa.repo;

import edu.carroll.initMusic.jpa.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository used for managing and retrieving the Playlist Entity
 *
 * @author Nick Clouse
 * @since September 11, 2024
 */
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    /**
     * Finds playlist by its id
     *
     * @param id ID to check for
     * @return List of all playlists found with given id (Should only be 1)
     */
    List<Playlist> findByPlaylistIDEquals(Long id);
}
