package edu.carroll.initMusic.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * This form is used for renaming a playlist.
 *
 * @author Nick Clouse
 * @since October 13, 2024
 */
public class RenamePlaylistForm {
    /** New name for playlist */
    @NotBlank(message = "Name of playlist can not be blank!")
    private String newPlaylistName;

    /** ID of playlist */
    @NotNull
    private Long playlistID;

    /**
     * Gets the new name of the playlist
     *
     * @return New name of playlist
     */
    public String getNewPlaylistName() {
        return newPlaylistName;
    }

    /**
     * Sets the new name of the playlist
     *
     * @param newPlaylistName New name to set
     */
    public void setNewPlaylistName(String newPlaylistName) {
        this.newPlaylistName = newPlaylistName;
    }

    /**
     * Gets id of playlist
     *
     * @return ID of playlist
     */
    public Long getPlaylistID() {
        return playlistID;
    }

    /**
     * Sets playlist id
     *
     * @param playlistID ID to set
     */
    public void setPlaylistID(Long playlistID) {
        this.playlistID = playlistID;
    }
}
