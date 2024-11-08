package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.config.CustomUserDetails;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.service.UserService;
import edu.carroll.initMusic.web.form.NewPasswordForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import edu.carroll.initMusic.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



@Controller
public class ProfileController {

    /** User service for operations with user objects */
    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

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
     * Handles the deletion of a user account.
     * @param authentication The current authentication object to get the logged-in user's details.
     * @param redirectAttributes Used to pass success or error messages to the view.
     * @return Redirect to the appropriate page after the operation.
     */
    @PostMapping("/deleteAccount")
    public String deleteUser(Authentication authentication, RedirectAttributes redirectAttributes) {
        final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        final User user = userDetails.getUser();

        if (user != null) {
            String email = user.getEmail(); // Get the logged-in user's username
            boolean isDeleted = userService.deleteByEmail(email);
            if (isDeleted) {
                redirectAttributes.addFlashAttribute("successMessage", "User account deleted successfully.");
                return "redirect:/logout"; // Redirects the user after account deletion
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete user account.");
                return "redirect:/myProfile"; // Redirects back to profile if deletion fails
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "User not authenticated.");
            return "redirect:/login"; // Redirect to login if not authenticated
        }
    }
}
