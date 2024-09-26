package edu.carroll.initMusic.jpa.repo;

import edu.carroll.initMusic.jpa.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository used for performing CRUD actions on the Artist Entity
 */
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    /**
     * Finds artist by name.
     * @param name Name of artist
     * @return List of all artists found
     */
    List<Artist> findByArtistName(String name);
}
