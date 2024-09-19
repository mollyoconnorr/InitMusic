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
    @ManyToMany(cascade = {CascadeType.ALL})
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
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "artist_song",
            joinColumns = { @JoinColumn(name = "artistID") },
            inverseJoinColumns = { @JoinColumn(name = "songID")}
    )
    private final Set<Song> songs = new HashSet<>();

    /**
     * First name of artist
     */
    @Column(name = "artist_first_name", nullable = false)
    private String artistFirstName;

    /**
     * Last name of artist.
     * Can be nullable if artist goes by one name,
     * like Drake, Marshmello, etc.
     */
    @Column(name = "artist_last_name", nullable = true)
    private String artistLastName;

    /**
     * Country artist is based in
     */
    @Column(name = "country", nullable = false)
    private String country;

    /**
     * JPA needs this constructor to instantiate entities when retrieving data from the database.
     * Its protected so it can't be used to create new Artist objects by other classes.
     */
    protected Artist() {
        //Default Constructor
    }

    /**
     * Creates new artist with given params
     * @param artistFirstName Artists first name
     * @param artistLastName Artists last name
     * @param country Country artist is based in
     */
    public Artist(String artistFirstName, String artistLastName, String country) {
        this.artistFirstName = artistFirstName;
        this.artistLastName = artistLastName;
        this.country = country;
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
    public String getArtistFirstName() {
        return artistFirstName;
    }

    /**
     * Sets artist's first name
     * @param artistFirstName First name to set
     */
    public void setArtistFirstName(String artistFirstName) {
        this.artistFirstName = artistFirstName;
    }

    /**
     * Gets artist's last name
     * @return Artist's last name
     */
    public String getArtistLastName() {
        return artistLastName;
    }

    /**
     * Sets the artist's last name
     * @param artistLastName Last name to set
     */
    public void setArtistLastName(String artistLastName) {
        this.artistLastName = artistLastName;
    }

    /**
     * Gets country of artist
     * @return The country artists is based in
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets artist's country
     * @param country Country to set
     */
    public void setCountry(String country) {
        this.country = country;
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
        return Objects.equals(artistID, artist.artistID) &&
                Objects.equals(artistFirstName, artist.artistFirstName) &&
                Objects.equals(artistLastName, artist.artistLastName) &&
                Objects.equals(country, artist.country);
    }

    /**
     * Converts object to hash code
     * @return Artist object in hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(artistID, artistFirstName, artistLastName, country);
    }

    /**
     * Converts artist to string
     * @return String version of artist
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Artist{");
        sb.append("artistID=").append(artistID);
        sb.append(", artistFirstName='").append(artistFirstName).append('\'');
        sb.append(", artistLastName='").append(artistLastName).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
