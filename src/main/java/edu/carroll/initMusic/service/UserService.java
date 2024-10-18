package edu.carroll.initMusic.service;

import edu.carroll.initMusic.ResponseStatus;
import edu.carroll.initMusic.jpa.model.Playlist;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.jpa.repo.PlaylistRepository;
import edu.carroll.initMusic.jpa.repo.UserRepository;
import edu.carroll.initMusic.web.form.RegistrationForm;
import edu.carroll.initMusic.web.form.SecurityQuestionsForm;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * Service class for handling user-related operations.
 * <p>
 * This class provides methods for saving user information, checking unique usernames/emails,
 * updating security questions, and managing passwords.
 * </p>
 */
@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    /**
     * User repository for interacting with the user database.
     */
    private final UserRepository userRepository;

    /**
     * BCrypt password encoder used for hashing passwords.
     */
    private final BCryptPasswordEncoder passwordEncoder;


    private final PlaylistRepository playlistRepository;

    /**
     * Constructor to initialize the UserService with the required dependencies.
     *
     * @param userRepository   the repository for interacting with the user data
     * @param passwordEncoder  the encoder used to hash passwords
     */
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, PlaylistRepository playlistRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.playlistRepository = playlistRepository;
    }

    /**
     * Checks whether the provided username is unique (i.e., not already present in the database).
     *
     * @param username the username to check for uniqueness
     * @return true if the username is unique, false otherwise
     */
    public boolean uniqueUserName(String username) {
        log.info("Checking if username '{}' is unique", username);
        List<User> usersByName = userRepository.findByUsernameIgnoreCase(username);

        if (!usersByName.isEmpty()) {
            log.info("Username '{}' already exists", username);
            return false;
        } else {
            log.info("Username '{}' is available", username);
            return true;
        }
    }

    /**
     * Checks whether the provided email is unique (i.e., not already present in the database).
     *
     * @param email the email to check for uniqueness
     * @return true if the email is unique, false otherwise
     */
    public boolean uniqueEmail(String email) {
        log.info("Checking if email '{}' is unique", email);
        List<User> usersByEmail = userRepository.findByEmailIgnoreCase(email);

        if (!usersByEmail.isEmpty()) {
            log.info("Email '{}' already exists", email);
            return false;
        } else {
            log.info("Email '{}' is available", email);
            return true;
        }
    }

    /**
     * Saves a new user based on the provided registration form data.
     *
     * @param registrationForm the form containing user registration details
     * @return the saved {@link User} object
     */
    public User saveUser(RegistrationForm registrationForm) {
        log.info("Saving new user with username '{}'", registrationForm.getUsername());
        User newUser = new User();  // Create a new User object inside the method
        newUser.setUsername(registrationForm.getUsername());
        newUser.setEmail(registrationForm.getEmail());
        // Validate that password is not null
        String hashedPassword = passwordEncoder.encode(registrationForm.getPassword());
        newUser.setHashedPassword(hashedPassword);
        log.info("Password for user '{}' has been hashed", newUser.getUsername());

        newUser.setFirstName(registrationForm.getFirstName());
        newUser.setLastName(registrationForm.getLastName());
        newUser.setQuestion1("null");
        newUser.setAnswer1("null");
        newUser.setQuestion2("null");
        newUser.setAnswer2("null");

        log.info("User '{}' saved with email '{}'", newUser.getUsername(), newUser.getEmail());
        return userRepository.save(newUser);
    }

    /**
     * Updates the security questions and answers for the specified user.
     *
     * @param user         the user whose security questions are being updated
     * @param questionForm the form containing the new security questions and answers
     */
    public void updateUser(User user, SecurityQuestionsForm questionForm) {
        log.info("Updating security questions for user '{}'", user.getUsername());
        user.setQuestion1(questionForm.getQuestion1());
        user.setAnswer1(questionForm.getAnswer1());
        user.setQuestion2(questionForm.getQuestion2());
        user.setAnswer2(questionForm.getAnswer2());
        userRepository.save(user); // Save the user with updated security questions to the database
        log.info("Security questions updated for user '{}'", user.getUsername());
    }

    /**
     * Finds a user by email address, ignoring case sensitivity.
     *
     * @param email the email address to search for
     * @return the user if found, otherwise null
     */
    public User findByEmail(String email) {
        log.info("Finding user by email '{}'", email);
        List<User> users = userRepository.findByEmailIgnoreCase(email);
        User foundUser = users.isEmpty() ? null : users.getFirst();  // Return the first user or null if none found

        if (foundUser != null) {
            log.info("User found with email '{}'", email);
        } else {
            log.info("No user found with email '{}'", email);
        }
        return foundUser;
    }

    /**
     * Updates the password for the specified user.
     *
     * @param user        the user whose password is being updated
     * @param newPassword the new password to be hashed and saved
     */
    public void updatePassword(User user, String newPassword) {
        log.info("Updating password for user '{}'", user.getUsername());
        user.setHashedPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user); // Save the user with updated password to the database
        log.info("Password updated for user '{}'", user.getUsername());
    }

    public User findByIdWithPlaylists(Long userId) {
        return userRepository.findByIdWithPlaylists(userId);
    }

    /**
     * Gets user object from inputted username. All usernames are unique, so
     * there should only be 1 username found
     * @param username Username to use for search
     * @return User object found, null if nothing found, or too many were found.
     */
    public User getUser(String username){
        final List<User> user = userRepository.findByUsernameIgnoreCase(username);
        if(user.size() != 1){
            return null;
        }
        return user.getFirst();
    }

    /**
     * Creates a new playlist for the given user with the given username. Returns true if the playlist
     * was created. It will false if the given user doesn't exist, the user already has a playlist
     * with the given name, or if the given name isn't valid (Is blank).
     * @param name Name of new playlist
     * @param user User who created playlist
     * @return ResponseStatus Enum which corresponds to outcome of function
     */
    public ResponseStatus createPlaylist(String name, User user){
        //If user doesn't exist
        if(!userRepository.existsById(user.getuserID())){
            log.warn("Attempted to create a new playlist, but User {} doesn't exist", user.getuserID());
            return ResponseStatus.USER_NOT_FOUND;
        }

        //If user already has a playlist with the same name
        if(user.getPlaylist(name) != null){
            log.warn("Attempted to create a new playlist, but Playlist '{}' already exists for user {}", name, user.getuserID());
            return ResponseStatus.PLAYLIST_NAME_EXISTS;
        }

        //If string is empty or blank
        if(name.isBlank()){
            log.warn("Attempted to create a new playlist, but User {} tried to make a playlist with a blank String", user.getuserID());
            return ResponseStatus.PLAYLIST_NAME_EMPTY;
        }

        name = name.strip();

        final Playlist newPlaylist = new Playlist(user,name);
        playlistRepository.save(newPlaylist);
        user.addPlaylist(newPlaylist);

        log.info("Playlist '{}' created for user '{}'", name, user.getuserID());

        return ResponseStatus.SUCCESS;
    }

    /**
     * This function handles renaming a playlist
     * @param newName New name of playlist
     * @param playlistID ID of playlist to rename
     * @param user User who created playlist
     * @return ResponseStatus Enum which corresponds to outcome of function
     */
    public ResponseStatus renamePlaylist(String newName, Long playlistID, User user){
        //If user already has playlist with same name
        if(user.getPlaylist(newName) != null){
            log.warn("Attempted to rename playlist, but a Playlist with name '{}' already exists for user '{}'", newName, user.getuserID());
            return ResponseStatus.PLAYLIST_NAME_EXISTS;
        }

        //If user doesn't exist
        if(!userRepository.existsById(user.getuserID())){
            log.warn("Attempted to rename playlist, but User {} doesn't exist", user.getuserID());
            return ResponseStatus.USER_NOT_FOUND;
        }

        //If string is empty or blank
        if(newName.isBlank()){
            log.warn("Attempted to rename playlist, but User {} tried to rename playlist with a blank String", user.getuserID());
            return ResponseStatus.PLAYLIST_NAME_EMPTY;
        }

        //Look through each playlist, faster to do in-memory then pull playlist from repository
        for(Playlist playlist : user.getPlaylists()){
            if(Objects.equals(playlist.getPlaylistID(), playlistID)){
                playlist.setPlaylistName(newName);
                playlistRepository.save(playlist);
                log.info("Playlist with id '{}' renamed to '{}'", playlistID, newName);
                return ResponseStatus.SUCCESS;
            }
        }

        return ResponseStatus.PLAYLIST_RENAME_ERROR;
    }

    /**
     * This function handles deleting a song from a playlist.
     * @param playlistName Name of playlist to delete
     * @param playlistID ID of playlist to delete
     * @param user User who created playlist
     * @return ResponseStatus Enum which corresponds to outcome of function
     */
    public ResponseStatus deletePlaylist(String playlistName, Long playlistID, User user){
        //If user doesn't exist
        if(!userRepository.existsById(user.getuserID())){
            log.warn("Attempted to delete playlist, but User {} doesn't exist", user.getuserID());
            return ResponseStatus.USER_NOT_FOUND;
        }

        //If playlist isn't found for given user.
        if(user.getPlaylist(playlistName) == null){
            log.warn("Attempted to delete playlist, but User {} doesn't have a playlist with name '{}', id#{}", user.getuserID(), playlistName, playlistID);
            return ResponseStatus.PLAYLIST_NOT_FOUND;
        }

        playlistRepository.delete(user.getPlaylist(playlistName));
        user.removePlaylist(user.getPlaylist(playlistName));
        log.info("Playlist '{}' deleted for user '{}'", playlistName, user.getuserID());

        userRepository.save(user);

        return ResponseStatus.SUCCESS;
    }

    public Playlist getPlaylist(Long playlistID){
        List<Playlist> playlistsFound = playlistRepository.findByPlaylistIDEquals(playlistID);

        if(playlistsFound.size() != 1){
            return null;
        }

        return playlistsFound.getFirst();
    }

    /**
     * Removes a song from a playlist based off their respective ID's
     * @param playlistID ID of playlist
     * @param songID ID of song
     * @return ResponseStatus Enum which corresponds to outcome of function
     */
    public ResponseStatus removeSongFromPlaylist(Long playlistID, Long songID){
        List<Playlist> playlistsFound = playlistRepository.findByPlaylistIDEquals(playlistID);

        //If there was 0 or more than 1 playlist found
        if(playlistsFound.size() != 1){
            return ResponseStatus.PLAYLIST_NOT_FOUND;
        }
        final Playlist playlist = playlistsFound.getFirst();

        final boolean songRemoved = playlist.removeSong(songID);

        //If song wasn't removed
        if(!songRemoved){
            return ResponseStatus.SONG_NOT_IN_PLAYLIST;
        }

        return ResponseStatus.SUCCESS;
    }
}
