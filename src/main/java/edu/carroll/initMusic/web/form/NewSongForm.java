package edu.carroll.initMusic.web.form;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This form is used when a song is going to be added to a playlist.
 * We have to pass all the song attributes so we can make the song into
 * a object.
 * </p>
 *
 * @author Nick Clouse
 *
 * @since October 9, 2024
 */
public class NewSongForm {
    /**
     * Deezer id of the song
     */
    private Long songID = 0L;

    /**
     * Name of song
     */
    private String songName = "";

    /**
     * Length of song
     */
    private int songLength = 0;

    /**
     * Name of artist
     */
    private String artistName = "";

    /**
     * Deezer id of artist who made the song
     */
    private Long artistID = 0L;

    /**
     * Name of album song is in
     */
    private String albumName = "";

    /**
     * Deezer id of album
     */
    private Long albumID = 0L;

    /**
     * Link to songs image
     */
    private String songImg = "";

    /**
     * Link to songs preview
     */
    private String songPreview = "";

    /**
     * Id's of playlists the song will be added to
     */
    private List<Long> selectedPlaylists = new ArrayList<>();

    /**
     * Gets the songs deezer id
     * @return The deezer id of the song
     */
    public Long getSongID() {
        return songID;
    }

    /**
     * Sets the songs id
     * @param songID ID to set
     */
    public void setSongID(Long songID) {
        this.songID = songID;
    }

    /**
     * Gets the name of the song
     * @return The name of the song
     */
    public String getSongName() {
        return songName;
    }

    /**
     * Sets the songs name
     * @param songName Name to set
     */
    public void setSongName(String songName) {
        this.songName = songName;
    }

    /**
     * Gets the length of the song
     * @return The length of the song
     */
    public int getSongLength() {
        return songLength;
    }

    /**
     * Sets the length of the song
     * @param songLength Length to set
     */
    public void setSongLength(int songLength) {
        this.songLength = songLength;
    }

    /**
     * Gets the name of the artist who made the song
     * @return Name of artist
     */
    public String getArtistName() {
        return artistName;
    }

    /**
     * Sets the name of the artist who made the song
     * @param artistName Name to set
     */
    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    /**
     * Gets the deezer id of the artist
     * @return
     */
    public long getArtistID() {
        return artistID;
    }

    /**
     * sets the id of the artist
     * @param artistID ID to set
     */
    public void setArtistID(long artistID) {
        this.artistID = artistID;
    }

    /**
     * Gets deezer id of the album
     * @return
     */
    public long getAlbumID() {
        return albumID;
    }

    /**
     * Sets the id of the album
     * @param albumID ID to set
     */
    public void setAlbumID(long albumID) {
        this.albumID = albumID;
    }

    /**
     * Gets name of album song is in
     * @return Name of album
     */
    public String getAlbumName() {
        return albumName;
    }

    /**
     * Sets name of album song is in
     * @param albumName Name to set
     */
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    /**
     * Gets the string that contains link to a preview of the song
     * @return The string preview link
     */
    public String getSongPreview() {
        return songPreview;
    }

    /**
     * Sets the song preview link, which is a string
     * @param songPreview String to set
     */
    public void setSongPreview(String songPreview) {
        this.songPreview = songPreview;
    }

    /**
     * Gets the string that contains link to image of the song
     * @return The string image link
     */
    public String getSongImg() {
        return songImg;
    }

    /**
     * Sets the string that contains link to image of the song
     * @param songImg String to set
     */
    public void setSongImg(String songImg) {
        this.songImg = songImg;
    }

    /**
     * Gets the list of playlist ids that the song will be added to
     * @return List of playlist ID's
     */
    public List<Long> getSelectedPlaylists() {
        return selectedPlaylists;
    }

    /**
     * Sets the list of playlist ids that the song will be added to
     * @param selectedPlaylists List to set
     */
    public void setSelectedPlaylists(List<Long> selectedPlaylists) {
        this.selectedPlaylists = selectedPlaylists;
    }
}