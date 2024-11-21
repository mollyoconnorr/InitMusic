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
     * @param songName Name of song to look for
     * @param artistName Name of artist to look for
     * @return Set of songs related to the query
     */
    Set<Song> searchForSongs(String songName,String artistName);

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

    /**
     * Checks if the given query is valid according to given standards
     * @param query Query to check if valid
     * @return  {@code true} if query is valid, {@code false} otherwise.
     */
    boolean isValidQuery(String query);

    /**
     * Retrieves the number of songs currently stored in the song repository.
     * @return the size of the song repository.
     */
    long getRepoSize();

    /**
     * Clears all songs from the repository.
     * This is primarily useful for testing or resetting data.
     */
    void clearRepo();

    Song findSong(Song song);

}
