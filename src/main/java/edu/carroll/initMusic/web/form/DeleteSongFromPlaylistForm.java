package edu.carroll.initMusic.web.form;

public class DeleteSongFromPlaylistForm {
    private Long playlistID;
    private Long songID;

    public Long getPlaylistID() {
        return playlistID;
    }

    public void setPlaylistID(Long playlistID) {
        this.playlistID = playlistID;
    }

    public Long getSongID() {
        return songID;
    }

    public void setSongID(Long songID) {
        this.songID = songID;
    }
}
