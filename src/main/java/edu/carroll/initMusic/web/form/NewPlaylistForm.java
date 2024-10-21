package edu.carroll.initMusic.web.form;

/**
 * This form is used to make a new playlist. It only
 * has one attribute: The name of the new playlist.
 *
 * @author Nick Clouse
 *
 * @since October 9, 2024
 */
public class NewPlaylistForm {
    /**
     * Name of new playlist
     */
    private String playlistName;

    /**
     * Gets the name of the new playlist
     * @return Name of playlist
     */
    public String getPlaylistName() {
        return playlistName;
    }

    /**
     * Sets the name of the playlist
     * @param playlistName Name to set
     */
    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

}
