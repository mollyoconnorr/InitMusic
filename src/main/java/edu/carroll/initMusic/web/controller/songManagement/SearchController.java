package edu.carroll.initMusic.web.controller.songManagement;

import edu.carroll.initMusic.MethodOutcome;
import edu.carroll.initMusic.config.CustomUserDetails;
import edu.carroll.initMusic.jpa.model.Playlist;
import edu.carroll.initMusic.jpa.model.Song;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.service.songManagement.PlaylistService;
import edu.carroll.initMusic.service.songManagement.SongService;
import edu.carroll.initMusic.service.userManagement.UserService;
import edu.carroll.initMusic.web.form.songManagement.NewPlaylistForm;
import edu.carroll.initMusic.web.form.songManagement.NewSongForm;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This Controller handles the search page, which is where users can
 * search for songs and add them to playlists
 */
@Controller
public class SearchController {
    /** Maximum length a query can be */
    private static final int MAX_QUERY_LENGTH = 40;

    /** Minimum length a query can be */
    private static final int MIN_QUERY_LENGTH = 3;

    /** Logger for logging */
    private static final Logger log = LoggerFactory.getLogger(SearchController.class);

    /** Song service for operations with songs and caches */
    private final SongService songService;

    /** User service for operations with user objects */
    private final UserService userService;

    /** Playlist service for operations with playlist objects */
    private final PlaylistService playlistService;

    /**
     * Constructor
     *
     * @param songService Injected song service
     * @param userService Injected user service
     */
    public SearchController(SongService songService, UserService userService, PlaylistService playlistService) {
        this.songService = songService;
        this.userService = userService;
        this.playlistService = playlistService;
    }

    /**
     * Gets the song attributes from a NewSongForm object and creates a new song
     *
     * @param addSongForm Form to get attributes from
     * @return The new song object
     * @see NewSongForm
     */
    private static Song getSong(NewSongForm addSongForm) {
        final Long songID = addSongForm.getSongID();
        final String songName = addSongForm.getSongName();
        final int songLength = addSongForm.getSongLength();
        final String artistName = addSongForm.getArtistName();
        final Long artistID = addSongForm.getArtistID();
        final String albumName = addSongForm.getAlbumName();
        final Long albumID = addSongForm.getAlbumID();
        final String songImg = addSongForm.getSongImg();
        final String songPreview = addSongForm.getSongPreview();


        // Create a new Song object
        final Song song = new Song(songID, songName, songLength, artistName, artistID, albumName, albumID);
        song.setSongImg(songImg);
        song.setSongPreview(songPreview);
        return song;
    }

    /**
     * This shows the search page and adds several important attributes to the model for the page
     *
     * @param model          Model to use
     * @param authentication Current authenticated user token, if any
     * @return Search page
     */
    @GetMapping("/search")
    public String showSearchPage(Model model, Authentication authentication, HttpSession session) {
        //Retrieve the current user from the session
        final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        final User user = userDetails.getUser();

        //Fetch user with playlists
        final User fullUser = userService.findByIdWithPlaylists(user.getuserID());

        log.info("showSearchPage: {} went to search page", fullUser.getuserID());

        //Add the user and their playlists to the model
        model.addAttribute("currentUser", fullUser);
        model.addAttribute("playlists", fullUser.getPlaylists());

        /*
          Result is stored in the httpSession so its available after adding song to a playlist.
          When a user first goes to search page, there is no results yet, so set it to a empty hashset
         */
        if (session.getAttribute("results") instanceof Set<?> && session.getAttribute("results") != null) {
            model.addAttribute("results", session.getAttribute("results"));
        } else {
            model.addAttribute("results", new HashSet<>());
        }

        /*
          query is stored in the httpSession so its available after adding song to a playlist.
          When a user first goes to search page, there is no results yet, so set it to a new song form
         */
        if (session.getAttribute("query") != null) {
            model.addAttribute("query", session.getAttribute("query"));
        } else {
            model.addAttribute("query", null);
        }

        model.addAttribute("newSongForm", new NewSongForm());
        model.addAttribute("NewPlaylistForm", new NewPlaylistForm());

        return "search"; // Return to the search page
    }

