package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.jpa.model.Playlist;
import edu.carroll.initMusic.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ViewPlaylistController {

    final UserService userService;


    public ViewPlaylistController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/viewPlaylist/{playlistID}")
    public String getViewPlaylistPage(@PathVariable("playlistID") Long playlistID, Model model) {

        final Playlist playlist = userService.getPlaylist(playlistID);

        if(playlist == null) {
            model.addAttribute("error", "Playlist not found");
            return "viewPlaylist";
        }

        model.addAttribute("playlist", playlist);

        model.addAttribute("playlistName", playlist.getPlaylistName());
        model.addAttribute("playlistSongs", playlist.getSongs());
        model.addAttribute("playlistAuthor", playlist.getAuthor().getUsername());

        model.addAttribute("playlistID",playlistID);

        return "viewPlaylist";
    }
}
