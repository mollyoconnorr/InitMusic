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
public enum MethodOutcome {
    SUCCESS("Success"),
    USER_NOT_FOUND("User not found!"),
    USER_ALREADY_EXISTS("User already exists!"),
    USER_TOO_SHORT("Username must be at least 5 characters!"),
    USER_TOO_LONG("Username must be less than 50 characters!"),
    USER_HAS_SPACES("Username can't contain any spaces!"),
    EMAIL_INVALID_FORMAT("Email address is not a valid email address!"),
    EMAIL_ALREADY_EXISTS("Email address already exists!"),
    EMAIL_TOO_LONG("Email address must be less than 255 characters!"),
    EMAIL_LOCAL_PART_TOO_LONG("Local part of email (Before the @) exceeds maximum length of 64 characters"),
    EMAIL_DOMAIN_LABEL_TOO_LONG("Domain label (After the @) exceeds maximum length of 63 characters"),
    SONG_NOT_FOUND("Song not found!"),
    PLAYLIST_NOT_FOUND("Playlist not found!"),
    PLAYLIST_ALREADY_EXISTS("Playlist already exists!"),
    PLAYLIST_ALREADY_CONTAINS_SONG("Playlist already contains given song!"),
    PLAYLIST_NAME_EXISTS("Playlist with given name already exists!"),
    PLAYLIST_NAME_INVALID("Playlist name is invalid!"),
    PLAYLIST_RENAME_ERROR("Playlist could not be renamed."),
    SONG_NOT_IN_PLAYLIST("Song not found in playlist!");

    /**
     * Message of the constant, part of what is displayed to the user
     */
    private final String message;

    /**
     * Constructs a new constant
     * @param message Message of constant
     */
    MethodOutcome(String message) {
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
