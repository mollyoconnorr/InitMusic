package edu.carroll.initMusic.web.form;

import jakarta.validation.constraints.NotBlank;

/**
 * This form is used to make a new playlist. It only
 * has one attribute: The name of the new playlist.
 */
public class NewPlaylistForm {
    /**
     * Name of new playlist
     */
    @NotBlank(message = "Playlist name can not be blank!")
    private String playlistName;

    /**
     * Gets the name of the new playlist
     *
     * @return Name of playlist
     */
    public String getPlaylistName() {
        return playlistName;
    }

    /**
     * Sets the name of the playlist
     *
     * @param playlistName Name to set
     */
    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }
}
