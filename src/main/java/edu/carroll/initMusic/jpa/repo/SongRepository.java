package edu.carroll.initMusic.jpa.repo;

import edu.carroll.initMusic.jpa.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository used for performing CRUD actions on the Song Entity
 */
public interface SongRepository extends JpaRepository<Song, Long> {
    /**
     * Finds songs containing the given name, ignoring the case, and ordered by song name
     * @param name Name of song to search for
     * @return List of songs found with name or similar names
     */
    List<Song> findBySongNameContainingIgnoreCaseOrderBySongName(String name);

    /**
     * Finds songs by artist, where artist first name is required, but last name is optional
     * @param firstName Artist's first name
     * @param lastName Artist's last name
     * @return List of songs found
     */
    @Query("SELECT s FROM Song s JOIN s.artists a WHERE LOWER(a.artistFirstName) = LOWER(:firstName) AND (:lastName IS NULL OR LOWER(a.artistLastName) = LOWER(:lastName))")
    List<Song> findByArtistName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    /**
     * Finds songs by album
     * @param albumTitle Album title to search for
     * @return List of songs found
     */
    @Query("SELECT s FROM Song s JOIN s.album a WHERE LOWER(a.albumName) = LOWER(:albumTitle)")
    List<Song> findByAlbumTitleIgnoreCase(@Param("albumTitle") String albumTitle);
}
