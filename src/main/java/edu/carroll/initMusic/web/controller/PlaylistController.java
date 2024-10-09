package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.service.SongService;
import edu.carroll.initMusic.web.form.LoginForm;
import edu.carroll.initMusic.web.form.NewPlaylistForm;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
     * This shows the default playlist page
     * @param model Model to use
     * @param httpSession httpSession of user
     * @return Page to go to
     */
    @GetMapping("/playlist")
    public String showPlaylistPage(Model model, HttpSession httpSession) {
        //Reload user
        final User sessionUser = (User) httpSession.getAttribute("currentUser");
        final User user = songService.getUser(sessionUser.getUsername());

        log.info("{} went to playlist page", user.getUsername());

        model.addAttribute("currentUser", user);
        model.addAttribute("NewPlaylistForm", new NewPlaylistForm());

        return "playlist"; // Return to the playlist page
    }

    @PostMapping("/createPlaylist")
    public String createPlaylist(@Valid @ModelAttribute NewPlaylistForm newPlaylistForm, BindingResult bindingResult, HttpSession httpSession,Model model) {
        //Reload user
        final User sessionUser = (User) httpSession.getAttribute("currentUser");
        final User user = songService.getUser(sessionUser.getUsername());
        model.addAttribute("currentUser", user);
        model.addAttribute("NewPlaylistForm", new NewPlaylistForm());
        log.info("User wants to make a new playlist with name {}",newPlaylistForm.getPlaylistName());
        return "redirect:/playlist";
    }

    @PostMapping("/renamePlaylist")
    public String renamePlaylist(){



        return "redirect:/playlist";
    }
}