    /**
     * Performs the search for all songs related to the query param, and displays them
     * on the page. Sets up the model so the user can add songs to a playlist if they wish
     *
     * @param songSearch     Song name to search for
     * @param artistSearch   Artist name to search for
     * @param model          Model to use
     * @param authentication Current authenticated user token, if any
     * @return Updated search page
     */
    @PostMapping("/search")
    @Transactional
    public String search(@RequestParam(value = "songSearch") String songSearch,
                         @RequestParam(value = "artistSearch") String artistSearch, Model model, Authentication authentication, HttpSession session) {
        //Retrieve the current user
        final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        final User user = userService.findByIdWithPlaylists(userDetails.getUser().getuserID());

        //Put the query into a format suitable for logging and displaying back to user
        String query = "";
        if (!songSearch.isEmpty() && !artistSearch.isEmpty()) {
            query = songSearch + " by " + artistSearch;
        } else if (!songSearch.isEmpty()) {
            query = songSearch;
        } else if (!artistSearch.isEmpty()) {
            query = artistSearch;
        }

        log.info("search: {} searched for songs with query '{}'", user.getuserID(), query);

        //If either query isn't valid according to our criteria
        if (!songService.isValidQuery(songSearch) && !songService.isValidQuery(artistSearch)) {
            //For whatever reason, when this if statement is triggered, the model is missing the
            //new playlist form, so we have to add it again here
            model.addAttribute("NewPlaylistForm", new NewPlaylistForm());
            model.addAttribute("searchError", "Song and Artist name both must be between " + MIN_QUERY_LENGTH + " and " + MAX_QUERY_LENGTH + " characters long, and" +
                    " there must be a song or artist name present.");
            log.error("search: User id#{} searched using an invalid query", user.getUsername());
            return "search"; // Return to the search page with error message
        }

        final Set<Song> results = songService.searchForSongs(songSearch, artistSearch);

        //If no songs found
        if (results == null || results.isEmpty()) {
            //For whatever reason, when this if statement is triggered, the model is missing the
            //new playlist form, so we have to add it again here
            model.addAttribute("NewPlaylistForm", new NewPlaylistForm());
            model.addAttribute("searchError", String.format("No songs found for query '%s'", query));
            return "search";
        }

        session.setAttribute("results", results);
        session.setAttribute("query", query);
        model.addAttribute("results", results);
        model.addAttribute("query", query);
        model.addAttribute("currentUser", user);
        model.addAttribute("playlists", user.getPlaylists());
        model.addAttribute("newSongForm", new NewSongForm());
        model.addAttribute("NewPlaylistForm", new NewPlaylistForm());

        return "search";
    }

    /**
     * Handles adding a song to a playlist. Since we can't
     * pass a Song object through the form, we need to pass
     * all the params needed for a song and create a new object.
     *
     * @param newSongForm Form that contains data needed to add song to playlist
     * @param result      Result of binding
     * @param attrs       RedirectAttributes
     * @param session     Current httpSession
     * @return Redirect to search page
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/addSongToPlaylist")
    public String addSongToPlaylist(@Valid @ModelAttribute NewSongForm newSongForm, BindingResult result, RedirectAttributes attrs, HttpSession session) {
        if (result.hasErrors()) {
            log.warn("addSongToPlaylist: Adding song errors: {}", result.getAllErrors());
            attrs.addFlashAttribute("searchError", result.getAllErrors().getFirst().getDefaultMessage());
            return "redirect:/search";
        }
        final List<Playlist> selectedPlaylists = newSongForm.getSelectedPlaylists();

        //Convert data in form to song obj
        final Song song = getSong(newSongForm);

        final List<String> errorMessages = new ArrayList<>();
        final List<String> successMessages = new ArrayList<>();

        // Handle the logic for adding the song to the selected playlists
        for (Playlist playlist : selectedPlaylists) {
            log.info("addSongToPlaylist: Calling songService to add song {} to playlist {}", song.getDeezerID(), playlist.getPlaylistID());
            final MethodOutcome outcome = playlistService.addSongToPlaylist(playlist, song);
            if (outcome.failed()) {
                errorMessages.add(String.format("Error adding %s to %s: %s", song.getSongName(), playlist.getPlaylistName(), outcome.getMessage()));
            } else {
                successMessages.add(String.format("Added %s to %s", song.getSongName(), playlist.getPlaylistName()));
            }
        }

        if (!errorMessages.isEmpty()) {
            attrs.addFlashAttribute("addingErrors", errorMessages);
        }

        if (!successMessages.isEmpty()) {
            attrs.addFlashAttribute("addingSuccesses", successMessages);
        }

        //If there are results in the httpsession, add them back to search page so they get displayed again
        if (session.getAttribute("results") instanceof Set<?>) {
            //Add results and query to flash attributes so they can be redisplayed again
            final Set<Song> results = (Set<Song>) session.getAttribute("results");
            final String query = (String) session.getAttribute("query");
            attrs.addFlashAttribute("results", results);
            attrs.addFlashAttribute("query", query);
        }

        return "redirect:/search";
    }

    /**
     * Handles the mapping for opening a popup window with the preview for a song
     *
     * @param deezerId ID of song to preview
     * @param model    Model to add preview url to
     * @return popupRedirect page
     */
    @GetMapping("/preview/{deezerId}")
    public String previewSong(@PathVariable String deezerId, Model model) {
        //Get the deezer preview url
        String previewURL = "";
        try {
            previewURL = songService.getSongPreview(Long.parseLong(deezerId));
        } catch (NumberFormatException e) {
            log.error("Non-long value passed to previewSong");
        }

        if (previewURL == null || previewURL.isEmpty()) {
            log.error("previewSong: Invalid Deezer id#{}, Showing error page", deezerId);
            model.addAttribute("status", "999");
            return "redirect:/error"; // Redirect to an internal error page
        }


        log.info("previewSong: Creating popup window for link {}", previewURL);

        model.addAttribute("deezerUrl", previewURL);  // Add URL to the model
        return "popupRedirect";  // View that opens the URL in a popup window
    }
}

