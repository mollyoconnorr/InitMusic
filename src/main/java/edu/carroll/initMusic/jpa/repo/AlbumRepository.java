package edu.carroll.initMusic.jpa.repo;

import edu.carroll.initMusic.jpa.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository used for managing and retrieving the Album Entity
 *
 * @author Nick Clouse
 *
 * @since September 11, 2024
 */
public interface AlbumRepository extends JpaRepository<Album, Long> {
    /**
     * Finds album by name, which can be or contain the parameter string
     * @param name Name to search for
     * @return A list of albums found related to name
     */
    List<Album> findByAlbumNameContainingIgnoreCase(String name);

    /**
     * Finds album by artist name and album name
     * @param artistName Name of artist
     * @param albumName Name of album
     * @return A list of albums found related to artist and album
     */
    List<Album>  findByArtists_ArtistNameAndAlbumName(String artistName,String albumName);

}
