package edu.carroll.initMusic;

public enum ResponseStatus {
    SUCCESS("Success"),
    USER_NOT_FOUND("User not found!"),
    SONG_NOT_FOUND("Song not found!"),
    PLAYLIST_NOT_FOUND("Playlist not found!"),
    PLAYLIST_ALREADY_EXISTS("Playlist already exists!"),
    PLAYLIST_NAME_EXISTS("Playlist with given name already exists!"),
    PLAYLIST_NAME_EMPTY("Playlist name cannot be empty!"),
    PLAYLIST_RENAME_ERROR("Playlist could not be renamed."),;

    private final String message;

    ResponseStatus(String message) {
        this.message = message;
    }

    public boolean failed(){
        return !SUCCESS.getMessage().equals(this.message);
    }

    public String getMessage() {
        return message;
    }
}
