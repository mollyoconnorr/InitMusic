package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.service.UserService;
import edu.carroll.initMusic.web.form.PassSQuestionsForm; // Ensure correct import
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpSession;

@Controller
public class PassSecurityController {
    /**
     * Logger object used for logging actions within this controller.
     */
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;


    @GetMapping("/passSecurity")
    public String showChangePasswordEmailPage() {
        return "passSecurity"; // Thymeleaf template for the security questions page
    }

    @PostMapping("/passSecurity")
    public String handleSecuritySubmission(@ModelAttribute PassSQuestionsForm passSecurityForm, HttpSession httpSession, Model model) {
        User currentUser = (User) httpSession.getAttribute("currentUser"); // Ensure this attribute is set in the session

        // Ensure that currentUser is not null to avoid NullPointerException
        if (currentUser != null) {
            // Check if the answers provided match the user's stored answers
            if (currentUser.getAnswer1().equals(passSecurityForm.getAnswer1()) && currentUser.getAnswer2().equals(passSecurityForm.getAnswer2())) {
                log.info("User got security questions right! Redirect to change password");
                return "changePassword"; // Redirect to the change password page
            } else {
                // Provide feedback to the user
                log.info("Answered Questions Wrong");
                model.addAttribute("errorMessage", "Incorrect answers. Please try again.");
                return "passSecurity"; // Return to the security questions page with an error message
            }
        } else {
            log.error("No user found in session.");
            model.addAttribute("errorMessage", "No user found. Please log in.");
            return "redirect:/login"; // Redirect to login if no user is found
        }
    }
}
