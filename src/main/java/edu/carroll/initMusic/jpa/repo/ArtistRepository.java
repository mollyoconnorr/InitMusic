package edu.carroll.initMusic.jpa.repo;

import edu.carroll.initMusic.jpa.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    List<Artist> findByArtistFirstNameAndArtistLastNameContainingIgnoreCase(String firstName, String lastName);
}
