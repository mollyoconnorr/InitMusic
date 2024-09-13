package edu.carroll.initMusic.jpa.repo;

import edu.carroll.initMusic.jpa.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findBySongNameIgnoreCase(String name);

    List<Song> findBySongNameContainingIgnoreCaseOrderBySongName(String name);

    @Query("SELECT s FROM Song s JOIN s.artists a WHERE LOWER(a.artistFirstName) = LOWER(:firstName) AND (:lastName IS NULL OR LOWER(a.artistLastName) = LOWER(:lastName))")
    List<Song> findByArtistName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("SELECT s FROM Song s JOIN s.album a WHERE LOWER(a.albumName) = LOWER(:albumTitle)")
    List<Song> findByAlbumTitleIgnoreCase(@Param("albumTitle") String albumTitle);
}
