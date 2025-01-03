package edu.carroll.initMusic.web.form.songManagement;

import jakarta.validation.constraints.NotNull;

/**
 * This form is used to help handle deleting a playlist
 */
public class DeletePlaylistForm {
    /** ID of playlist */
    @NotNull
    private Long playlistID;

    /** Name of playlist */
    @NotNull
    private String playlistName;

    /**
     * Gets the playlist id
     *
     * @return The playlist id
     */
    public Long getPlaylistID() {
        return playlistID;
    }

    /**
     * Sets the playlist id to given id
     *
     * @param playlistID ID to set
     */
    public void setPlaylistID(Long playlistID) {
        this.playlistID = playlistID;
    }

    /**
     * Gets the name of the playlist
     *
     * @return The name of the playlist
     */
    public String getPlaylistName() {
        return playlistName;
    }

    /**
     * Sets the playlist's name to given name
     *
     * @param playlistName Name to set
     */
    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }
}
