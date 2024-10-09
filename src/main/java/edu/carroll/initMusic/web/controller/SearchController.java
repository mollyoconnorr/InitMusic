package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.jpa.model.Song;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.service.SongService;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * This Controller handles the search page, which is where users can
 * search for songs and add them to playlists
 * </p>
 *
 * @author Nick Clouse
 *
 * @since October 2, 2024
 */
@Controller
public class SearchController {

    /**
     * Logger for logging
     */
    private static final Logger log = LoggerFactory.getLogger(SearchController.class);

    /**
     * Song service for operations
     */
    private final SongService songService;

    /**
     * Constructor
     * @param songService Injected song service
     */
    public SearchController(SongService songService) {
        this.songService = songService;
    }

    /**
     * This shows the search page and adds several important attributes to the model for the oage
     * @param model Model to use
     * @param httpSession HttpSessions of user
     * @return Search page
     */
    @GetMapping("/search")
    public String showSearchPage(Model model, HttpSession httpSession) {
        final User user = (User) httpSession.getAttribute("currentUser");
        log.info("{} went to search page", user.getUsername());


        model.addAttribute("currentUser", user);
        model.addAttribute("playlists",user.getPlaylists());

        model.addAttribute("results", new HashSet<>()); // Initialize results as empty
        model.addAttribute("query", null); // Initialize query as empty
        return "search"; // Return to the search page
    }

    /**
     * Performs the search for all songs related to the query param, and displays them
     * on the page. Sets up the model so the user can add songs to a playlist if they wish
     * @param query Query to search for
     * @param model Model to use
     * @param httpSession httpSession of user
     * @return Updated search page
     */
    @PostMapping("/search")
    @Transactional
    public String search(@RequestParam(value = "query") String query, Model model, HttpSession httpSession) {
        //Reload user
        final User sessionUser = (User) httpSession.getAttribute("currentUser");
        final User user = songService.getUser(sessionUser.getUsername());

        log.info("{} searched for songs with query '{}'", user.getUsername(), query);

        if (query.trim().isEmpty() || query.length() < 3) {
            model.addAttribute("error", "Search term must be at least 3 characters long.");
            return "search"; // Return to the search page with error message
        }

        Hibernate.initialize(user.getPlaylists());

        final Set<Song> results = songService.searchForSongs(query);

        if(results.isEmpty()) {
            model.addAttribute("error", "No songs found.");
            return "search";
        }

        model.addAttribute("results", results);
        model.addAttribute("query", query);
        model.addAttribute("currentUser", user);
        model.addAttribute("playlists",user.getPlaylists());


        return "search"; // Return the search template
    }

    /**
     * Handles adding a song to a playlist. Since we can't
     * pass a Song object through the form, we need to pass
     * all the params needed for a song and create a new object.
     * @param playlistId ID of Playlist to add to
     * @param songID Song's id
     * @param songName Song's name
     * @param length Length of song
     * @param artistName Name of artist who created song
     * @param artistID Deezer id of artist
     * @param albumName Name of album song is in
     * @param albumID Deezer id of album
     * @return The search page after song has been added
     */
    @PostMapping("/addSongToPlaylist")
    public String addSongToPlaylist(
            @RequestParam Long playlistId,
            @RequestParam Long songID,
            @RequestParam String songName,
            @RequestParam int length,
            @RequestParam String artistName,
            @RequestParam long artistID,
            @RequestParam String albumName,
            @RequestParam long albumID) {

        // Create a new Song object
        final Song song = new Song(songID, songName, length, artistName, artistID, albumName, albumID);

        log.info("Adding song: {} to playlist with ID: {}", songName, playlistId);

        // Add song to playlist
        songService.addSongToPlaylist(playlistId, song);

        return "redirect:/search"; // Redirect to the playlist page
    }
}

