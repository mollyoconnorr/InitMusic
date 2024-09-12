package edu.carroll.initMusic.jpa.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;

/**
 * <p>
 * This class is used to represent Playlist objects, and maps to our database table.
 * A playlist is a collection of songs created by a user.
 * </p>
 *
 * @author Nick Clouse
 *
 * @since September 11, 2024
 */
@Entity
@Table(name = "playlist")
public class Playlist {
    /**
     * Serial version ID
     */
    private static final long serialVersionID = 1L;

    /**
     * Playlist's id number, used as primary key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer playlist_id;

    /**
     * ID of author who made playlist. A Many to one relationship.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "playlist_song",
            joinColumns = { @JoinColumn(name = "playlist_id") },
            inverseJoinColumns = { @JoinColumn(name = "song_id")}
    )
    private final Set<Song> songs = new HashSet<>();

    /**
     * Name of playlist
     */
    @Column(name= "playlist_name",nullable = false, unique = true)
    private String playlistName;

    /**
     * Date playlist was created.
     * Has length of 10 for MM/DD/YYYY format
     */
    @Column(name = "date_created", nullable = false, length = 10)
    private String dateCreated;

    /**
     * Number of songs in playlist
     */
    @Column(name = "number_of_songs", nullable = false)
    private int numberOfSongs;

    /**
     * Total length of all songs in playlist in minutes
     */
    @Column(name = "total_song_length", nullable = false)
    private int totalSongLength;

    /**
     * Gets all songs in playlist
     * @return Set of songs
     */
    public Set<Song> getSongs() {
        return songs;
    }

    /**
     * Add song to playlist
     * @param song Song to add
     */
    public void addSong(Song song) {
        this.songs.add(song);
    }

    /**
     * Remove a song from playlist
     * @param song Song to remove
     * @return If song was removed or not
     */
    public boolean removeSong(Song song) {
        return this.songs.remove(song);
    }

    /**
     * Gets the ID of the playlist
     * @return The playlist's ID
     */
    public Integer getPlaylist_id() {
        return playlist_id;
    }

    /**
     * Sets the ID of the playlist
     * @param playlist_id The id to set
     */
    public void setPlaylist_id(Integer playlist_id) {
        this.playlist_id = playlist_id;
    }

    /**
     * Gets the author who owns the playlist
     * @return The author object
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Sets the author of the playlist to parameter passed.
     * @param author Author to set
     */
    public void setAuthor(User author) {
        this.author = author;
    }

    /**
     * Gets the playlist's name
     * @return The playlist's name
     */
    public String getPlaylistName() {
        return playlistName;
    }

    /**
     * Sets playlist name
     * @param playlistName Name to set
     */
    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    /**
     * Gets the date the playlist was created
     * @return The creation Date
     */
    public String getDateCreated() {
        return dateCreated;
    }

    /**
     * Sets the playlist's creation date
     * @param dateCreated Date to set
     */
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * Gets the number of songs in the playlist
     * @return The number of songs in the playlist
     */
    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    /**
     * Sets the number of songs in playlist
     * @param numberOfSongs Number of songs to set
     */
    public void setNumberOfSongs(int numberOfSongs) {
        this.numberOfSongs = numberOfSongs;
    }

    /**
     * Gets the total length of the playlist, which is the combined length
     * of all songs in the playlist in minutes
     * @return The total length of the playlist
     */
    public int getTotalSongLength() {
        return totalSongLength;
    }

    /**
     * Sets the total length of the playlist
     * @param totalSongLength Length to set
     */
    public void setTotalSongLength(int totalSongLength) {
        this.totalSongLength = totalSongLength;
    }

    /**
     * Compares Playlist object with another
     * @param o Object to compare
     * @return If playlists are equal or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Playlist playlist = (Playlist) o;
        return numberOfSongs == playlist.numberOfSongs &&
                totalSongLength == playlist.totalSongLength &&
                Objects.equals(playlist_id, playlist.playlist_id) &&
                Objects.equals(author, playlist.author) &&
                Objects.equals(playlistName, playlist.playlistName) &&
                Objects.equals(dateCreated, playlist.dateCreated);
    }

    /**
     * Converts object to hash code
     * @return Hash code of object
     */
    @Override
    public int hashCode() {
        return Objects.hash(playlist_id, author, playlistName, dateCreated, numberOfSongs, totalSongLength);
    }

    /**
     * Converts playlist to string
     * @return String version of playlist
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Playlist{");
        sb.append("playlist_id=").append(playlist_id);
        sb.append(", author=").append(author);
        sb.append(", playlistName='").append(playlistName).append('\'');
        sb.append(", dateCreated='").append(dateCreated).append('\'');
        sb.append(", numberOfSongs=").append(numberOfSongs);
        sb.append(", totalSongLength=").append(totalSongLength);
        sb.append('}');
        return sb.toString();
    }
}
