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
    private Set<Artist> artists = new HashSet<>();


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
     * Total length of album, which is the sum of the length of all songs in album
     */
    @Column(name = "total_song_length", nullable = false)
    private int totalSongLength;

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
     * @param totalSongLength Total runtime of all songs
     */
    public Album(String albumName, String genre, String releaseDate, int totalSongLength) {
        this.albumName = albumName;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.totalSongLength = totalSongLength;
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
     * Adds song to album
     * @param song Song to add
     */
    public void addSong(Song song) {
        this.songs.add(song);
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
        return totalSongLength == album.totalSongLength &&
                Objects.equals(albumID, album.albumID) &&
                Objects.equals(songs, album.songs) &&
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
        return Objects.hash(albumID, songs, albumName, genre, releaseDate, totalSongLength);
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
        sb.append('}');
        return sb.toString();
    }
}
