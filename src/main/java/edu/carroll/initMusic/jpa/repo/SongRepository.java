package edu.carroll.initMusic.jpa.repo;

import edu.carroll.initMusic.jpa.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository used for retrieving Song Entities
 *
 * @author Nick Clouse
 * @since September 11, 2024
 */
public interface SongRepository extends JpaRepository<Song, Long> {
    /**
     * Finds song objects by id
     *
     * @param id ID to search for
     * @return List of Song objects found with given id (Should only be one, since each ID is unique)
     */
    List<Song> findByDeezerID(Long id);
}
