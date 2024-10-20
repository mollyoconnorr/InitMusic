package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.ResponseStatus;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.service.SongService;
import edu.carroll.initMusic.service.UserService;
import edu.carroll.initMusic.web.form.DeletePlaylistForm;
import edu.carroll.initMusic.web.form.NewPlaylistForm;
import edu.carroll.initMusic.web.form.RenamePlaylistForm;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * This controller handles the playlist page, which is where users can do things like
 * look at their playlists and add/remove playlists.
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

    private final UserService userService;

    /**
     * Default constructor
     * @param songService Injected SongService
     * @param userService Injected userService
     */
    public PlaylistController(SongService songService, UserService userService) {
        this.songService = songService;
        this.userService = userService;
    }

    /**
     * This shows the default playlist page
     * @param model Model to use
     * @param httpSession httpSession of user
     * @return Page to go to
     */
    @GetMapping("/playlists")
    public String showPlaylistPage(Model model, HttpSession httpSession) {
        //Reload user
        final User sessionUser = (User) httpSession.getAttribute("currentUser");
        final User user = userService.getUser(sessionUser.getUsername());

        log.info("{} went to playlist page", user.getUsername());

        model.addAttribute("currentUser", user);
        model.addAttribute("NewPlaylistForm", new NewPlaylistForm());
        model.addAttribute("RenamePlaylistForm", new RenamePlaylistForm());
        model.addAttribute("DeletePlaylistForm",new DeletePlaylistForm());

        // Check for flash attributes
        if (model.containsAttribute("creationError")) {
            log.info("Flash attribute 'creationError' found: {}", model.getAttribute("creationError"));
        }

        return "playlists"; // Return to the playlist page
    }

    /**
     * Handles creating a new playlist with name inputted by user
     * @param newPlaylistForm Form to get name from
     * @param bindingResult Result of validation
     * @param httpSession Current httpSession
     * @param model Model for page
     * @return Redirect to playlist
     */
    @PostMapping("/createPlaylist")
    public String createPlaylist(@Valid @ModelAttribute NewPlaylistForm newPlaylistForm,
                                 BindingResult bindingResult,
                                 HttpSession httpSession,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        //Reload user
        final User sessionUser = (User) httpSession.getAttribute("currentUser");
        final User user = userService.getUser(sessionUser.getUsername());
        final String playlistName = newPlaylistForm.getPlaylistName();

        log.info("User {} wants to make a new playlist with name {}",user.getuserID(),playlistName);

        //Create new playlist
        final ResponseStatus playlistCreated = userService.createPlaylist(playlistName,user);
        if(playlistCreated.failed()) {
            redirectAttributes.addFlashAttribute("error", playlistCreated.getMessage());
            return "redirect:/playlists";
        }

        redirectAttributes.addFlashAttribute("successMsg", "Playlist '" + playlistName + "' created!");

        return "redirect:/playlists";
    }

    /**
     * Handles renaming a playlist using data from user
     * @param renamePlaylistForm Form to get data from
     * @param bindingResult Binding result
     * @param httpSession Current httpSession
     * @param redirectAttributes Attributes to show once redirecting, if any
     * @return Redirect to playlist page
     */
    @PostMapping("/renamePlaylist")
    public String renamePlaylist(@Valid @ModelAttribute RenamePlaylistForm renamePlaylistForm,
                                 BindingResult bindingResult,
                                 HttpSession httpSession,
                                 RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            log.error("Binding errors found when attempting to rename a playlist: {}", bindingResult.getAllErrors());
            return "redirect:/playlists";  // Return the view with errors
        }

        //Reload user
        final User sessionUser = (User) httpSession.getAttribute("currentUser");
        final User user = userService.getUser(sessionUser.getUsername());
        final String newPlaylistName = renamePlaylistForm.getNewPlaylistName();
        final Long playlistID = renamePlaylistForm.getPlaylistID();

        log.info("User {} wants to rename playlist {} to '{}'",user.getuserID(),playlistID,newPlaylistName);

        final ResponseStatus playlistRenamed = userService.renamePlaylist(newPlaylistName,playlistID,user);

        if (playlistRenamed.failed()) {
            redirectAttributes.addFlashAttribute("error", playlistRenamed.getMessage());
            return "redirect:/playlists";
        }

        redirectAttributes.addFlashAttribute("successMsg", "Playlist renamed to "+newPlaylistName +"!");

        return "redirect:/playlists";
    }

    /**
     * Handles deleting a playlist
     * @param deletePlaylistForm Form to get data from
     * @param bindingResult Binding result
     * @param httpSession Current httpSession
     * @param redirectAttributes Attributes to show once redirecting, if any
     * @return Redirect to playlist page
     */
    @PostMapping("/deletePlaylist")
    public String deletePlaylist(@Valid @ModelAttribute DeletePlaylistForm deletePlaylistForm,
                                 BindingResult bindingResult,
                                 HttpSession httpSession,
                                 RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            log.error("Binding errors found when attempting to delete a playlist: {}", bindingResult.getAllErrors());
            return "redirect:/playlists";  // Return the view with errors
        }

        //Reload user
        final User sessionUser = (User) httpSession.getAttribute("currentUser");
        final User user = userService.getUser(sessionUser.getUsername());
        final Long playlistID = deletePlaylistForm.getPlaylistID();
        final String playlistName = deletePlaylistForm.getPlaylistName();

        log.info("User {} wants to delete playlist id#{}",user.getuserID(),playlistID);

        //If there was an error deleting a playlist, add error attr to model and return it
        final ResponseStatus playlistDeleted = userService.deletePlaylist(playlistName,playlistID,user);
        if(playlistDeleted.failed()){
            redirectAttributes.addFlashAttribute("error", playlistDeleted.getMessage());
            return "redirect:/playlists";
        }

        redirectAttributes.addFlashAttribute("successMsg", "Playlist '"+playlistName+"' deleted!");

        return "redirect:/playlists";
    }
}
