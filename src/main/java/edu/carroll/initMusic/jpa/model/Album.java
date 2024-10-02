package edu.carroll.initMusic.jpa.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;

/**
 * <p>
 * This class is used to represent album objects, and maps to our database table.
 * An album is a collection of songs created by a music artist
 * </p>
 *
 * @author Nick Clouse
 *
 * @since September 11, 2024
 */
@Entity
@Table(name = "album")
public class Album {
    /**
     * Serial version ID
     */
    private static final long serialVersionID = 1L;

    /**
     * ID number for album. Automatically generated
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer albumID;

    /**
     * Set of songs that belong in playlist
     * One-to-Many relationship with Song class
     */
    @OneToMany(mappedBy = "album")
    private final Set<Song> songs = new HashSet<>();

    /**
     * Set that keeps track of what artists this album belongs to.
     * Many-to-Many relationship with Artist class
     */
    @ManyToMany(mappedBy = "albums")
    private final Set<Artist> artists = new HashSet<>();

    /**
     * Name of album
     */
    @Column(name = "album_name", nullable = false)
    private String albumName;

    /**
     * Genre of album
     */
    @Column(name = "genre", nullable = false)
    private String genre;

    /**
     * Release date of album.
     * Has length of 10 for MM/DD/YYYY format
     */
    @Column(name = "release_date", nullable = false, length = 10)
    private String releaseDate;

    /**
     * Total length of album, which is the sum of the length of all songs in album.
     * Default of 0, because on creation, there are no songs in an album.
     */
    @Column(name = "total_song_length", nullable = false, columnDefinition = "int default 0")
    private int totalSongLength;

    /**
     * Number of songs in the album.
     * Default of 0, because on creation, there are no songs in an album.
     */
    @Column(name = "number_of_songs", nullable = false, columnDefinition = "int default 0")
    private int numberOfSongs;

    /**
     * JPA needs this constructor to instantiate entities when retrieving data from the database.
     * Its protected so it can't be used to create new Album objects by other classes.
     */
    protected Album() {
        //Default Constructor
    }

    /**
     * Creates a new album object
     * @param albumName Name of album
     * @param genre Genre of album
     * @param releaseDate Release date of album
     */
    public Album(String albumName, String genre, String releaseDate) {
        this.albumName = albumName;
        this.genre = genre;
        this.releaseDate = releaseDate;
    }

    /**
     * Gets number of songs in album
     * @return Number of songs in album
     */
    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    /**
     * Sets number of songs in album
     * @param numberOfSongs  Number of songs in album to set
     */
    public void setNumberOfSongs(int numberOfSongs) {
        this.numberOfSongs = numberOfSongs;
    }

    /**
     * Adds artist to this album
     * @param artist Artist to add
     */
    public void addArtist(Artist artist) {
        this.artists.add(artist);
        artist.getAlbums().add(this); // Keep the relationship bidirectional
    }

    /**
     * Removes artist from album
     * @param artist Artist to remove
     */
    public void removeArtist(Artist artist) {
        this.artists.remove(artist);
        artist.getAlbums().remove(this);
    }

    /**
     * Gets artists of album
     * @return Set of artists
     */
    public Set<Artist> getArtists() {
        return this.artists;
    }

    /**
     * Check if a artist made this album
     * @param artist Album to check
     * @return If artist made this album
     */
    public boolean madeByArtist(Artist artist) {
        return this.artists.contains(artist);
    }

    /**
     * Gets ID of album
     * @return ID of album
     */
    public Integer getAlbumID() {
        return albumID;
    }

    /**
     * Sets the album ID
     * @param albumID ID to set
     */
    public void setAlbumID(Integer albumID) {
        this.albumID = albumID;
    }

    /**
     * Get songs in album
     * @return Set of songs
     */
    public Set<Song> getSongs() {
        return songs;
    }

    /**
     * Checks if album contains song
     * @param song Song to check for
     * @return If album contains song, false otherwise
     */
    public boolean containsSong(Song song) {
        return this.songs.contains(song);
    }

    /**
     * Adds song to album
     * @param song Song to add
     */
    public void addSong(Song song) {

        this.songs.add(song);
        this.numberOfSongs++;
        this.totalSongLength += song.getLength();
    }

    /**
     * Removes song from album
     * @param song Song to remove
     */
    public void removeSong(Song song) {
        this.songs.remove(song);
        this.numberOfSongs--;
        this.totalSongLength -= song.getLength();
    }

    /**
     * Gets name of album
     * @return Name of album
     */
    public String getAlbumName() {
        return albumName;
    }

    /**
     * Sets album name
     * @param albumName Name to set
     */
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    /**
     * Gets genre of album
     * @return Genre of album
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Sets genre of album
     * @param genre Genre to set
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Gets release date of album
     * @return Release date of album
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * Sets release date of album
     * @param releaseDate Release date to set
     */
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * Gets total length of album, which is the combined length of all songs
     * in the album
     * @return Total length of album
     */
    public int getTotalSongLength() {
        return totalSongLength;
    }

    /**
     * Sets the total length of the album
     * @param totalSongLength Length to set
     */
    public void setTotalSongLength(int totalSongLength) {
        this.totalSongLength = totalSongLength;
    }

    /**
     * Compares Album to another album
     * @param o Other album to compare
     * @return If albums are equal or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return Objects.equals(artists, album.artists) &&
                Objects.equals(albumName, album.albumName) &&
                Objects.equals(genre, album.genre) &&
                Objects.equals(releaseDate, album.releaseDate);
    }

    /**
     * Converts album object to hash code
     * @return Hash code of object
     */
    @Override
    public int hashCode() {
        return Objects.hash(albumName, genre, releaseDate, totalSongLength);
    }

    /**
     * Converts album to string
     * @return String version of album
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Album{");
        sb.append("albumID=").append(albumID);
        sb.append(", albumName='").append(albumName).append('\'');
        sb.append(", genre='").append(genre).append('\'');
        sb.append(", releaseDate='").append(releaseDate).append('\'');
        sb.append(", totalSongLength=").append(totalSongLength);
        sb.append(", artists=").append(artists);
        sb.append('}');
        return sb.toString();
    }
}
