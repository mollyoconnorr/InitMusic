package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.MethodOutcome;
import edu.carroll.initMusic.config.CustomUserDetails;
import edu.carroll.initMusic.jpa.model.Playlist;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.service.PlaylistService;
import edu.carroll.initMusic.service.UserService;
import edu.carroll.initMusic.web.form.DeleteSongFromPlaylistForm;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * This Controller handles the view playlist page, which is where users can
 * view an individual playlist and edit its contents
 */
@Controller
public class ViewPlaylistController {

    /** Logger for logging */
    private static final Logger log = LoggerFactory.getLogger(ViewPlaylistController.class);

    /** User service for operations involving user objects */
    final UserService userService;

    /** playlist service for operations involving playlist objects*/
    final PlaylistService playlistService;

    /**
     * Constructor, injects dependencies
     * @param userService Injected UserService
     */
    public ViewPlaylistController(UserService userService, PlaylistService playlistService) {
        this.userService = userService;
        this.playlistService = playlistService;
    }

    /**
     * Gets the view playlist page, loads the selected playlist,
     * which is passed through the path to the page.
     * @param playlistID ID of playlist to load
     * @param model Model to add attributes to
     * @param authentication Current authentication token, if any
     * @return ViewPlaylist page
     */
    @GetMapping("/viewPlaylist/{playlistID}")
    public String getViewPlaylistPage(@PathVariable("playlistID") Long playlistID, Model model, Authentication authentication) {
        //Retrieve the current user
        final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        final User user = userDetails.getUser();
        log.info("getViewPlaylistPage: User id#{} went to view playlist id#{}", user.getuserID(), playlistID);

        final Playlist playlist = playlistService.getPlaylist(playlistID);

        if(playlist == null) {
            model.addAttribute("error", "Playlist not found");
            log.warn("getViewPlaylistPage: Playlist not found when user id#{} tried to view playlist id#{}", user.getuserID(), playlistID);
            return "viewPlaylist";
        }
        model.addAttribute("playlist", playlist);
        model.addAttribute("playlistName", playlist.getPlaylistName());
        model.addAttribute("playlistSongs", playlist.getSongs());
        model.addAttribute("playlistAuthor", playlist.getAuthor().getUsername());
        model.addAttribute("playlistID",playlistID);
        model.addAttribute("deleteSongFromPlaylistForm", new DeleteSongFromPlaylistForm());
        return "viewPlaylist";
    }

    /**
     * Handles deleting the selected song from the playlist
     * @param deleteSongFromPlaylistForm Form which contains information of song and playlist to delete song from
     * @param bindingResult Binding result
     * @param authentication Current authentication token, if any
     * @param redirectAttributes Redirection Attributes, if any
     * @return Redirect to viewPlaylist page regardless if song was deleted or not.
     */
    @PostMapping("/deleteSongFromPlaylist")
    public String deleteSongFromPlaylist(@Valid @ModelAttribute DeleteSongFromPlaylistForm deleteSongFromPlaylistForm,
                                         BindingResult bindingResult,
                                         Authentication authentication,
                                         RedirectAttributes redirectAttributes){

        //Retrieve the current user
        final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        final User user = userDetails.getUser();

        final Long playlistID = deleteSongFromPlaylistForm.getPlaylistID();
        final Long songID = deleteSongFromPlaylistForm.getSongID();
        final String songName = deleteSongFromPlaylistForm.getSongName();
        final String playlistName = deleteSongFromPlaylistForm.getPlaylistName();
        log.info("User id#{} wants to delete song from playlist id#{}", user.getuserID(),playlistID);

        //If there are any binding errors, log errors and return back to viewPlaylist page
        if (bindingResult.hasErrors()) {
            log.warn("deleteSongFromPlaylist: Binding errors found when attempting to delete a song from a playlist: {}", bindingResult.getAllErrors());
            //This error would not be caused by any user input
            redirectAttributes.addFlashAttribute("error", "Error deleting "+ songName +" from " +playlistName);
            return "redirect:/viewPlaylist/"+playlistID;
        }

        final MethodOutcome songRemoved = playlistService.removeSongFromPlaylist(playlistID, songID);

        //If song wasn't removed for some reason
        if(songRemoved.failed()){
            redirectAttributes.addFlashAttribute("error", songRemoved.getMessage());
            log.error("deleteSongFromPlaylist: Song wasn't removed from playlist because '{}'", songRemoved.getMessage());
            return "redirect:/viewPlaylist/"+playlistID;
        }

        //Success message since song was removed
        redirectAttributes.addFlashAttribute("successMsg", "Removed " +
                songName + " from " + playlistName);

        return "redirect:/viewPlaylist/"+playlistID;
    }
}
