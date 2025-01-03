package edu.carroll.initMusic.web.controller.securityManagement;

import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.service.userManagement.UserService;
import edu.carroll.initMusic.web.form.securityManagement.CheckUserEmailForm;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller for handling password changes via email verification.
 * This controller manages the flow where users can input their email to change their password.
 * If the email is valid, it redirects to the security question validation page.
 */
@Controller
public class ChangePasswordEmailController {

    /**
     * Logger object used for logging actions within this controller.
     */
    private static final Logger log = LoggerFactory.getLogger(ChangePasswordEmailController.class);

    /**
     * Service for user-related operations such as finding users by email.
     */
    private final UserService userService;

    /**
     * Constructs a new ChangePasswordEmailController.
     *
     * @param userService the service that handles user operations such as finding users by email.
     */
    public ChangePasswordEmailController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the email input form for changing the password.
     * This method is invoked when the user requests the password reset page.
     *
     * @return the name of the Thymeleaf template for the email input page.
     */
    @GetMapping("/changePasswordEmail")
    public String showChangePasswordEmailPage() {
        return "changePasswordEmail";  // Thymeleaf template for the email input page
    }

    /**
     * Handles the email form submission for password change.
     * This method checks if the email exists in the system. If the email is valid,
     * it redirects the user to the security questions page. Otherwise, it shows an error.
     *
     * @param emailForm the form containing the email to check.
     * @param model     the model used to pass data back to the view.
     * @param session   the HTTP session used to store the current user data.
     * @return the view to display next, either the security questions page or the email input page with an error.
     */
    @PostMapping("/changePasswordEmail")
    public String handleEmailSubmission(@ModelAttribute CheckUserEmailForm emailForm, Model model, HttpSession session) {
        User currentUser = userService.findByEmail(emailForm.getEmail()); // Method to find user by email
        if (currentUser != null) {
            // Check if the user has security questions set
            if (currentUser.getAnswer1() == null || currentUser.getAnswer1().isEmpty() ||
                    currentUser.getAnswer2() == null || currentUser.getAnswer2().isEmpty() ||
                    currentUser.getQuestion1() == null || currentUser.getQuestion1().isEmpty() ||
                    currentUser.getQuestion2() == null || currentUser.getQuestion2().isEmpty()) {
                // Display error if no security questions are set
                log.warn("handleEmailSubmission: User {} doesn't have any security questions available", emailForm.getEmail());
                model.addAttribute("errorMessage", "When you created your account you never created security questions so you are unable to change your password. Please make a new account or email us (moconnor@carroll.edu, nclouse@carroll.edu) to reset your password.");
                return "changePasswordEmail"; // Reload the form with an error message
            } else {
                // Pass user's security questions to the model for rendering on the next page
                model.addAttribute("question1", currentUser.getQuestion1());
                model.addAttribute("question2", currentUser.getQuestion2());
                session.setAttribute("currentUser", currentUser);
                log.info("handleEmailSubmission: User {} has security questions available", emailForm.getEmail());
                return "passSecurity";  // Redirect to the security question validation page
            }
        } else {
            // Log the situation and show an error message if the email isn't found
            log.warn("handleEmailSubmission: No user found with email: {}", emailForm.getEmail());
            model.addAttribute("errorMessage", "That email doesn't exist in our system. Please try again, or register a new account!");
            return "changePasswordEmail";  // Reload the form with an error message
        }
    }
}