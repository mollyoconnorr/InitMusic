package edu.carroll.initMusic;

/**
 * <p>
 * This enum is used to assist with providing user feedback after operations are performed, like
 * adding, deleting, or creating playlists. Each constant has a corresponding message which tells
 * what the constant means.
 * </p>
 *
 * @author Nick Clouse
 *
 * @since October 2, 2024
 */
public enum ResponseStatus {
    SUCCESS("Success"),
    USER_NOT_FOUND("User not found!"),
    SONG_NOT_FOUND("Song not found!"),
    PLAYLIST_NOT_FOUND("Playlist not found!"),
    PLAYLIST_ALREADY_EXISTS("Playlist already exists!"),
    PLAYLIST_NAME_EXISTS("Playlist with given name already exists!"),
    PLAYLIST_NAME_EMPTY("Playlist name cannot be empty!"),
    PLAYLIST_RENAME_ERROR("Playlist could not be renamed."),
    SONG_NOT_IN_PLAYLIST("Song is not playlist!");

    /**
     * Message of the constant, part of what is displayed to the user
     */
    private final String message;

    /**
     * Constructs a new constant
     * @param message Message of constant
     */
    ResponseStatus(String message) {
        this.message = message;
    }

    /**
     * Checks if the response is anything but the success constant. Used when
     * checking if a operation succeeded or failed.
     * @return If the response is not the success message.
     */
    public boolean failed(){
        return !SUCCESS.getMessage().equals(this.message);
    }

    /**
     * Gets the message of the constant
     * @return The message
     */
    public String getMessage() {
        return message;
    }
}
