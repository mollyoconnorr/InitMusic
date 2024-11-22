package edu.carroll.initMusic.web.controller.securityManagement;

import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.service.userManagement.UserService;
import edu.carroll.initMusic.web.form.securityManagement.NewPasswordForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * Controller for handling password change requests for users who have successfully
 * passed security checks and are changing their password.
 */
@Controller
public class ChangePasswordController {

    /** Logger object used for logging actions within this controller. */
    private static final Logger log = LoggerFactory.getLogger(ChangePasswordController.class);

    /** Service for user-related operations such as updating user passwords. */
    private final UserService userService;

    /**
     * Constructs a NewPasswordController with the specified UserService.
     *
     * @param userService the service used for user-related operations such as updating passwords.
     */
    public ChangePasswordController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the password change form page.
     * This method is invoked when a user requests to change their password.
     * It serves the page containing the form where users can input their new password.
     *
     * @param request the HTTP request object to retrieve the referer header.
     * @return the name of the Thymeleaf template for the password change page.
     */
    @GetMapping("/changePassword")
    public String showChangePasswordEmailPage(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer == null || !referer.endsWith("/passSecurity")) {
            // Redirect to an error page or login page if the user did not come from the expected page
            return "redirect:/login";
        }
        return "changePassword";  // Thymeleaf template for the password change page
    }

    /**
     * Processes the password change form submission.
     * This method handles the POST request when a user submits their new password.
     * It checks if a user is stored in the session, and if so, updates their password.
     * If no user is found, it redirects the user to the login page.
     *
     * @param passwordForm the form containing the user's new password.
     * @param session      the HTTP session object that holds the current user's session data.
     * @param model        the model to store attributes for the view.
     * @return the name of the view to render (either the success page or a redirect).
     */
    @PostMapping("/changePassword")
    public String handleSecuritySubmission(@ModelAttribute NewPasswordForm passwordForm, HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser != null) {
            // Retrieve the current user
            log.info("handleSecuritySubmission: Password changed for {}", currentUser.getUsername());
            final boolean passwordUpdated = userService.updatePassword(currentUser, passwordForm.getNewPassword());
            if (!passwordUpdated) {
                model.addAttribute("errorMessage", "Password update failed");
            }
            return "passwordChanged";  // Redirect to the password changed confirmation page
        } else {
            log.error("handleSecuritySubmission: No user found in session.");
            return "redirect:/login";  // Redirect to the login page if no user is found in the session
        }
    }
}