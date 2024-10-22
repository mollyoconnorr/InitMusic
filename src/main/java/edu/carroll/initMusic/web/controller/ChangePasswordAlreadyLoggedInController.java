package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import edu.carroll.initMusic.jpa.model.User; // Ensure you have the correct import for the User model
import jakarta.servlet.http.HttpSession;
import edu.carroll.initMusic.web.form.NewPasswordForm; // Adjust package as necessary

@Controller
public class ChangePasswordAlreadyLoggedInController {
    /**
     * Logger object used for logging actions within this controller.
     */
    private static final Logger log = LoggerFactory.getLogger(NewPasswordController.class);

    private final UserService userService;

    public ChangePasswordAlreadyLoggedInController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/changePasswordLoggedIn")
    public String showChangePasswordLoggedIn() {
        return "changePasswordLoggedIn"; // Thymeleaf template for the security questions page
    }

    @PostMapping("/changePasswordLoggedIn")
    public String handleSecuritySubmission(@ModelAttribute NewPasswordForm passwordForm, HttpSession httpSession, Model model) {
        User currentUser = (User) httpSession.getAttribute("currentUser"); // Ensure this attribute is set in the session
        if (currentUser != null) {
            log.info("Password changed for {}", currentUser.getUsername());
            userService.updatePassword(currentUser, passwordForm.getNewPassword());
            return "passwordChangedLoggedIn"; // Redirect to the password changed page
        } else {
            log.error("No user found in session.");
            return "redirect:/login"; // Redirect to login, user wasn't found
        }
    }
}
