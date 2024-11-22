package edu.carroll.initMusic.web.controller.songManagement;

import edu.carroll.initMusic.MethodOutcome;
import edu.carroll.initMusic.config.CustomUserDetails;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.service.songManagement.PlaylistService;
import edu.carroll.initMusic.service.userManagement.UserService;
import edu.carroll.initMusic.web.form.songManagement.DeletePlaylistForm;
import edu.carroll.initMusic.web.form.songManagement.NewPlaylistForm;
import edu.carroll.initMusic.web.form.songManagement.RenamePlaylistForm;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * This controller handles the playlist page, which is where users can do things like
 * look at their playlists and add/remove playlists.
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
     *
     * @param userService Injected userService
     */
    public PlaylistController(UserService userService, PlaylistService playlistService) {
        this.userService = userService;
        this.playlistService = playlistService;
    }

    /**
     * This shows the default playlist page
     *
     * @param model          Model to use
     * @param authentication Current authentication token, if any
     * @return Page to go to
     */
    @GetMapping("/playlists")
    public String showPlaylistPage(Model model, Authentication authentication) {
        //Retrieve the current user
        final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        final User user = userService.findByIdWithPlaylists(userDetails.getUser().getuserID());

        log.info("showPlaylistPage: {} went to playlist page", user.getuserID());

        model.addAttribute("currentUser", user);
        model.addAttribute("NewPlaylistForm", new NewPlaylistForm());
        model.addAttribute("RenamePlaylistForm", new RenamePlaylistForm());
        model.addAttribute("DeletePlaylistForm", new DeletePlaylistForm());

        // Check for flash attributes
        if (model.containsAttribute("creationError")) {
            log.info("showPlaylistPage: Flash attribute 'creationError' found: {}", model.getAttribute("creationError"));
        }

        return "playlists"; // Return to the playlist page
    }

    /**
     * Handles creating a new playlist with name inputted by user
     *
     * @param newPlaylistForm Form to get name from
     * @param bindingResult   Result of validation
     * @param authentication  Current authentication token, if any
     * @return Redirect to playlist
     */
    @PostMapping("/createPlaylist")
    public String createPlaylist(@RequestHeader(value = "referer", required = false) String referer,
                                 @Valid @ModelAttribute NewPlaylistForm newPlaylistForm,
                                 BindingResult bindingResult,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {

        //Retrieve the current user
        final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        final User user = userService.findByIdWithPlaylists(userDetails.getUser().getuserID());

        //If there are any binding errors, log errors and return back to playlists page

        //Check if there was a referer page user just came from, if so, we'll return user back to that page
        final String defaultRedirect = "redirect:/playlists";
        String redirectPage = defaultRedirect;
        if (referer != null) {
            //Extract the uri (/page part) so user get redirected back to page they were just on.
            try {
                final URI uri = new URI(referer);
                redirectPage = "redirect:" + uri.getPath();
                //Catch URI syntax exception
            } catch (URISyntaxException e) {
                log.warn("createPlaylist: createPlaylist: Tried to redirect to {} after playlist was created but got: {}", referer, e.getMessage());
                redirectPage = defaultRedirect;
            }
        }

        //If there are any binding errors, log errors and return back to page
        if (bindingResult.hasErrors()) {
            if (bindingResult.getFieldError("playlistName") != null) {
                redirectAttributes.addFlashAttribute("error", bindingResult.getFieldError("playlistName").getDefaultMessage());
            }
            log.error("createPlaylist: Binding errors found when attempting to create a playlist: {}", bindingResult.getAllErrors());

            return redirectPage;
        }
        final String playlistName = newPlaylistForm.getPlaylistName();

        log.info("createPlaylist: User id#{} wants to make a new playlist with name {}", user.getuserID(), playlistName);

        //Create new playlist
        final MethodOutcome playlistCreated = playlistService.createPlaylist(playlistName, user);
        if (playlistCreated.failed()) {
            redirectAttributes.addFlashAttribute("error", playlistCreated.getMessage());
            return redirectPage;
        }

        //Add flash attribute for success message for user
        redirectAttributes.addFlashAttribute("successMsg", playlistName + " created!");

        return redirectPage;
    }

    /**
     * Handles renaming a playlist using data from user
     *
     * @param renamePlaylistForm Form to get data from
     * @param bindingResult      Binding result
     * @param authentication     Current authentication token
     * @param redirectAttributes Attributes to show once redirecting, if any
     * @return Redirect to playlist page
     */
    @PostMapping("/renamePlaylist")
    public String renamePlaylist(@Valid @ModelAttribute RenamePlaylistForm renamePlaylistForm,
                                 BindingResult bindingResult,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        //If there are any binding errors, log errors and return back to playlists page
        if (bindingResult.hasErrors()) {
            //This is the only binding result for this form that would be caused by user
            if (bindingResult.getFieldError("newPlaylistName") != null) {
                redirectAttributes.addFlashAttribute("error", bindingResult.getFieldError("newPlaylistName"));
            } else {
                //Error was not related to any user input
                redirectAttributes.addFlashAttribute("error", "Error renaming playlist");
            }
            log.error("renamePlaylist: Binding errors found when attempting to rename a playlist: {}", bindingResult.getAllErrors());
            return "redirect:/playlists";  // Return the view with errors
        }

        //Retrieve the current user
        final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        final User user = userService.findByIdWithPlaylists(userDetails.getUser().getuserID());
        final String newPlaylistName = renamePlaylistForm.getNewPlaylistName();
        final Long playlistID = renamePlaylistForm.getPlaylistID();
        final String oldPlaylistName = playlistService.getPlaylist(playlistID).getPlaylistName();


        log.info("renamePlaylist: User {} wants to rename playlist {} to '{}'", user.getuserID(), playlistID, newPlaylistName);

        //Check if playlist was successfully renamed
        final MethodOutcome playlistRenamed = playlistService.renamePlaylist(newPlaylistName, playlistID, user);

        if (playlistRenamed.failed()) {
            redirectAttributes.addFlashAttribute("error", playlistRenamed.getMessage());
            return "redirect:/playlists";
        }

        //Add flash attribute for success message for user
        redirectAttributes.addFlashAttribute("successMsg", oldPlaylistName + " renamed to " + newPlaylistName + "!");

        return "redirect:/playlists";
    }

    /**
     * Handles deleting a playlist
     *
     * @param deletePlaylistForm Form to get data from
     * @param bindingResult      Binding result
     * @param authentication     Current authentication token
     * @param redirectAttributes Attributes to show once redirecting, if any
     * @return Redirect to playlist page
     */
    @PostMapping("/deletePlaylist")
    public String deletePlaylist(@Valid @ModelAttribute DeletePlaylistForm deletePlaylistForm,
                                 BindingResult bindingResult,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        //If there are any binding errors, log errors and return back to playlists page
        if (bindingResult.hasErrors()) {
            log.error("deletePlaylist: Binding errors found when attempting to delete a playlist: {}", bindingResult.getAllErrors());
            //This error would not be caused by any user input
            redirectAttributes.addFlashAttribute("error", "Error deleting playlist");
            return "redirect:/playlists";  // Return the view with errors
        }

        //Retrieve the current user
        final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        final User user = userService.findByIdWithPlaylists(userDetails.getUser().getuserID());
        final Long playlistID = deletePlaylistForm.getPlaylistID();
        final String playlistName = deletePlaylistForm.getPlaylistName();

        log.info("deletePlaylist: User {} wants to delete playlist id#{}", user.getuserID(), playlistID);

        //If there was an error deleting a playlist, add error attr to model and return it
        final MethodOutcome playlistDeleted = playlistService.deletePlaylist(playlistName, playlistID, user);
        if (playlistDeleted.failed()) {
            redirectAttributes.addFlashAttribute("error", playlistDeleted.getMessage());
            return "redirect:/playlists";
        }

        //Add flash attribute for success message for user
        redirectAttributes.addFlashAttribute("successMsg", playlistName + " deleted!");

        return "redirect:/playlists";
    }
}
