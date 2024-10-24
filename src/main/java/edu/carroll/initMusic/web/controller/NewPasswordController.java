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

/**
 * Controller for handling password change requests for users who have successfully
 * passed security checks and are changing their password.
 *
 * @author Molly O'Connor
 *
 * @since October 8, 2024
 */
@Controller
public class NewPasswordController {

    /**
     * Logger object used for logging actions within this controller.
     */
    private static final Logger log = LoggerFactory.getLogger(NewPasswordController.class);

    /** Service for user-related operations such as updating user passwords. */
    private final UserService userService;

    /**
     * Constructs a NewPasswordController with the specified UserService.
     *
     * @param userService the service used for user-related operations such as updating passwords.
     */
    public NewPasswordController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the password change form page.
     * <p>
     * This method is invoked when a user requests to change their password.
     * It serves the page containing the form where users can input their new password.
     * </p>
     *
     * @return the name of the Thymeleaf template for the password change page.
     */
    @GetMapping("/changePassword")
    public String showChangePasswordEmailPage() {
        return "changePassword";  // Thymeleaf template for the password change page
    }

    /**
     * Processes the password change form submission.
     * <p>
     * This method handles the POST request when a user submits their new password.
     * It checks if a user is stored in the session, and if so, updates their password.
     * If no user is found, it redirects the user to the login page.
     * </p>
     *
     * @param passwordForm the form containing the user's new password.
     * @param httpSession  the HTTP session for retrieving the current user.
     * @param model        the model to store attributes for the view.
     * @return the name of the view to render (either the success page or a redirect).
     */
    @PostMapping("/changePassword")
    public String handleSecuritySubmission(@ModelAttribute NewPasswordForm passwordForm, HttpSession httpSession, Model model) {
        User currentUser = (User) httpSession.getAttribute("currentUser");  // Retrieve the current user from the session

        if (currentUser != null) {
            log.info("Password changed for {}", currentUser.getUsername());
            final boolean passwordUpdated = userService.updatePassword(currentUser, passwordForm.getNewPassword());
            if(!passwordUpdated) {
                model.addAttribute("errorMessage", "Password update failed");
            }
            return "passwordChanged";  // Redirect to the password changed confirmation page
        } else {
            log.error("No user found in session.");
            return "redirect:/login";  // Redirect to the login page if no user is found in the session
        }
    }
}
