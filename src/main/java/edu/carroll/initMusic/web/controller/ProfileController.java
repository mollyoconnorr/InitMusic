package edu.carroll.initMusic.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {
    @GetMapping("/myProfile")
    public String showMyProfile() {
        return "myProfile"; // Make sure myProfile.html exists in your templates folder
    }
}
