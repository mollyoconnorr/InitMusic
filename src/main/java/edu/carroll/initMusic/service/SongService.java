package edu.carroll.initMusic.service;

import edu.carroll.initMusic.jpa.model.Song;

import java.util.Set;

/**
 * Interface for SongService, defines methods related to searching for songs
 * and adding songs to playlists.
 *
 * @author Nick Clouse
 *
 * @since October 20, 2024
 */
public interface SongService {

    /**
     * Searches for songs related to given query.
     * @param query Query to search for
     * @return Set of songs related to query
     */
    Set<Song> searchForSongs(String query);

    /**
     * Adds a song to the given playlist.
     * @param playlistId ID of playlist to add song to
     * @param song Song to add to playlist
     * @return True if playlist was added, false if not
     */
    boolean addSongToPlaylist(Long playlistId, Song song);
}
