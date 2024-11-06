package edu.carroll.initMusic.jpa.repo;

import edu.carroll.initMusic.jpa.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository used for managing and retrieving the Song Entity
 *
 * @author Nick Clouse
 *
 * @since September 11, 2024
 */
public interface SongRepository extends JpaRepository<Song, Long> {
    /**
     * Finds songs containing the given name, ignoring the case, and ordered by song name
     * @param name Name of song to search for
     * @return List of songs found with name or similar names
     */
    List<Song> findBySongNameContainingIgnoreCaseOrderBySongName(String name);
}
