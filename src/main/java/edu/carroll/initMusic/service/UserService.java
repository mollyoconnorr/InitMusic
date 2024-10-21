package edu.carroll.initMusic.service;

import edu.carroll.initMusic.ResponseStatus;
import edu.carroll.initMusic.jpa.model.Playlist;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.web.form.RegistrationForm;
import edu.carroll.initMusic.web.form.SecurityQuestionsForm;

/**
 * Interface for UserService, defines methods related to user transactions like
 * creating and updating user objects, and creating and updating playlist objects
 * for users.
 *
 * @author Nick Clouse
 *
 * @since October 21, 2024
 */
public interface UserService {

    /**
     * Checks if given username is unique
     * @param username Username to check
     * @return True if username is unique, false otherwise
     */
    boolean uniqueUserName(String username);

    /**
     * Checks if given email is unique
     * @param email Email to check
     * @return True if username is unique, false otherwise
     */
    boolean uniqueEmail(String email);

    /**
     * Saves a user and their information to the database
     * @param registrationForm Form
     * @return User object, which contains all the users information
     */
    User saveUser(RegistrationForm registrationForm);

    /**
     * Updates users security questions
     * @param user User object to update
     * @param questionForm Form
     */
    void updateUser(User user, SecurityQuestionsForm questionForm);

    /**
     * Updates the users password
     * @param user User to update
     * @param newPassword New password to use
     */
    void updatePassword(User user, String newPassword);

    /**
     * Finds user by email address
     * @param email Email to search by
     * @return The user object found, if any
     */
    User findByEmail(String email);

    /**
     * Finds user by id, will also eagerly fetch
     * the users playlists in case operations are needed
     * to be done on them.
     * @param userId User id to search by
     * @return The user object found, if any
     */
    User findByIdWithPlaylists(Long userId);

    /**
     * Gets user by username
     * @param username Username to search by
     * @return The user object found, if any
     */
    User getUser(String username);

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
