package edu.carroll.initMusic.jpa.repo;

import edu.carroll.initMusic.jpa.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository used for performing CRUD actions on the Artist Entity
 */
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    /**
     * Finds artist by name. The search will look for all artists who's name contains firstName
     * or lastName, or both.
     * @param firstName First name of artist
     * @param lastName Last name of artist
     * @return List of all artists found
     */
    List<Artist> findByArtistFirstNameOrArtistLastNameContainingIgnoreCase(String firstName, String lastName);
}
