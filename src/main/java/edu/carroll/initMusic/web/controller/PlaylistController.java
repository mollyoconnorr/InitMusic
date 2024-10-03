package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.jpa.repo.PlaylistRepository;
import edu.carroll.initMusic.jpa.repo.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashSet;
import java.util.List;

@Controller
public class PlaylistController {

    private static final Logger log = LoggerFactory.getLogger(PlaylistController.class);

    private final UserRepository userRepo;

    private final PlaylistRepository playlistRepo;

    public PlaylistController(UserRepository userRepo, PlaylistRepository playlistRepo) {
        this.userRepo = userRepo;
        this.playlistRepo = playlistRepo;
    }

    @GetMapping("/playlist")
    public String showSearchPage(Model model, HttpSession httpSession) {

        final String username = (String) httpSession.getAttribute("currentUser");
        log.info("{} went to playlist page", username);

        final List<User> foundUser = userRepo.findByUsernameIgnoreCase(username);
        if(foundUser.size() != 1) {
            model.addAttribute("error", "No user found");
            return "playlist";
        }

        model.addAttribute("currentUser", foundUser.getFirst());

        return "playlist"; // Return to the search page
    }


}
