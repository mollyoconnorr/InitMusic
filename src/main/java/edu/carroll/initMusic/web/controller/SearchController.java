package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.jpa.model.Song;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.service.SongService;
import edu.carroll.initMusic.service.UserService;
import edu.carroll.initMusic.web.form.NewSongForm;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
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

    private final UserService userService;

    /**
     * Constructor
     * @param songService Injected song service
     */
    public SearchController(SongService songService, UserService userService) {

        this.songService = songService;
        this.userService = userService;
    }

    /**
     * This shows the search page and adds several important attributes to the model for the oage
     * @param model Model to use
     * @param httpSession HttpSessions of user
     * @return Search page
     */
    @GetMapping("/search")
    public String showSearchPage(Model model, HttpSession httpSession) {
        // Retrieve the current user from the session
        final User user = (User) httpSession.getAttribute("currentUser");

        // Fetch user with playlists
        User fullUser = userService.findByIdWithPlaylists(user.getuserID());

        log.info("{} went to search page", fullUser.getUsername());

        // Add the user and their playlists to the model
        model.addAttribute("currentUser", fullUser);
        model.addAttribute("playlists", fullUser.getPlaylists());

        // Initialize results and query
        model.addAttribute("results", new HashSet<>()); // Initialize results as empty
        model.addAttribute("query", null); // Initialize query as empty
        model.addAttribute("newSongForm", new NewSongForm());

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
        model.addAttribute("newSongForm", new NewSongForm());

        return "search"; // Return the search template
    }

    /**
     * Handles adding a song to a playlist. Since we can't
     * pass a Song object through the form, we need to pass
     * all the params needed for a song and create a new object.
     */
    @PostMapping("/addSongToPlaylist")
    public String addSongToPlaylist(@ModelAttribute NewSongForm newSongForm, BindingResult result, RedirectAttributes attrs) {
        
        if (result.hasErrors()) {
            log.info("Adding song errors: {}", result.getAllErrors());
            return "redirect:/search"; // Handle errors
        }

        // Access the selected playlist IDs
        List<Long> selectedPlaylists = newSongForm.getSelectedPlaylists();
        System.out.println("Selected Playlists: " + selectedPlaylists);

        final Song song = getSong(newSongForm);

        // Handle the logic for adding the song to the selected playlists
        for (Long playlistId : newSongForm.getSelectedPlaylists()) {
            log.info("Added {} to playlist {}", song, playlistId);
            songService.addSongToPlaylist(playlistId, song);
        }

//        log.info("Adding song: {} to playlist with ID: {}", song, playlistId);
//
//        // Add song to playlist
//        songService.addSongToPlaylist(playlistId, song);

        return "redirect:/search"; // Redirect to the playlist page
    }

    private static Song getSong(NewSongForm addSongForm) {
        Long songID = addSongForm.getSongID();
        String songName = addSongForm.getSongName();
        int songLength = addSongForm.getSongLength();
        String artistName = addSongForm.getArtistName();
        Long artistID = addSongForm.getArtistID();
        String albumName = addSongForm.getAlbumName();
        Long albumID = addSongForm.getAlbumID();
        String songImg = addSongForm.getSongImg();
        String songPreview = addSongForm.getSongPreview();


        // Create a new Song object
        final Song song = new Song(songID, songName, songLength, artistName, artistID, albumName, albumID);
        song.setSongImg(songImg);
        song.setSongPreview(songPreview);
        return song;
    }
}

