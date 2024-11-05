package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.service.UserService;
import edu.carroll.initMusic.web.form.CheckUserEmailForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller for handling password changes via email verification.
 * This controller manages the flow where users can input their email to change their password.
 * If the email is valid, it redirects to the security question validation page.
 *
 * @author Molly O'Connor
 *
 * @since October 8, 2024
 */
@Controller
public class ChangePasswordEmailController {

    /**
     * Logger object used for logging actions within this controller.
     */
    private static final Logger log = LoggerFactory.getLogger(ChangePasswordEmailController.class);

    /** Service for user-related operations such as finding users by email. */
    private final UserService userService;

    /**
     * Constructs a new ChangePasswordEmailController.
     *
     * @param userService the service that handles user operations
     */
    public ChangePasswordEmailController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the email input form for changing the password.
     *
     * This method is invoked when the user requests the password reset page.
     *
     * @return the name of the Thymeleaf template for the email input page
     */
    @GetMapping("/changePasswordEmail")
    public String showChangePasswordEmailPage() {
        log.info("Request mapping for /changePasswordEmail GET method is called.");
        return "changePasswordEmail"; // Thymeleaf template for the email input page
    }

    /**
     * Handles the email form submission for password change.
     * <p>
     * This method checks if the email exists in the system. If the email is valid,
     * it redirects the user to the security questions page. Otherwise, it shows an error.
     * </p>
     * @param emailForm the form containing the email to check
     * @param authentication the authentication token
     * @param model the model used to pass data back to the view
     * @return the view to display next, either the security questions page or the email input page with an error
     */
    @PostMapping("/changePasswordEmail")
    public String handleEmailSubmission(@ModelAttribute CheckUserEmailForm emailForm, Authentication authentication, Model model) {
        User user = userService.findByEmail(emailForm.getEmail()); // Method to find user by email
        if (user != null && authentication.getPrincipal() != null) {
            // Pass user's security questions to the model for rendering on the next page
            model.addAttribute("question1", user.getQuestion1());
            model.addAttribute("question2", user.getQuestion2());
            return "passSecurity"; // Thymeleaf template for the security questions
        } else {
            // Log the situation and show an error message if the email isn't found
            log.info("handleEmailSubmission: No user found with email: {}", emailForm.getEmail());
            model.addAttribute("errorMessage", "That email doesn't exist in our system. Please try again.");
            return "changePasswordEmail"; // Reload the form with an error message
        }
    }
}

