package edu.carroll.initMusic.jpa.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


/**
 * <p>
 * This class is used to represent User objects, and maps to our database table.
 * </p>
 * <p>
 * The table is called users, instead of user, because 'user' is a reserved word in h2, and we could not use
 * a in memory database to test our program without changing the name.
 * </p>
 *
 * @author Nick Clouse
 * @since September 11, 2024
 */
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {
    /**
     * Serial version ID
     */
    private static final long serialVersionID = 3L;
    /**
     * User's playlists, has a one-to-many relationship
     * with the playlist class.
     */
    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final Set<Playlist> playlists = new HashSet<>();
    /**
     * User's id number, used as primary key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;
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

    /* User's Security Question1 */
    @Column(name = "question1")
    private String question1;

    /**
     * User's Security Question2
     */
    @Column(name = "question2")
    private String question2;

    /**
     * User's Security Answer1
     */
    @Column(name = "answer1")
    private String answer1;

    /**
     * User's Security Answer2
     */
    @Column(name = "answer2")
    private String answer2;

    /**
     * Date user account was created
     */
    @CreatedDate
    @Column(name = "account_creation_date", nullable = false)
    private LocalDateTime accountCreationDate;

    /**
     * JPA needs this default constructor to instantiate entities when retrieving data from the database.
     */
    public User() {
        //Default Constructor
    }

    /**
     * Creates a user instance
     *
     * @param username       User's username
     * @param hashedPassword User's password, hashed
     * @param firstName      User's first name
     * @param lastName       User's last name
     * @param email          User's email
     * @param question1      User's first security question
     * @param question2      User's second security question
     * @param answer1        User's answer to first security question
     * @param answer2        User's answer second security question
     */
    public User(String username, String hashedPassword, String firstName, String lastName, String email,
                String question1, String question2, String answer1, String answer2) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.question1 = question1;
        this.question2 = question2;
        this.answer1 = answer1;
        this.answer2 = answer2;
    }

    /**
     * Gets all playlists the user has
     *
     * @return Set of user's playlists
     */
    public Set<Playlist> getPlaylists() {
        return playlists;
    }

    /**
     * Get a playlist based off of its name
     *
     * @param playlistName Name of playlist to search for
     * @return Playlist if found, null if not.
     */
    public Playlist getPlaylist(String playlistName) {
        for (Playlist playlist : playlists) {
            if (playlist.getPlaylistName().equalsIgnoreCase(playlistName)) {
                return playlist;
            }
        }
        return null;
    }

    /**
     * Adds a playlist to user's list of playlist
     *
     * @param playlist Playlist to add
     */
    public void addPlaylist(final Playlist playlist) {
        this.playlists.add(playlist);
    }

    /**
     * Removes a playlist from User's playlists
     *
     * @param playlist Playlist to remove
     */
    public void removePlaylist(final Playlist playlist) {
        this.playlists.remove(playlist);
    }

    /**
     * Gets the user's id
     *
     * @return User's id
     */
    public Long getuserID() {
        return userID;
    }

    /**
     * Sets the users id
     *
     * @param userID ID to set
     */
    public void setuserID(Long userID) {
        this.userID = userID;
    }

    /**
     * Gets user's username
     *
     * @return username User's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the user's username
     *
     * @param username Username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns user's hashed password
     *
     * @return The hashed password
     */
    public String getHashedPassword() {
        return hashedPassword;
    }

    /**
     * Sets the user's password to hashed version
     *
     * @param hashedPassword Password to set
     */
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    /**
     * Gets user's first name
     *
     * @return User's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets user's first name
     *
     * @param firstName First name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets user's last name
     *
     * @return User's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets user's last name
     *
     * @param lastName Last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets user's email
     *
     * @return User's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets user's email
     *
     * @param email Email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets user's first security question
     *
     * @return User's first security question
     */
    public String getQuestion1() {
        return question1;
    }

    /**
     * Sets user's first security question
     *
     * @param question1 question to set
     */
    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    /**
     * Gets user's second security question
     *
     * @return User's second security question
     */
    public String getQuestion2() {
        return question2;
    }

    /**
     * Sets user's second security question
     *
     * @param question2 question to set
     */
    public void setQuestion2(String question2) {
        this.question2 = question2;
    }

    /**
     * Gets user's first security question answer
     *
     * @return User's first security question answer
     */
    public String getAnswer1() {
        return answer1;
    }

    /**
     * Sets user's answer to first security question
     *
     * @param answer1 answer to question1
     */
    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    /**
     * Gets user's second security question answer
     *
     * @return User's scond security question answer
     */
    public String getAnswer2() {
        return answer2;
    }

    /**
     * Sets user's answer to second security question
     *
     * @param answer2 answer to question1
     */
    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    /**
     * Gets the date the account was created
     *
     * @return The date the user's account was created
     */
    public LocalDateTime getAccountCreationDate() {
        return accountCreationDate;
    }

    /**
     * Sets the user's account creation date
     *
     * @param accountCreationDate Date to set
     */
    public void setAccountCreationDate(LocalDateTime accountCreationDate) {
        this.accountCreationDate = accountCreationDate;
    }

    /**
     * Compares user object with another user object
     *
     * @param o object to compare with
     * @return boolean - If objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(hashedPassword, user.hashedPassword) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(email, user.email) &&
                Objects.equals(question1, user.question1) &&
                Objects.equals(question2, user.question2) &&
                Objects.equals(answer1, user.answer1) &&
                Objects.equals(answer2, user.answer2);
    }

    /**
     * Converts object to hash code
     *
     * @return Object as hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(userID, username, hashedPassword, firstName, lastName, email);
    }

    /**
     * Converts object to a string
     *
     * @return String version of the object
     */
    @Override
    public String toString() {
        String sb = "Login @ User{" + "id=" + userID +
                ", username='" + username + '\'' +
                ", hashedPassword='" + "*****" + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
        return sb;
    }
}