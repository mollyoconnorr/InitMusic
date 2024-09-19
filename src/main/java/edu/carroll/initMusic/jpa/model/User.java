package edu.carroll.initMusic.jpa.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


/**
 * <p>
 * This class is used to represent User objects, and maps to our database table.
 * </p>
 *
 * @author Nick Clouse
 *
 * @since September 11, 2024
 */
@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
public class User {
    /**
     * Serial version ID
     */
    private static final long serialVersionID = 1L;

    /**
     * User's id number, used as primary key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userID;

    /**
     * User's playlists, has a one-to-many relationship
     * with the playlist class.
     */
    @OneToMany(mappedBy = "author")
    private final Set<Playlist> playlists = new HashSet<>();

    /**
     * User's username
     */
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    /**
     * User's password
     */
    @Column(name = "password", nullable = false)
    private String hashedPassword;

    /**
     * User's first name
     */
    @Column(name = "user_first_name", nullable = false)
    private String firstName;

    /**
     * User's last name
     */
    @Column(name = "user_last_name", nullable = false)
    private String lastName;

    /**
     * User's email
     */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * User's country
     */
    @Column(name = "country", nullable = false)
    private String country;

    /**
     * Date user account was created.
     */
    @CreatedDate
    @Column(name = "account_creation_date", nullable = false)
    private LocalDateTime accountCreationDate;

    /**
     * JPA needs this constructor to instantiate entities when retrieving data from the database.
     * Its protected so it can't be used to create new Album objects by other classes.
     */
    public User(){
        //Default Constructor
    }

    /**
     * Creates a user user instance
     * @param username User's username
     * @param hashedPassword User's password, hashed
     * @param firstName User's first name
     * @param lastName User's last name
     * @param email User's email
     * @param country User's country
     */
    public User(String username, String hashedPassword, String firstName, String lastName, String email, String country) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.country = country;
    }

    /**
     * Gets all playlists the user has
     * @return Set of user's playlists
     */
    public Set<Playlist> getPlaylists() {
        return playlists;
    }

    /**
     * Get a playlist based off of string
     * @param playlistName Name of playlist to search for
     * @return Playlist if found, null if not.
     */
    public Playlist getPlaylist(String playlistName) {
        for(Playlist playlist : playlists) {
            if(playlist.getPlaylistName().equalsIgnoreCase(playlistName)){
                return playlist;
            }
        }
        return null;
    }

    /**
     * Adds a playlist to user's list of playlist
     * @param playlist Playlist to add
     */
    public void addPlaylist(final Playlist playlist) {
        this.playlists.add(playlist);
    }

    /**
     * Removes a playlist from User's playlists
     * @param playlist Playlist to remove
     */
    public void removePlaylist(final Playlist playlist) {
        this.playlists.remove(playlist);
    }

    /**
     * Gets the user's id
     * @return User's id
     */
    public Integer getuserID() {
        return userID;
    }

    /**
     * Sets the users id
     * @param userID ID to set
     */
    public void setuserID(Integer userID) {
        this.userID = userID;
    }

    /**
     * Gets user's username
     * @return username User's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the user's username
     * @param username Username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns user's hashed password
     * @return The hashed password
     */
    public String getHashedPassword() {
        return hashedPassword;
    }

    /**
     * Sets the user's password to hashed version
     * @param hashedPassword Password to set
     */
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    /**
     * Gets user's first name
     * @return User's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets user's first name
     * @param firstName First name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets user's last name
     * @return User's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets user's last name
     * @param lastName Last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets user's email
     * @return User's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets user's email
     * @param email Email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets user's country
     * @return User's country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets user's country
     * @param country Country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets the date the account was created
     * @return The date the user's account was created
     */
    public LocalDateTime getAccountCreationDate() {
        return accountCreationDate;
    }

    /**
     * Sets the user's account creation date
     * @param accountCreationDate Date to set
     */
    public void setAccountCreationDate(LocalDateTime accountCreationDate) {
        this.accountCreationDate = accountCreationDate;
    }

    /**
     * Compares user object with another user object
     * @param o object to compare with
     * @return boolean - If objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userID, user.userID) &&
                Objects.equals(username, user.username) &&
                Objects.equals(hashedPassword, user.hashedPassword) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(email, user.email) && Objects.equals(country, user.country);
    }

    /**
     * Converts object to hash code
     * @return Object as hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(userID, username, hashedPassword, firstName, lastName, email, country);
    }

    /**
     * Converts object to a string
     * @return String version of the object
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Login @ User{");
        sb.append("id=").append(userID);
        sb.append(", username='").append(username).append('\'');
        sb.append(", hashedPassword='").append("*****").append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append('}');
        return sb.toString();
    }
}