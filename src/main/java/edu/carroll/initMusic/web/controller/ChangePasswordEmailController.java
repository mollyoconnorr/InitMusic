package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.service.UserService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import edu.carroll.initMusic.web.form.CheckUserEmailForm; // Adjust package as necessary
import edu.carroll.initMusic.jpa.model.User; // Ensure you have the correct import for the User model
import org.springframework.stereotype.Controller;


@Controller
public class ChangePasswordEmailController {
    /**
     * Logger object used for logging actions within this controller.
     */
    private static final Logger log = LoggerFactory.getLogger(ChangePasswordEmailController.class);

    private final UserService userService;

    public ChangePasswordEmailController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/changePasswordEmail")
    public String showChangePasswordEmailPage() {
        log.info("request mapping getting called");
        return "changePasswordEmail"; // Thymeleaf template for the email input page
    }

    @PostMapping("/changePasswordEmail")
    public String handleEmailSubmission(@ModelAttribute CheckUserEmailForm emailForm, HttpSession httpSession, Model model) {
        User user = userService.findByEmail(emailForm.getEmail()); // Method to find user by email
        if (user != null) {
            httpSession.setAttribute("currentUser", user);

            // Email exists, redirect to the security questions page
            // Pass the user's security questions to the model for rendering
            model.addAttribute("question1", user.getQuestion1());
            model.addAttribute("question2", user.getQuestion2());
            return "passSecurity"; // Change this to your Thymeleaf template for the security questions
        } else {
            // Email does not exist, return an error message
            log.info("When trying to change password, no user found with this email.");
            model.addAttribute("errorMessage", "That email doesn't exist in our system. Please try again."); // Add error message to model
            return "changePasswordEmail";
        }
    }
}
