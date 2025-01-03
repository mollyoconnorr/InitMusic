package edu.carroll.initMusic.service.songManagement;

import edu.carroll.initMusic.MethodOutcome;
import edu.carroll.initMusic.jpa.model.Playlist;
import edu.carroll.initMusic.jpa.model.Song;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.jpa.repo.PlaylistRepository;
import edu.carroll.initMusic.jpa.repo.SongRepository;
import edu.carroll.initMusic.jpa.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Service class which interacts with playlists. Handles functions like
 * creating, renaming, and deleting playlists, and adding/removing songs from them.
 *
 * @see PlaylistService
 * @since October 23, 2024
 */
@Service
public class PlaylistServiceImpl implements PlaylistService {

    /** Logger object used for logging */
    private static final Logger log = LoggerFactory.getLogger(PlaylistServiceImpl.class);

    /** Maximum length a query can be */
    private static final int MAX_NAME_LENGTH = 50;

    /** Song repository */
    private final SongRepository songRepository;

    /** Playlist repository */
    private final PlaylistRepository playlistRepository;

    /** User repository */
    private final UserRepository userRepository;

    /**
     * Injects dependencies
     *
     * @param songRepository     Song Repository needed
     * @param playlistRepository Playlist Repository needed
     * @param userRepository     User Repository needed
     */
    public PlaylistServiceImpl(final SongRepository songRepository, final PlaylistRepository playlistRepository, final UserRepository userRepository) {
        this.songRepository = songRepository;
        this.playlistRepository = playlistRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new playlist for the given user with the given username. Returns true if the playlist
     * was created. It will false if the given user doesn't exist, the user already has a playlist
     * with the given name, or if the given name isn't valid (Is blank).
     *
     * @param name Name of new playlist
     * @param user User who created playlist
     * @return MethodOutcome Enum which corresponds to outcome of function
     */
    public MethodOutcome createPlaylist(String name, User user) {
        if (name == null) {
            log.warn("createPlaylist: Attempted to create a new playlist, but playlist was null.", user.getuserID());
            return MethodOutcome.PLAYLIST_NAME_INVALID;
        }
        //If user doesn't exist
        if (!userRepository.existsById(user.getuserID())) {
            log.warn("createPlaylist: Attempted to create a new playlist, but User id#{} doesn't exist", user.getuserID());
            return MethodOutcome.USER_NOT_FOUND;
        }

        name = name.strip();

        if (name.trim().isEmpty()) {
            log.warn("createPlaylist: Attempted to create a new playlist, but playlist name is null or empty.", user.getuserID());
            return MethodOutcome.PLAYLIST_NAME_INVALID;
        }

        if (name.length() > MAX_NAME_LENGTH) {
            log.warn("createPlaylist: Attempted to create a new playlist, but playlist name is to long.", user.getuserID());
            return MethodOutcome.PLAYLIST_NAME_INVALID; // Add a descriptive error if needed
        }

        if(name.isBlank()){
            log.warn("createPlaylist: Attempted to create a new playlist, but User id#{} tried to make a playlist with a blank String", user.getuserID());
            return MethodOutcome.PLAYLIST_NAME_INVALID;
        }

        if (user.getPlaylist(name) != null) {
            log.warn("createPlaylist: Attempted to create a duplicate playlist name '{}' for User id#{}", name, user.getuserID());
            return MethodOutcome.PLAYLIST_NAME_EXISTS;
        }

        final Playlist newPlaylist = new Playlist(user, name);
        playlistRepository.save(newPlaylist);
        user.addPlaylist(newPlaylist);

        log.info("createPlaylist: Playlist '{}' created for user '{}'", name, user.getuserID());

        return MethodOutcome.SUCCESS;
    }

    /**
     * This function handles renaming a playlist
     *
     * @param newName    New name of playlist
     * @param playlistID ID of playlist to rename
     * @param user       User who created playlist
     * @return MethodOutcome Enum which corresponds to outcome of function
     */
    public MethodOutcome renamePlaylist(String newName, Long playlistID, User user){
        if (playlistRepository.findByPlaylistIDEquals(playlistID).isEmpty()) {
            log.warn("renamePlaylist: Attempted to rename playlist, but playlist ID {} doesn't exist", playlistID);
            return MethodOutcome.PLAYLIST_NOT_FOUND;
        }
        if(user.getPlaylist(newName) != null){
            log.warn("renamePlaylist: Attempted to rename playlist, but a Playlist with name '{}' already exists for user id#{}", newName, user.getuserID());
            return MethodOutcome.PLAYLIST_NAME_EXISTS;
        }

        //If user doesn't exist
        if (!userRepository.existsById(user.getuserID())) {
            log.warn("renamePlaylist: Attempted to rename playlist, but User id#{} doesn't exist", user.getuserID());
            return MethodOutcome.USER_NOT_FOUND;
        }

        if (newName == null) {
            log.warn("renamePlaylist: Attempted to rename playlist, but User id#{} tried to rename playlist null", user.getuserID());
            return MethodOutcome.PLAYLIST_NAME_INVALID;
        }

        //If string is empty or blank
        if(newName.isBlank()) {
            log.warn("renamePlaylist: Attempted to rename playlist, but User id#{} tried to rename playlist with a blank String", user.getuserID());
            return MethodOutcome.PLAYLIST_NAME_INVALID;
        }

        //If string is extremely long
        if (newName.length() > MAX_NAME_LENGTH) {
            log.warn("renamePlaylist: Attempted to rename playlist, but User id#{} tried to rename playlist with to many characters", user.getuserID());
            return MethodOutcome.PLAYLIST_NAME_INVALID;
        }

        //Look through each playlist, faster to do in-memory then pull playlist from repository
        for (Playlist playlist : user.getPlaylists()) {
            if (Objects.equals(playlist.getPlaylistID(), playlistID)) {
                playlist.setPlaylistName(newName);
                playlistRepository.save(playlist);
                log.info("renamePlaylist: Playlist with id '{}' renamed to '{}'", playlistID, newName);
                return MethodOutcome.SUCCESS;
            }
        }
        return MethodOutcome.PLAYLIST_RENAME_ERROR;
    }

    /**
     * This function handles deleting a song from a playlist.
     *
     * @param playlistName Name of playlist to delete
     * @param playlistID   ID of playlist to delete
     * @param user         User who created playlist
     * @return MethodOutcome Enum which corresponds to outcome of function
     */
    public MethodOutcome deletePlaylist(String playlistName, Long playlistID, User user){
        if (playlistName == null || playlistName.isBlank() || playlistName.length() > MAX_NAME_LENGTH){
            log.warn("deletePlaylist: Attempted to delete playlist, but playlist name invalid");
            return MethodOutcome.PLAYLIST_NAME_INVALID;
        }
        //If user doesn't exist
        if (!userRepository.existsById(user.getuserID())) {
            log.warn("deletePlaylist: Attempted to delete playlist, but User id#{} doesn't exist", user.getuserID());
            return MethodOutcome.USER_NOT_FOUND;
        }

        //If playlist isn't found for given user.
        if (user.getPlaylist(playlistName) == null) {
            log.warn("deletePlaylist: Attempted to delete playlist, but User id#{} doesn't have a playlist with name '{}', id#{}", user.getuserID(), playlistName, playlistID);
            return MethodOutcome.PLAYLIST_NOT_FOUND;
        }
        playlistRepository.delete(user.getPlaylist(playlistName));
        user.removePlaylist(user.getPlaylist(playlistName));
        log.info("deletePlaylist: Playlist '{}' deleted for user id#{}", playlistName, user.getuserID());
        userRepository.save(user);
        return MethodOutcome.SUCCESS;
    }

    /**
     * Gets the playlist object with the given id
     *
     * @param playlistID ID to search by
     * @return The playlist object found, if any
     */
    public Playlist getPlaylist(Long playlistID) {
        final List<Playlist> playlistsFound = playlistRepository.findByPlaylistIDEquals(playlistID);

        if(playlistsFound.size() != 1){
            log.warn("getPlaylist: Attempted to get playlist, but playlist wasn't found.");
            return null;
        }
        log.info("getPlaylist: Retrieved playlist {}", playlistsFound.getFirst());
        return playlistsFound.getFirst();
    }

    /**
     * Removes a song from a playlist based off their respective ID's
     *
     * @param playlistID ID of playlist
     * @param songID     ID of song
     * @return MethodOutcome Enum which corresponds to outcome of function
     */
    public MethodOutcome removeSongFromPlaylist(Long playlistID, Long songID){
        if (playlistID == null){
            log.warn("removeSongFromPlaylist: Attempted to remove a song from a playlist, but playlist id was null");
            return MethodOutcome.PLAYLIST_NAME_INVALID;
        }

        if (songID == null){
            log.warn("removeSongFromPlaylist: Attempted to remove a song from a playlist, but song id was null");
            return MethodOutcome.INVALID_SONG;
        }

        final List<Playlist> playlistsFound = playlistRepository.findByPlaylistIDEquals(playlistID);

        //If there was 0 or more than 1 playlist found
        if (playlistsFound.size() != 1) {
            log.warn("removeSongFromPlaylist: Playlist id#{} not found", playlistID);
            return MethodOutcome.PLAYLIST_NOT_FOUND;
        }

        final Playlist playlist = playlistsFound.getFirst();

        final boolean songRemoved = playlist.removeSong(songID);

        //If song wasn't removed
        if (!songRemoved) {
            log.warn("removeSongFromPlaylist: Error when removing song id#{} from playlist id#{} using removeSong(songID)", songID, playlistID);
            return MethodOutcome.SONG_NOT_IN_PLAYLIST;
        }

        playlistRepository.save(playlist);

        log.info("removeSongFromPlaylist: Song id#{} successfully removed from playlist id#{}", songID, playlistID);

        return MethodOutcome.SUCCESS;
    }

    /**
     * Adds a song to the given playlist. This first searches for the playlist by id. It
     * should always find a playlist, because when used, it takes the playlist id directly from
     * a playlist object that has already been created.
     *
     * @param playlist playlist to add song to
     * @param song     Song to add to playlist
     * @return MethodOutcome, the outcome of the method
     */
    public MethodOutcome addSongToPlaylist(Playlist playlist, Song song) {
        if (song == null) {
            log.warn("addSongToPlaylist: Song was null.");
            return MethodOutcome.INVALID_SONG;
        }
        if (playlist == null) {
            log.warn("addSongToPlaylist: Playlist was null.");
            return MethodOutcome.PLAYLIST_NOT_FOUND;
        }

        if (playlistRepository.findByPlaylistIDEquals(playlist.getPlaylistID()).isEmpty()) {
            log.warn("addSongToPlaylist: Playlist id#{} does not exist", playlist.getPlaylistID());
            return MethodOutcome.PLAYLIST_NOT_FOUND; // Playlist does not exist
        }

        //Check if the song is already in the playlist
        if (playlist.containsSong(song)) {
            log.warn("addSongToPlaylist: Playlist id#{} by user id#{} already contains song#{}", playlist.getPlaylistID(), playlist.getAuthor().getuserID(), song.getDeezerID());
            return MethodOutcome.PLAYLIST_ALREADY_CONTAINS_SONG; //Song is already in the playlist
        }

        //Attempt to find the song in the repository
        final Optional<Song> songFound = songRepository.findById(song.getDeezerID());
        if (songFound.isPresent()) {
            log.warn("addSongToPlaylist: Playlist id#{} by user id#{} already contains song#{}", playlist.getPlaylistID(), playlist.getAuthor().getuserID(), song.getDeezerID());            //Add the managed song to the playlist
            playlist.addSong(songFound.get());
            songFound.get().addPlaylist(playlist);
        } else {
            log.info("addSongToPlaylist: Saving new song#{} to the database and adding it to playlist id#{}", song.getDeezerID(), playlist.getPlaylistID());
            //If the song does not exist, save it
            songRepository.save(song);
            playlist.addSong(song);
            song.addPlaylist(playlist);
        }
        //Save only the playlist, which will cascade the updates
        playlistRepository.save(playlist);

        log.info("addSongToPlaylist: Song id#{} added to playlist id#{} by user id#{}", song.getDeezerID(), playlist.getPlaylistID(), playlist.getAuthor().getuserID());
        return MethodOutcome.SUCCESS;
    }

    /**
     * Retrieves the number of playlists currently stored in the playlist repository.
     * @return the size of the playlist repository.
     */
    public long getRepoSize() {
        long size = playlistRepository.count();
        log.info("getRepoSize: Playlist repository size is {}", size);
        return size;
    }

    /**
     * Clears all playlists from the repository.
     * This is primarily useful for testing or resetting data.
     */
    public void clearRepo() {
        playlistRepository.deleteAll();
        log.info("clearRepo: All playlists have been cleared from the repository");
    }
}
