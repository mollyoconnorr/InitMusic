package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.config.CustomUserDetails;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.service.UserService;
import edu.carroll.initMusic.web.form.NewPasswordForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller for handling password changes for users who are already logged in.
 * <p>
 * This controller manages the flow for users to change their passwords securely.
 * It checks the old password, validates the new password, and updates the user's password if valid.
 * </p>
 * @author Molly O'Connor
 *
 * @since October 20, 2024
 */
@Controller
public class ChangePasswordAlreadyLoggedInController {

    /** BCrypt password encoder used for hashing passwords. */
    private final BCryptPasswordEncoder passwordEncoder;

    /** Logger object used for logging actions within this controller. */
    private static final Logger log = LoggerFactory.getLogger(ChangePasswordAlreadyLoggedInController.class);

    /** Service for user-related operations such as updating passwords. */
    private final UserService userService;

    /**
     * Constructs a new ChangePasswordAlreadyLoggedInController.
     *
     * @param userService the service that handles user operations
     * @param passwordEncoder the password encoder for hashing passwords
     */
    public ChangePasswordAlreadyLoggedInController(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Displays the form for changing the password when the user is already logged in.
     *
     * @return the name of the Thymeleaf template for changing the password
     */
    @GetMapping("/changePasswordLoggedIn")
    public String showChangePasswordLoggedIn() {
        return "changePasswordLoggedIn"; // Thymeleaf template for the change password page
    }

    /**
     * Handles the form submission for changing the password.
     * <p>
     * This method verifies that the old password matches the user's current password.
     * If the old password is correct, it updates the password to the new one. If not,
     * it displays an error message.
     * </p>
     * @param passwordForm the form containing the old and new passwords
     * @param authentication the current authentication token, used to retrieve the logged-in user
     * @param model the model used to pass data back to the view
     * @return the view to display next, either a success page or the form with an error
     */
    @PostMapping("/changePasswordLoggedIn")
    public String handleSecuritySubmission(@ModelAttribute NewPasswordForm passwordForm, Authentication authentication, Model model) {
        if (authentication.getPrincipal() != null) {
            //Retrieve the current user
            final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            final User currentUser = userDetails.getUser();

            String storedHashedPassword = currentUser.getHashedPassword(); // Retrieves the hashed password from the user

            // Check if the old password matches the stored hashed password
            if (passwordEncoder.matches(passwordForm.getOldPassword(), storedHashedPassword)) {
                final boolean passwordUpdated = userService.updatePassword(currentUser, passwordForm.getNewPassword());
                if(!passwordUpdated) {
                    log.warn("handleSecuritySubmission: Password update failed for User id#{}",currentUser.getuserID());
                    model.addAttribute("errorMessage", "Password update failed");
                }
                return "passwordChangedLoggedIn"; // Redirect to the password changed confirmation page
            } else {
                // Password mismatch, show error
                log.warn("handleSecuritySubmission: Old password is incorrect for User id#{}",currentUser.getuserID());
                model.addAttribute("error", "Old password is incorrect.");
                return "changePasswordLoggedIn"; // Reload the form with an error message
            }
        } else {
            log.error("handleSecuritySubmission: No user found in session.");
            return "redirect:/login"; // Redirect to login, user wasn't found
        }
    }
}

