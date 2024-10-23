package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.ResponseStatus;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.service.PlaylistService;
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

    /** Logger for logging */
    private static final Logger log = LoggerFactory.getLogger(PlaylistController.class);

    /** User service for operations involving user objects */
    private final UserService userService;

    /** Playlist service for operations involving playlist objects */
    private final PlaylistService playlistService;

    /**
     * Default constructor
     * @param userService Injected userService
     */
    public PlaylistController(UserService userService, PlaylistService playlistService) {
        this.userService = userService;
        this.playlistService = playlistService;
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

        log.info("{} went to playlist page", user.getuserID());

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
     * @return Redirect to playlist
     */
    @PostMapping("/createPlaylist")
    public String createPlaylist(@Valid @ModelAttribute NewPlaylistForm newPlaylistForm,
                                 BindingResult bindingResult,
                                 HttpSession httpSession,
                                 RedirectAttributes redirectAttributes) {
        //If there are any binding errors, log errors and return back to playlists page
        if (bindingResult.hasErrors()) {
            if(bindingResult.getFieldError("playlistName") != null) {
                redirectAttributes.addFlashAttribute("error", bindingResult.getFieldError("playlistName"));
            }
            log.error("Binding errors found when attempting to create a playlist: {}", bindingResult.getAllErrors());
            return "redirect:/playlists";  // Return the view with errors
        }

        //Reload user
        final User sessionUser = (User) httpSession.getAttribute("currentUser");
        final User user = userService.getUser(sessionUser.getUsername());
        final String playlistName = newPlaylistForm.getPlaylistName();

        log.info("User {} wants to make a new playlist with name {}",user.getuserID(),playlistName);

        //Create new playlist
        final ResponseStatus playlistCreated = playlistService.createPlaylist(playlistName,user);
        if(playlistCreated.failed()) {
            redirectAttributes.addFlashAttribute("error", playlistCreated.getMessage());
            return "redirect:/playlists";
        }

        //Add flash attribute for success message for user
        redirectAttributes.addFlashAttribute("successMsg", playlistName + " created!");

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
        //If there are any binding errors, log errors and return back to playlists page
        if (bindingResult.hasErrors()) {
            //This is the only binding result for this form that would be caused by user
            if(bindingResult.getFieldError("newPlaylistName") != null) {
                redirectAttributes.addFlashAttribute("error", bindingResult.getFieldError("newPlaylistName"));
            }else{
                //Error was not related to any user input
                redirectAttributes.addFlashAttribute("error", "Error renaming playlist");
            }
            log.error("Binding errors found when attempting to rename a playlist: {}", bindingResult.getAllErrors());
            return "redirect:/playlists";  // Return the view with errors
        }

        //Reload user
        final User sessionUser = (User) httpSession.getAttribute("currentUser");
        final User user = userService.getUser(sessionUser.getUsername());
        final String newPlaylistName = renamePlaylistForm.getNewPlaylistName();
        final Long playlistID = renamePlaylistForm.getPlaylistID();
        final String oldPlaylistName = playlistService.getPlaylist(playlistID).getPlaylistName();


        log.info("User {} wants to rename playlist {} to '{}'",user.getuserID(),playlistID,newPlaylistName);

        //Check if playlist was successfully renamed
        final ResponseStatus playlistRenamed = playlistService.renamePlaylist(newPlaylistName,playlistID,user);

        if (playlistRenamed.failed()) {
            redirectAttributes.addFlashAttribute("error", playlistRenamed.getMessage());
            return "redirect:/playlists";
        }

        //Add flash attribute for success message for user
        redirectAttributes.addFlashAttribute("successMsg", oldPlaylistName + " renamed to "+newPlaylistName +"!");

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
        //If there are any binding errors, log errors and return back to playlists page
        if (bindingResult.hasErrors()) {
            log.error("Binding errors found when attempting to delete a playlist: {}", bindingResult.getAllErrors());
            //This error would not be caused by any user input
            redirectAttributes.addFlashAttribute("error", "Error deleting playlist");
            return "redirect:/playlists";  // Return the view with errors
        }

        //Reload user
        final User sessionUser = (User) httpSession.getAttribute("currentUser");
        final User user = userService.getUser(sessionUser.getUsername());
        final Long playlistID = deletePlaylistForm.getPlaylistID();
        final String playlistName = deletePlaylistForm.getPlaylistName();

        log.info("User {} wants to delete playlist id#{}",user.getuserID(),playlistID);

        //If there was an error deleting a playlist, add error attr to model and return it
        final ResponseStatus playlistDeleted = playlistService.deletePlaylist(playlistName,playlistID,user);
        if(playlistDeleted.failed()){
            redirectAttributes.addFlashAttribute("error", playlistDeleted.getMessage());
            return "redirect:/playlists";
        }

        //Add flash attribute for success message for user
        redirectAttributes.addFlashAttribute("successMsg", playlistName+" deleted!");

        return "redirect:/playlists";
    }
}
