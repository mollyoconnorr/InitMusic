package edu.carroll.initMusic.jpa.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;

/**
 * <p>
 * This class is used to represent artist objects, and maps to our database table.
 * </p>
 *
 * @author Nick Clouse
 *
 * @since September 11, 2024
 */
@Entity
@Table(name = "artist")
public class Artist {
    /**
     * Serial version ID
     */
    private static final long serialVersionID = 1L;

    /**
     * ID number for artist. Automatically generated
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer artistID;

    /**
     * Keeps track of artists albums.
     * Many-to-Many relationship with Album class, because one
     * Album could have multiple artists and one artist can have
     * multiple albums
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "artist_album",
            joinColumns = { @JoinColumn(name = "artistID") },
            inverseJoinColumns = { @JoinColumn(name = "albumID")}
    )
    private final Set<Album> albums = new HashSet<>();

    /**
     * Keeps track of artists songs.
     * Many-to-Many relationship with Song class, because one
     * Song could have multiple artists and one artist can have
     * multiple songs
     */
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinTable(
            name = "artist_song",
            joinColumns = { @JoinColumn(name = "artistID") },
            inverseJoinColumns = { @JoinColumn(name = "songID")}
    )
    private Set<Song> songs = new HashSet<>();

    /**
     * First name of artist
     */
    @Column(name = "artist_name", nullable = false)
    private String artistName;

    /**
     * JPA needs this constructor to instantiate entities when retrieving data from the database.
     * Its protected so it can't be used to create new Artist objects by other classes.
     */
    protected Artist() {
        //Default Constructor
    }

    /**
     * Creates new artist with given params
     * @param artistName Artists name
     */
    public Artist(String artistName) {
        this.artistName = artistName;
    }

    /**
     * Gets songs this artist created
     * @return Set of songs
     */
    public Set<Song> getSongs() {
        return songs;
    }

    /**
     * Sets songs to new set of songs
     * @param songs Song set to set
     */
    public void setSongs(Set<Song> songs) {
        this.songs = songs;
    }

    /**
     * Adds a new song to song set
     * @param song Song to add
     */
    public void addSong(Song song) {
        this.songs.add(song);
    }

    /**
     * Removes a song from artist
     * @param song Song to remove
     */
    public void removeSong(Song song) {
        this.songs.remove(song);
    }

    /**
     * Check if artist created a song
     * @param song Song to check
     * @return If artist created song, false otherwise
     */
    public boolean createdSong(Song song) {
        return this.songs.contains(song);
    }

    /**
     * Gets number of albums artist has
     * @return Number of albums
     */
    public int getNumberOfAlbums() {
        return albums.size();
    }

    /**
     * Gets number of songs artist has made
     * @return Number of songs
     */
    public int getNumberOfSongs() {
        return this.songs.size();
    }

    /**
     * Checks if artist created album
     * @param a Album to check for
     * @return If artist created album, false otherwise
     */
    public boolean createdAlbum(Album a) {
        return this.albums.contains(a);
    }

    /**
     * Adds album to artist
     * @param album Album to add
     */
    public void addAlbum(Album album) {
        this.albums.add(album);
        album.getArtists().add(this); // Keep the relationship bidirectional
    }

    /**
     * Removes album from artist
     * @param album Album to remove
     */
    public void removeAlbum(Album album) {
        this.albums.remove(album);
        album.getArtists().remove(this);
    }

    /**
     * Gets all albums owned by artist
     * @return Set of albums
     */
    public Set<Album> getAlbums() {
        return albums;
    }

    /**
     * Gets ID of artist
     * @return ID of artist
     */
    public Integer getArtistID() {
        return artistID;
    }

    /**
     * Set ID of artist
     * @param artistID ID toset
     */
    public void setArtistID(Integer artistID) {
        this.artistID = artistID;
    }

    /**
     * Gets artists first name
     * @return Artist's first name
     */
    public String getArtistName() {
        return artistName;
    }

    /**
     * Sets artist's first name
     * @param artistName First name to set
     */
    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    /**
     * Compares Artist object with another artist object
     * @param o Artist object to compare with
     * @return If objects are equal or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return Objects.equals(artistName, artist.getArtistName());
    }

    /**
     * Converts object to hash code
     * @return Artist object in hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(artistName);
    }

    /**
     * Converts artist to string
     * @return String version of artist
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Artist{");
        sb.append("artistID=").append(artistID);
        sb.append(", artistName='").append(artistName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
