package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.ResponseStatus;
import edu.carroll.initMusic.jpa.model.Playlist;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.service.UserService;
import edu.carroll.initMusic.web.form.DeleteSongFromPlaylistForm;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ViewPlaylistController {

    /**
     * Logger for logging
     */
    private static final Logger log = LoggerFactory.getLogger(ViewPlaylistController.class);


    final UserService userService;


    public ViewPlaylistController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/viewPlaylist/{playlistID}")
    public String getViewPlaylistPage(@PathVariable("playlistID") Long playlistID, Model model,HttpSession httpSession) {

        final User sessionUser = (User) httpSession.getAttribute("currentUser");
        final User user = userService.getUser(sessionUser.getUsername());
        log.info("User#{} went to view playlist#{}", user.getuserID(), playlistID);

        final Playlist playlist = userService.getPlaylist(playlistID);

        if(playlist == null) {
            model.addAttribute("error", "Playlist not found");
            log.info("Playlist not found when user#{} tried to view playlist#{}", user.getuserID(), playlistID);
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

    @PostMapping("/deleteSongFromPlaylist")
    public String deleteSongFromPlaylist(@Valid @ModelAttribute DeleteSongFromPlaylistForm deleteSongFromPlaylistForm,
                                         BindingResult bindingResult,
                                         HttpSession httpSession,
                                         RedirectAttributes redirectAttributes){

        final User sessionUser = (User) httpSession.getAttribute("currentUser");
        final User user = userService.getUser(sessionUser.getUsername());

        final Long playlistID = deleteSongFromPlaylistForm.getPlaylistID();
        final Long songID = deleteSongFromPlaylistForm.getSongID();
        log.info("User#{} wants to delete song from playlist#{}", user.getuserID(),playlistID);

        final ResponseStatus songRemoved = userService.removeSongFromPlaylist(playlistID, songID);

        if(songRemoved.failed()){
            redirectAttributes.addFlashAttribute("error", songRemoved.getMessage());
            return "redirect:/viewPlaylist/"+playlistID;
        }

        redirectAttributes.addFlashAttribute("successMsg", "Playlist '"+songRemoved+"' deleted!");


        return "redirect:/viewPlaylist/"+playlistID;
    }
}
