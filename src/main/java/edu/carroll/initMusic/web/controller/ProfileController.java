package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.config.CustomUserDetails;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controller responsible for handling user profile-related actions such as displaying user details
 * and deleting a user account.
 */
@Controller
public class ProfileController {

    /** Logger for logging */
    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);

    /** User service for operations with user objects */
    private final UserService userService;

    /**
     * Constructor to inject the UserService dependency.
     *
     * @param userService User service to handle user-related operations
     */
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the user's profile page with details such as username, email, and account creation date.
     *
     * @param model Model to pass data to the view
     * @param authentication The current authentication object to get the logged-in user's details
     * @return The name of the view template ("myProfile") to render the user profile page
     */
    @GetMapping("/myProfile")
    public String showMyProfile(Model model, Authentication authentication) {
        // Get the logged-in user from the SecurityContext
        final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        final User currentUser = userDetails.getUser();
        String username = currentUser.getUsername();
        String email = currentUser.getEmail();

        LocalDateTime accountCreation = currentUser.getAccountCreationDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = accountCreation.format(formatter);
        model.addAttribute("accountCreation", formattedDate);

        // Pass the user details to the model
        model.addAttribute("username", username);
        model.addAttribute("email", email);

        return "myProfile"; // Ensure you have a myProfile.html template
    }

    /**
     * Handles the deletion of a user account. Deletes the user based on the logged-in user's email.
     *
     * @param authentication     The current authentication object to get the logged-in user's details
     * @param redirectAttributes Used to pass success or error messages to the view
     * @return A redirect to the appropriate page after the operation
     */
    @PostMapping("/deleteAccount")
    public String deleteUser(Authentication authentication, RedirectAttributes redirectAttributes) {
        // Retrieve the logged-in user's details from the authentication object
        final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        final User user = userDetails.getUser();

        // If the user is authenticated, attempt to delete the account
        if (user != null) {
            String email = user.getEmail(); // Get the logged-in user's email
            boolean isDeleted = userService.deleteByEmail(email);
            if (isDeleted) {
                log.info("User with {} is deleted from initMusic", email);
                // If deletion is successful, show a success message and redirect to logout
                return "redirect:/logout"; // Redirects the user after account deletion
            } else {
                log.warn("User with {} failed to be removed from initMusic", email);
                // If deletion fails, show an error message and redirect back to the profile page
                return "redirect:/myProfile"; // Redirects back to profile if deletion fails
            }
        } else {
            log.error("User doesn't exist");
            return "redirect:/login"; // Redirect to login if not authenticated
        }
    }
}

