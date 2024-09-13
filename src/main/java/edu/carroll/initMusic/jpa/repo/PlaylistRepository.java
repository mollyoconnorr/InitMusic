package edu.carroll.initMusic.jpa.repo;

import edu.carroll.initMusic.jpa.model.Playlist;
import edu.carroll.initMusic.jpa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    @Query("SELECT p FROM Playlist p WHERE p.author.username LIKE %:username%")
    List<Playlist> findByAuthorUsernameContainingIgnoreCase(@Param("username") String username);

    List<Playlist> findByPlaylistNameContainingIgnoreCase(String name);
}
