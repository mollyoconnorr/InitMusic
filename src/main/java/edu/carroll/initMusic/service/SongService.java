package edu.carroll.initMusic.service;

import edu.carroll.initMusic.jpa.model.QueryCache;
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
     * Searches for songs related to the given query
     * @param query Query to search for
     * @return Set of songs related to the query
     */
    Set<Song> searchForSongs(String query);

    /**
     * Gets the local cache of songs related to the given query, if any
     * @param query Query to use to check for local cache
     * @return Set of songs found, if any
     */
    Set<Song> getLocalCache(String query);

    /**
     * Creates a new QueryCache with the given query and songs
     * @param query Query that was searched for
     * @param songs Songs found related to query
     * @return {@code true} if cache was created, {@code false} if not
     *
     * @see QueryCache
     */
    boolean createCache(String query, Set<Song> songs);
}
