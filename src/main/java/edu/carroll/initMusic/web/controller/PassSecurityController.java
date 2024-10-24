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
import jakarta.servlet.http.HttpSession;

/**
 * Controller for handling user security questions validation.
 * This class manages the process of verifying the user's answers to
 * pre-set security questions and redirecting to the password change page upon success.
 *
 * @author Molly O'Connor
 *
 * @since October 8, 2024
 */
@Controller
public class PassSecurityController {

    /**
     * Logger object used for logging actions within this controller.
     */
    private static final Logger log = LoggerFactory.getLogger(PassSecurityController.class);

    /** Service for user-related operations. */
    private final UserService userService;

    /**
     * Constructs a PassSecurityController with the specified UserService.
     *
     * @param userService the service used for user-related operations, such as verifying answers.
     */
    public PassSecurityController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the security questions page.
     * <p>
     * This method serves the page where users can answer their security questions
     * to verify their identity.
     * </p>
     *
     * @return the name of the Thymeleaf template for the security questions page.
     */
    @GetMapping("/passSecurity")
    public String showChangePasswordEmailPage() {
        return "passSecurity";  // Thymeleaf template for the security questions page
    }

    /**
     * Processes the security question form submission.
     * <p>
     * This method handles the POST request when a user submits their answers to the
     * security questions. It checks if the answers match the stored answers for the user.
     * If the answers are correct, the user is redirected to the password change page.
     * Otherwise, an error message is displayed, and the user remains on the security
     * questions page.
     * </p>
     *
     * @param passSecurityForm the form containing the user's answers to the security questions.
     * @param httpSession      the HTTP session for retrieving the current user.
     * @param model            the model to store attributes for the view.
     * @return the name of the view to render (either the password change page or the security questions page with errors).
     */
    @PostMapping("/passSecurity")
    public String handleSecuritySubmission(@ModelAttribute PassSQuestionsForm passSecurityForm, HttpSession httpSession, Model model) {
        User currentUser = (User) httpSession.getAttribute("currentUser");  // Retrieve the current user from the session

        // Ensure that currentUser is not null to avoid NullPointerException
        if (currentUser != null) {
            // Check if the answers provided match the user's stored answers
            if (currentUser.getAnswer1().equals(passSecurityForm.getAnswer1()) && currentUser.getAnswer2().equals(passSecurityForm.getAnswer2())) {
                log.info("User got security questions right. Redirect to change password");
                return "changePassword";  // Redirect to the change password page
            } else {
                // Provide feedback to the user
                log.info("User answered Security Questions Wrong");
                model.addAttribute("question1", currentUser.getQuestion1());  // Assuming you have this method
                model.addAttribute("question2", currentUser.getQuestion2());
                model.addAttribute("errorMessage", "Incorrect answers. Please try again.");
                return "passSecurity";  // Return to the security questions page with an error message
            }
        } else {
            log.error("No user found in session.");
            model.addAttribute("errorMessage", "No user found. Please log in.");
            return "redirect:/login";  // Redirect to login if no user is found in the session
        }
    }
}

