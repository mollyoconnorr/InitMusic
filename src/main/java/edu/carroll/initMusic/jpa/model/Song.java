package edu.carroll.initMusic.jpa.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This class is used to represent songs, and
 * stores several attributes about each song.
 */
@Entity
@Table(name = "song")
public class Song {
    /** Serial version ID */
    private static final long serialVersionID = 1L;

    /**
     * Each song id is generated when the song is inserted
     * into our database and all are unique.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    /**
     * Stores the songs deezerID, which is given to the song
     * using the deezer api. Helpful when keeping track of songs
     * in our database and preventing duplicates
     */
    private Long deezerID;

    /**
     * Set that keeps track of what playlists this song is in.
     * Many-to-Many relationship with Playlist class
     */
    @ManyToMany(mappedBy = "songs", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Playlist> playlists = new HashSet<>();

    @ManyToMany(mappedBy = "results", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<QueryCache> queryCaches = new HashSet<>();

    /** Name of song */
    @Column(name = "name", nullable=false)
    private String songName;

    /** Length of song in seconds */
    @Column(name = "length", nullable=false)
    private int length;

    /** Name of artist who produced the song */
    @Column(name = "artist_name", nullable=false)
    private String artistName;

    /** Deezer ID of artist */
    @Column(name = "artist_deezer_id", nullable=false)
    private Long artistDeezerID;

    /** Name of album this song is in */
    @Column(name = "album_name", nullable=false)
    private String albumName;

    /** Deezer ID of album */
    @Column(name = "album_deezer_id", nullable=false)
    private Long albumDeezerID;

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
     * @param artistDeezerID Deezer id of artist
     * @param albumName Album song is in
     * @param albumDeezerID Deezer id of album
     */
    public Song(Long songID, String songName, int length, String artistName, long artistDeezerID, String albumName, long albumDeezerID) {
        this.deezerID = songID;
        this.songName = songName;
        this.length = length;
        this.artistName = artistName;
        this.artistDeezerID = artistDeezerID;
        this.albumName = albumName;
        this.albumDeezerID = albumDeezerID;
    }

    /**
     * Gets the songs ID number
     * @return Song's ID number
     */
    public Long getDeezerID() {
        return deezerID;
    }

    /**
     * Sets the song ID
     * @param songID ID to set
     */
    public void setDeezerID(Long songID) {
        this.deezerID = songID;
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
    public Long getArtistDeezerID() {
        return artistDeezerID;
    }

    /**
     * Sets id of artist
     * @param artistDeezerID Id to set
     */
    public void setArtistDeezerID(Long artistDeezerID) {
        this.artistDeezerID = artistDeezerID;
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
    public Long getAlbumDeezerID() {
        return albumDeezerID;
    }

    /**
     * Sets Deezer ID of songs' album
     * @param albumDeezerID ID to set
     */
    public void setAlbumDeezerID(Long albumDeezerID) {
        this.albumDeezerID = albumDeezerID;
    }

    /**
     * Gets query cache set
     * @return Set of queryCaches song is in
     */
    public Set<QueryCache> getQueryCaches() {
        return queryCaches;
    }

    /**
     * Sets query cache set
     * @param queryCaches New set of queryCaches song to set
     */
    public void setQueryCaches(Set<QueryCache> queryCaches) {
        this.queryCaches = queryCaches;
    }

    /**
     * Adds a queryCache to this song
     * @param queryCache queryCache to add
     */
    public void addQueryCache(QueryCache queryCache) {
        this.queryCaches.add(queryCache);
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
                Objects.equals(artistDeezerID, song.artistDeezerID) &&
                Objects.equals(albumDeezerID, song.albumDeezerID) &&
                Objects.equals(deezerID, song.deezerID) &&
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
        return Objects.hash(songName, length, deezerID,artistName, artistDeezerID, albumDeezerID);
    }

    /**
     * Converts Song object to string
     * @return String version of Song
     */
    @Override
    public String toString() {
        return "Song{" + "songName='" + songName + '\'' +
                ", deezerID=" + deezerID +
                ", artistName='" + artistName + '\'' +
                ", artistDeezerID=" + artistDeezerID +
                ", albumName='" + albumName + '\'' +
                ", albumDeezerID=" + albumDeezerID +
                ", length=" + length +
                ", songImg='" + songImg + '\'' +
                ", songPreview='" + songPreview + '\'' +
                '}';
    }
}
