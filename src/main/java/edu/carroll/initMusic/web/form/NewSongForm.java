package edu.carroll.initMusic.web.form;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

public class NewSongForm {
    private Long songID = 0L; // Default value to avoid null
    private String songName = ""; // Default empty string
    private int songLength = 0;
    private String artistName = "";
    private Long artistID = 0L; // Default value
    private String albumName = "";
    private Long albumID = 0L; // Default value
    private String songImg = "";
    private String songPreview = "";
    private List<Long> selectedPlaylists = new ArrayList<>();

    public Long getSongID() {
        return songID;
    }

    public void setSongID(Long songID) {
        this.songID = songID;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public int getSongLength() {
        return songLength;
    }

    public void setSongLength(int songLength) {
        this.songLength = songLength;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public long getArtistID() {
        return artistID;
    }

    public void setArtistID(long artistID) {
        this.artistID = artistID;
    }

    public long getAlbumID() {
        return albumID;
    }

    public void setAlbumID(long albumID) {
        this.albumID = albumID;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getSongPreview() {
        return songPreview;
    }

    public void setSongPreview(String songPreview) {
        this.songPreview = songPreview;
    }

    public String getSongImg() {
        return songImg;
    }

    public void setSongImg(String songImg) {
        this.songImg = songImg;
    }

    public List<Long> getSelectedPlaylists() {
        return selectedPlaylists;
    }

    public void setSelectedPlaylists(List<Long> selectedPlaylists) {
        this.selectedPlaylists = selectedPlaylists;
    }
}
