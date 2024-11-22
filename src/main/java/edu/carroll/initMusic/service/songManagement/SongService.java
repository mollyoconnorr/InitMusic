package edu.carroll.initMusic.service;

import edu.carroll.initMusic.jpa.model.Song;

import java.util.Set;

/**
 * Defines methods related to searching for songs
 */
public interface SongService {
    /**
     * Searches for songs related to the given query
     *
     * @param songName   Name of song to look for
     * @param artistName Name of artist to look for
     * @return Set of songs related to the query, empty set if no songs found
     */
    Set<Song> searchForSongs(String songName, String artistName);

    /**
     * Checks if the given query is valid according to given standards
     *
     * @param query Query to check if valid
     * @return {@code true} if query is valid, {@code false} otherwise.
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

    /**
     * Gets the preview link for the given id
     *
     * @param id id to get preview for
     * @return String of URL
     */
    String getSongPreview(Long id);
}
