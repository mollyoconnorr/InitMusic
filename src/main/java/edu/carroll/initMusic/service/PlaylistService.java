package edu.carroll.initMusic.service;

import edu.carroll.initMusic.ResponseStatus;
import edu.carroll.initMusic.jpa.model.Playlist;
import edu.carroll.initMusic.jpa.model.Song;
import edu.carroll.initMusic.jpa.model.User;

/**
 * Interface for PlaylistService, defines methods related to interacting with playlists.
 *
 * @author Nick Clouse
 *
 * @since October 23, 2024
 */
public interface PlaylistService{
    /**
     * Adds a song to the given playlist.
     * @param playlistId ID of playlist to add song to
     * @param song Song to add to playlist
     * @return True if playlist was added, false if not
     */
    boolean addSongToPlaylist(Long playlistId, Song song);

    /**
     * Gets a playlist by playlist ID
     * @param playlistID Playlist ID to search by
     * @return The playlist object found, if any
     */
    Playlist getPlaylist(Long playlistID);

    /**
     * Creates a new playlist under the given user with the given name
     * @param name Name of playlist
     * @param user User who created playlist
     * @return A responseStatus enum, which tells the outcome of the transaction.
     *
     * @see ResponseStatus
     */
    ResponseStatus createPlaylist(String name, User user);

    /**
     * Renames the given playlist with the new given name
     * @param newName New name of playlist
     * @param playlistID ID of playlist to rename
     * @param user User who created playlist
     * @return A responseStatus enum, which tells the outcome of the transaction.
     *
     * @see ResponseStatus
     */
    ResponseStatus renamePlaylist(String newName, Long playlistID, User user);

    /**
     * Deletes the given playlist
     * @param playlistName Name of playlist to delete
     * @param playlistID ID of playlist to delete
     * @param user User who created playlist
     * @return A responseStatus enum, which tells the outcome of the transaction.
     *
     * @see ResponseStatus
     */
    ResponseStatus deletePlaylist(String playlistName, Long playlistID, User user);

    /**
     * Removes the given song from the given playlist
     * @param playlistID ID of playlist song is in
     * @param songID ID of song to remove
     * @return A responseStatus enum, which tells the outcome of the transaction.
     *
     * @see ResponseStatus
     */
    ResponseStatus removeSongFromPlaylist(Long playlistID, Long songID);
}
