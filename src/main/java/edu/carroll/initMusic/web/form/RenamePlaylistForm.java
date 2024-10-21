package edu.carroll.initMusic.web.form;

/**
 * This form is used for renaming a playlist.
 *
 * @author Nick Clouse
 *
 * @since October 13, 2024
 */
public class RenamePlaylistForm {
    /**
     * New name for playlist
     */
    private String newPlaylistName;

    private Long playlistID;

    /**
     * Gets the new name of the playlist
     * @return New name of playlist
     */
    public String getNewPlaylistName() {
        return newPlaylistName;
    }

    /**
     * Sets the new name of the playlist
     * @param newPlaylistName New name to set
     */
    public void setNewPlaylistName(String newPlaylistName) {
        this.newPlaylistName = newPlaylistName;
    }

    public Long getPlaylistID() {
        return playlistID;
    }

    public void setPlaylistID(Long playlistID) {
        this.playlistID = playlistID;
    }
}
