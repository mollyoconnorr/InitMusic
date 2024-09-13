package edu.carroll.initMusic.jpa.repo;

import edu.carroll.initMusic.jpa.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByAlbumNameContainingIgnoreCase(String name);

    @Query("SELECT a FROM Album a JOIN a.artists ar WHERE ar.artistFirstName = :firstName AND (:lastName IS NULL OR ar.artistLastName = :lastName)")
    List<Album> findByArtistName(@Param("firstName") String firstName, @Param("lastName") String lastName);

}
