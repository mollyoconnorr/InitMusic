package edu.carroll.initMusic.jpa.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;

/**
 * This class is used to represent songs, and
 * stores several attributes about each song.
 *
 * @author Nick Clouse
 *
 * @since September 11, 2024
 */
@Entity
@Table(name = "song")
public class Song {
    /** Serial version ID */
    private static final long serialVersionID = 1L;

    /**
     * ID number for song. A songs id is its deezer ID,
     * makes it easier to check if a song is in database or not.
     */
    @Id
    private Long songID;

    /**
     * Set that keeps track of what playlists this song is in.
     * Many-to-Many relationship with Playlist class
     */
    @ManyToMany(mappedBy = "songs", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Playlist> playlists = new HashSet<>();

    /**
     * Set that keeps track of what artists made this song.
     * Many-to-Many relationship with Artist class
     */
    @ManyToMany(mappedBy = "songs")
    private Set<Artist> artists = new HashSet<>();

    /**
     * Album song belongs to.
     * Many-to-One relationship with Album class
     */
    @ManyToOne()
    @JoinColumn(name = "albumID")
    private Album album;

    /** Name of song */
    @Column(name = "name", nullable=false)
    private String songName;

    /**
     * Release date of song,
     * Has length of 10 for YYYY/MM/DD format
     */
    @Column(name = "release_date", length = 10)
    private String releaseDate;

    /** Length of song in seconds */
    @Column(name = "length", nullable=false)
    private int length;

    /** Name of artist who produced the song */
    @Column(name = "artist_name", nullable=false)
    private String artistName;

    /** Deezer ID of artist */
    @Column(name = "artist_id", nullable=false)
    private long artistID;

    /** Name of album this song is in */
    @Column(name = "album_name", nullable=false)
    private String albumName;

    /** Deezer ID of album */
    @Column(name = "album_id", nullable=false)
    private long albumID;

    /** Link to songs cover art */
    @Column(name = "song_img")
    private String songImg;


    /** Link to a preview of song, approx 30 sec long */
    @Column(name = "song_preview", columnDefinition = "TEXT") // Change to TEXT
    private String songPreview;

    /**
    * JPA needs this default constructor to instantiate entities when retrieving data from the database.
    */
    public Song() {
        //Default Constructor
    }

    /**
     * @param songID Song's Deezer id
     * @param songName Name of song
     * @param length Length of song in seconds
     * @param artistName Artist who made song
     * @param artistID Deezer id of artist
     * @param albumName Album song is in
     * @param albumID Deezer id of album
     */
    public Song(Long songID, String songName, int length, String artistName, long artistID, String albumName, long albumID) {
        this.songID = songID;
        this.songName = songName;
        this.length = length;
        this.artistName = artistName;
        this.artistID = artistID;
        this.albumName = albumName;
        this.albumID = albumID;
    }

    /**
     * Gets the songs ID number
     * @return Song's ID number
     */
    public Long getSongID() {
        return songID;
    }

    /**
     * Sets the song ID
     * @param songID ID to set
     */
    public void setSongID(Long songID) {
        this.songID = songID;
    }

    /**
     * Gets the set of playlists the song is in
     * @return Set of playlists
     */
    public Set<Playlist> getPlaylists() {
        return playlists;
    }

    /**
     * Set playlists song appears in
     * @param playlists Playlist set to use
     */
    public void setPlaylists(Set<Playlist> playlists) {
        this.playlists = playlists;
    }

    /**
     * Add playlist song is in
     * @param playlist Playlist to add
     */
    public void addPlaylist(Playlist playlist) {
        this.playlists.add(playlist);
    }

    /**
     * Gets name of song
     * @return The name of song
     */
    public String getSongName() {
        return songName;
    }

    /**
     * Sets the song name
     * @param songName Name to set
     */
    public void setSongName(String songName) {
        this.songName = songName;
    }

    /**
     * Gets artists of song
     * @return The artists
     */
    public Set<Artist> getArtists() {
        return artists;
    }

    /**
     * Checks if song was created by given artist
     * @param artist Artist to check
     * @return If artist created the song, false otherwise
     */
    public boolean isCreatedBy(Artist artist){
        return artists.contains(artist);
    }

    /**
     * Sets the artists of song
     * @param artists Artists to set
     */
    public void setArtists(Set<Artist> artists) {
        this.artists = artists;
    }

    /**
     * Add artist to artists set
     * @param artist Artist to add
     */
    public void addArtist(Artist artist) {
        this.artists.add(artist);
    }

    /**
     * Remove artist
     * @param artist Artist to remove
     * @return If artist was removed
     */
    public boolean removeArtist(Artist artist) {
        return this.artists.remove(artist);
    }

    /**
     * Gets album of song
     * @return The album the song is in
     */
    public Album getAlbum() {
        return album;
    }

    /**
     * Sets the album the song is in
     * @param album Album to set
     */
    public void setAlbum(Album album) {
        this.album = album;
    }

    /**
     * Gets release date of song
     * @return Release date of song
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * Sets release date of the song
     * @param releaseDate Release date to set
     */
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * Gets length of song
     * @return Length of song
     */
    public int getLength() {
        return length;
    }

    /**
     * Sets the length of the song
     * @param length Length to set
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * Gets the cover art for the song
     * @return The songs cover art url
     */
    public String getSongImg() {
        return songImg;
    }

    /**
     * Sets the songs cover art
     * @param songImg Image url to set
     */
    public void setSongImg(String songImg) {
        this.songImg = songImg;
    }

    /**
     * Gets the url for the songs preview
     * @return The url for the songs preview
     */
    public String getSongPreview() {
        return songPreview;
    }

    /**
     * Sets the url for the songs preview
     * @param songPreview Url to set
     */
    public void setSongPreview(String songPreview) {
        this.songPreview = songPreview;
    }

    /**
     * Gets name of artist who made song
     * @return Name of artist who made song
     */
    public String getArtistName() {
        return artistName;
    }

    /**
     * Sets name of artist that made song
     * @param artistName Name to set
     */
    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    /**
     * Gets Deezer id of artist
     * @return Deezer id of artist
     */
    public long getArtistID() {
        return artistID;
    }

    /**
     * Sets id of artist
     * @param artistID Id to set
     */
    public void setArtistID(long artistID) {
        this.artistID = artistID;
    }

    /**
     * Gets name of songs' album
     * @return Name of album
     */
    public String getAlbumName() {
        return albumName;
    }

    /**
     * Sets name of songs' album
     * @param albumName Name to set
     */
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    /**
     * Gets Deezer ID of songs' album
     * @return ID of album
     */
    public long getAlbumID() {
        return albumID;
    }

    /**
     * Sets Deezer ID of songs' album
     * @param albumID ID to set
     */
    public void setAlbumID(long albumID) {
        this.albumID = albumID;
    }

    /**
     * Compares Song object to another Song object
     * @param o Object to compare to
     * @return If two objects are equal or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Song song = (Song) o;
        return length == song.length &&
                artistID == song.artistID &&
                albumID == song.albumID &&
                Objects.equals(songID, song.songID) &&
                Objects.equals(songName, song.songName) &&
                Objects.equals(artistName, song.artistName) &&
                Objects.equals(albumName, song.albumName) &&
                Objects.equals(songImg, song.songImg) &&
                Objects.equals(songPreview, song.songPreview);
    }

    /**
     * Converts object to hash code
     * @return Object in hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(songName, length, songID,artistName, artistID, album, albumID);
    }

    /**
     * Converts Song object to string
     * @return String version of Song
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Song{");
        sb.append("songName='").append(songName).append('\'');
        sb.append(", songID=").append(songID);
        sb.append(", artistName='").append(artistName).append('\'');
        sb.append(", artistID=").append(artistID);
        sb.append(", albumName='").append(albumName).append('\'');
        sb.append(", albumID=").append(albumID);
        sb.append(", length=").append(length);
        sb.append(", songImg='").append(songImg).append('\'');
        sb.append(", songPreview='").append(songPreview).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
