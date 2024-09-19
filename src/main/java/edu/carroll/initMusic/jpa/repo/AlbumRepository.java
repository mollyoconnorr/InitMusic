package edu.carroll.initMusic.jpa.repo;

import edu.carroll.initMusic.jpa.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository used for performing CRUD actions on the Album Entity
 */
public interface AlbumRepository extends JpaRepository<Album, Long> {
    /**
     * Finds album by name, which can be or contain the parameter string
     * @param name Name to search for
     * @return A list of albums found related to name
     */
    List<Album> findByAlbumNameContainingIgnoreCase(String name);

    /**
     * Finds albums based off of artist name
     * @param firstName First name of artist
     * @param lastName Last name of artist, can be optional
     * @return List of albums found related to artist
     */
    @Query("SELECT a FROM Album a JOIN a.artists ar WHERE ar.artistFirstName = :firstName AND (:lastName IS NULL OR ar.artistLastName = :lastName)")
    List<Album> findByArtistName(@Param("firstName") String firstName, @Param("lastName") String lastName);

}
