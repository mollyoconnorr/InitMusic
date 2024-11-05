package edu.carroll.initMusic.service;

import edu.carroll.initMusic.jpa.model.Song;

import java.util.Set;

/**
 * Defines methods related to searching for songs
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
}
