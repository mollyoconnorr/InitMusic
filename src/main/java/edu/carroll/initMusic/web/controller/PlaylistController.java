package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.service.SongService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * <p>
 * This controller handles the playlist page, which is where users can do things like
 * look at their playlists and add/remove playlists.
 * </p>
 *
 * @author Nick Clouse
 *
 * @since October 2, 2024
 */
@Controller
public class PlaylistController {

    /**
     * Logger for logging
     */
    private static final Logger log = LoggerFactory.getLogger(PlaylistController.class);

    /**
     * Song service for operations
     */
    private final SongService songService;

    /**
     * Default constructor
     * @param songService Injected SongService
     */
    public PlaylistController(SongService songService) {
        this.songService = songService;
    }

    /**
     * This shows the default search page
     * @param model Model to use
     * @param httpSession httpSession of user
     * @return Page to go to
     */
    @GetMapping("/playlist")
    public String showSearchPage(Model model, HttpSession httpSession) {
        //Reload user
        final User sessionUser = (User) httpSession.getAttribute("currentUser");
        final User user = songService.getUser(sessionUser.getUsername());

        log.info("{} went to playlist page", user.getUsername());

        model.addAttribute("currentUser", user);

        return "playlist"; // Return to the search page
    }
}
