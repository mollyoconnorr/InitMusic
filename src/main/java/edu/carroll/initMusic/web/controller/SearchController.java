package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.jpa.model.Song;
import edu.carroll.initMusic.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.Set;

@Controller
public class SearchController {

    private static final Logger log = LoggerFactory.getLogger(SearchController.class);
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public String showSearchPage(Model model) {
        model.addAttribute("results", new HashSet<>()); // Initialize results as empty
        model.addAttribute("query", null); // Initialize query as empty
        return "search"; // Return to the search page
    }

    @PostMapping("/search") // Use POST for the search submission
    public String search(@RequestParam(value = "query") String query, Model model) {
        log.info("Searching for songs with query '{}'", query);

        if (query.trim().isEmpty() || query.length() < 3) {
            model.addAttribute("error", "Search term must be at least 3 characters long.");
            return "search"; // Return to the search page with error message
        }

        final Set<Song> results = searchService.searchForSongs(query);
        model.addAttribute("results", results);
        model.addAttribute("query", query);

        return "search"; // Return the search template
    }
}

