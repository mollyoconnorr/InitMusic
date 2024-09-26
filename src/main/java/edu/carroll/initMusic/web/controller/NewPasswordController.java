package edu.carroll.initMusic.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import edu.carroll.initMusic.jpa.model.User; // Ensure you have the correct import for the User model
import edu.carroll.initMusic.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import edu.carroll.initMusic.web.form.NewPasswordForm; // Adjust package as necessary

@Controller
public class NewPasswordController {
    /**
     * Logger object used for logging actions within this controller.
     */
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;


    @GetMapping("/changePassword")
    public String showChangePasswordEmailPage() {
        return "changePassword"; // Thymeleaf template for the security questions page
    }

    @PostMapping("/changePassword")
    public String handleSecuritySubmission(@ModelAttribute NewPasswordForm passwordForm, HttpSession httpSession, Model model) {
        User currentUser = (User) httpSession.getAttribute("currentUser"); // Ensure this attribute is set in the session
        log.info("Security questions submitted for user: {}", currentUser.getUsername());
        userService.updatePassword(currentUser, passwordForm.getNewPassword());
        return "redirect:/login"; // Redirect to login if no user is found
    }
}
