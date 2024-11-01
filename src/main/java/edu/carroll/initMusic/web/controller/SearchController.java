package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.config.CustomUserDetails;
import edu.carroll.initMusic.jpa.model.Song;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.service.PlaylistService;
import edu.carroll.initMusic.service.SongService;
import edu.carroll.initMusic.service.UserService;
import edu.carroll.initMusic.web.form.NewPlaylistForm;
import edu.carroll.initMusic.web.form.NewSongForm;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
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
 * This Controller handles the search page, which is where users can
 * search for songs and add them to playlists
 *
 * @author Nick Clouse
 *
 * @since October 2, 2024
 */
@Controller
public class SearchController {

    /** Logger for logging */
    private static final Logger log = LoggerFactory.getLogger(SearchController.class);

    /** Song service for operations */
    private final SongService songService;

    /** User service for operations with user objects */
    private final UserService userService;

    /** Playlist service for operations with playlist objects */
    private final PlaylistService playlistService;

    /**
     * Constructor
     * @param songService Injected song service
     * @param userService Injected user service
     */
    public SearchController(SongService songService, UserService userService, PlaylistService playlistService) {
        this.songService = songService;
        this.userService = userService;
        this.playlistService = playlistService;
    }

    /**
     * This shows the search page and adds several important attributes to the model for the oage
     * @param model Model to use
     * @param authentication Current authenticated user token, if any
     * @return Search page
     */
    @GetMapping("/search")
    public String showSearchPage(Model model, Authentication authentication) {
        //Retrieve the current user from the session
        final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        final User user = userDetails.getUser();


        //Fetch user with playlists
        final User fullUser = userService.findByIdWithPlaylists(user.getuserID());

        log.info("{} went to search page", fullUser.getUsername());

        //Add the user and their playlists to the model
        model.addAttribute("currentUser", fullUser);
        model.addAttribute("playlists", fullUser.getPlaylists());

        //Initialize results and query
        model.addAttribute("results", new HashSet<>()); // Initialize results as empty
        model.addAttribute("query", null); // Initialize query as empty
        model.addAttribute("newSongForm", new NewSongForm());
        model.addAttribute("NewPlaylistForm", new NewPlaylistForm());

        return "search"; // Return to the search page
    }

    /**
     * Performs the search for all songs related to the query param, and displays them
     * on the page. Sets up the model so the user can add songs to a playlist if they wish
     * @param query Query to search for
     * @param model Model to use
     * @param authentication Current authenticated user token, if any
     * @return Updated search page
     */
    @PostMapping("/search")
    @Transactional
    public String search(@RequestParam(value = "query") String query, Model model, Authentication authentication) {
        //Retrieve the current user
        final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        final User user = userService.findByIdWithPlaylists(userDetails.getUser().getuserID());

        log.info("{} searched for songs with query '{}'", user.getUsername(), query);

        //If query doesnt have any text
        if (query.trim().isEmpty() || query.length() < 3) {
            model.addAttribute("error", "Search term must be at least 3 characters long.");
            return "search"; // Return to the search page with error message
        }

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
        model.addAttribute("NewPlaylistForm", new NewPlaylistForm());

        return "search"; // Return the search template
    }

    /**
     * Handles adding a song to a playlist. Since we can't
     * pass a Song object through the form, we need to pass
     * all the params needed for a song and create a new object.
     */
    @PostMapping("/addSongToPlaylist")
    public String addSongToPlaylist(@Valid @ModelAttribute NewSongForm newSongForm, BindingResult result, RedirectAttributes attrs) {
        if (result.hasErrors()) {
            log.info("Adding song errors: {}", result.getAllErrors());
            attrs.addFlashAttribute("error", result.getAllErrors().getFirst().getDefaultMessage());
            return "redirect:/search";
        }
        final List<Long> selectedPlaylists = newSongForm.getSelectedPlaylists();

        final Song song = getSong(newSongForm);

        // Handle the logic for adding the song to the selected playlists
        for (Long playlistId : selectedPlaylists) {
            log.info("Calling songService to add song {} to playlist {}", song.getSongID(), playlistId);
            playlistService.addSongToPlaylist(playlistId, song);
        }

        return "redirect:/search";
    }

    /**
     * Gets the song attributes from a NewSongForm object and creates a new song
     * @param addSongForm Form to get attributes from
     * @return The new song object
     * @see NewSongForm
     */
    private static Song getSong(NewSongForm addSongForm) {
        final Long songID = addSongForm.getSongID();
        final  String songName = addSongForm.getSongName();
        final int songLength = addSongForm.getSongLength();
        final String artistName = addSongForm.getArtistName();
        final long artistID = addSongForm.getArtistID();
        final String albumName = addSongForm.getAlbumName();
        final long albumID = addSongForm.getAlbumID();
        final String songImg = addSongForm.getSongImg();
        final String songPreview = addSongForm.getSongPreview();


        // Create a new Song object
        final Song song = new Song(songID, songName, songLength, artistName, artistID, albumName, albumID);
        song.setSongImg(songImg);
        song.setSongPreview(songPreview);
        return song;
    }
}

