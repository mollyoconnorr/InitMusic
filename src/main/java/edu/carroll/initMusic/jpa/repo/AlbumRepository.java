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

    List<Album>  findByArtists_ArtistNameAndAlbumName(String artistName,String albumName);

}
