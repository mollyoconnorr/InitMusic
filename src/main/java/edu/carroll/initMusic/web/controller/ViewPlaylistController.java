package edu.carroll.initMusic.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ViewPlaylistController {

    @GetMapping("/viewPlaylist/{playlistID}")
    public String getViewPlaylistPage(@PathVariable("playlistID") Long playlistID, Model model) {

        model.addAttribute("playlistID",playlistID);

        return "viewPlaylist";
    }
}
