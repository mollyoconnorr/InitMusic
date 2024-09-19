package edu.carroll.initMusic.jpa.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;

/**
 * <p>
 * This class is used to represent song objects, and maps to our database table.
 * </p>
 *
 * @author Nick Clouse
 *
 * @since September 11, 2024
 */
@Entity
@Table(name = "song")
public class Song {
    /**
     * Serial version ID
     */
    private static final long serialVersionID = 1L;

    /**
     * ID number for song. Automatically generated
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer songID;

    /**
     * Set that keeps track of what playlists this song is in.
     * Many-to-Many relationship with Playlist class
     */
    @ManyToMany(mappedBy = "songs")
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
    @ManyToOne
    @JoinColumn(name = "albumID")
    private Album album;

    /**
     * Name of song
     */
    @Column(name = "name", nullable=false)
    private String songName;

    /**
     * Closest genre of music for song
     */
    @Column(name = "genre", nullable=false)
    private String genre;

    /**
     * Release date of song,
     * Has length of 10 for MM/DD/YYYY format
     */
    @Column(name = "release_date", length = 10)
    private String releaseDate;

    /**
     * Length of song in seconds
     */
    @Column(name = "length", nullable=false)
    private int length;

    /**
     * Number of streams song has
     */
    @Column(name = "album_name", nullable=true)
    private int numberOfStreams;

    /**
    * JPA needs this constructor to instantiate entities when retrieving data from the database.
    * Its protected so it can't be used to create new Album objects by other classes.
    */
    public Song() {
        //Default Constructor
    }

    /**
     * Creates a new song instance
     * @param songName Name of song
     * @param genre Genre of song
     * @param releaseDate Release date of song
     * @param length Length of song in seconds
     * @param numberOfStreams Number of streams song has
     */
    public Song(String songName, String genre, String releaseDate, int length, int numberOfStreams) {
        this.songName = songName;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.length = length;
        this.numberOfStreams = numberOfStreams;
    }

    /**
     * Gets the songs ID number
     * @return Song's ID number
     */
    public Integer getSongID() {
        return songID;
    }

    /**
     * Sets the song ID
     * @param songID ID to set
     */
    public void setSongID(Integer songID) {
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
    public Set<Artist> getArtist() {
        return artists;
    }

    /**
     * Sets the artists of song
     * @param artists Artists to set
     */
    public void setArtist(Set<Artist> artists) {
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
     * Gets genre of song
     * @return The genre of song
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Sets genre of song
     * @param genre Genre to set
     */
    public void setGenre(String genre) {
        this.genre = genre;
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
     * Gets number of streams song has
     * @return The number of streams
     */
    public int getNumberOfStreams() {
        return numberOfStreams;
    }

    /**
     * Sets the number of streams
     * @param numberOfStreams Number of streams to set
     */
    public void setNumberOfStreams(int numberOfStreams) {
        this.numberOfStreams = numberOfStreams;
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
        Song song = (Song) o;
        return length == song.length &&
                numberOfStreams == song.numberOfStreams &&
                Objects.equals(songID, song.songID) &&
                Objects.equals(songName, song.songName) &&
                Objects.equals(artists, song.artists) &&
                Objects.equals(album, song.album) &&
                Objects.equals(genre, song.genre) &&
                Objects.equals(releaseDate, song.releaseDate);
    }

    /**
     * Converts object to hash code
     * @return Object in hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(songID, songName, artists, album, genre, releaseDate, length, numberOfStreams);
    }

    /**
     * Converts Song object to string
     * @return String version of Song
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Song{");
        sb.append("songID=").append(songID);
        sb.append(", songName='").append(songName).append('\'');
        sb.append(", artist='").append(artists).append('\'');
        sb.append(", album='").append(album).append('\'');
        sb.append(", genre='").append(genre).append('\'');
        sb.append(", releaseDate='").append(releaseDate).append('\'');
        sb.append(", length=").append(length);
        sb.append(", numberOfStreams=").append(numberOfStreams);
        sb.append('}');
        return sb.toString();
    }
}
