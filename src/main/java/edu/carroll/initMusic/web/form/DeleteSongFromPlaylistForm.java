package edu.carroll.initMusic.web.form;

/**
 * This form is used to help handle deleting a playlist
 *
 * @author Nick Clouse
 *
 * @since October 17, 2024
 */
public class DeleteSongFromPlaylistForm {
    /** ID of playlist */
    private Long playlistID;

    /** ID of song */
    private Long songID;

    /** Name of playlist */
    private String playlistName;

    /** Name of song */
    private String songName;

    /**
     * Gets the id of the playlist
     * @return The playlist's id
     */
    public Long getPlaylistID() {
        return playlistID;
    }

    /**
     * Sets the playlist's ID
     * @param playlistID ID to set
     */
    public void setPlaylistID(Long playlistID) {
        this.playlistID = playlistID;
    }

    /**
     * Gets the id of the song
     * @return The song's id
     */
    public Long getSongID() {
        return songID;
    }

    /**
     * Sets the song's ID
     * @param songID ID to set
     */
    public void setSongID(Long songID) {
        this.songID = songID;
    }

    /**
     * Gets the name of the song
     * @return The song's name
     */
    public String getSongName() {
        return songName;
    }

    /**
     * Sets the playlist's name
     * @param songName Name to set
     */
    public void setSongName(String songName) {
        this.songName = songName;
    }

    /**
     * Gets the name of the playlist
     * @return The playlist's name
     */
    public String getPlaylistName() {
        return playlistName;
    }

    /**
     * Sets the playlist's name
     * @param playlistName Name to set
     */
    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }
}
