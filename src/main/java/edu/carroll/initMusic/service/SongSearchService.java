package edu.carroll.initMusic.service;

import edu.carroll.initMusic.jpa.model.Song;

import java.util.Set;

/**
 * Defines methods related to searching for songs using an API
 */
public interface SongSearchService {

    /**
     * Searches for songs related to given query.
     * In theory, this method should use a API to search for songs
     * from an external service like Deezer, Spotify, or any
     * music API.
     * @param songName Name of song to search for
     * @param artistName Name of artist to search for
     * @return Set of songs related to query
     */
    Set<Song> externalSearchForSongs(String songName, String artistName);
}