package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.web.form.PassSQuestionsForm;
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
 * Controller for handling user security questions validation.
 * <p>
 * This class manages the process of verifying the user's answers to
 * pre-set security questions and redirecting to the password change page upon success.
 * </p>
 *
 * <p>
 * This controller ensures that only users who correctly answer their security questions
 * can proceed to change their password.
 * </p>
 *
 * <p>
 * Handles both displaying the security questions page and processing
 * the user's answers.
 * </p>
 *
 * @author Molly O'Connor
 * @since October 8, 2024
 */
@Controller
public class PassSecurityController {

    /**
     * Logger object used for logging actions within this controller.
     */
    private static final Logger log = LoggerFactory.getLogger(PassSecurityController.class);

    /**
     * Displays the security questions page.
     * <p>
     * This method serves the page where users can answer their security questions
     * to verify their identity. It checks the referrer to ensure that users arrive
     * at this page through the intended flow.
     * </p>
     *
     * @param request the HTTP servlet request, used to obtain the referrer header.
     * @return the name of the Thymeleaf template for the security questions page,
     * or a redirect to the login page if the referrer is invalid.
     */
    @GetMapping("/passSecurity")
    public String showChangePasswordEmailPage(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer == null || !referer.endsWith("/changePasswordEmail")) {
            // Redirect to an error page or login page
            return "redirect:/login";
        }
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
     * @param model            the model to store attributes for the view.
     * @param session          the HTTP session, used to retrieve the currently logged-in user.
     * @return the name of the view to render (either the password change page or the security questions page with errors).
     */
    @PostMapping("/passSecurity")
    public String handleSecuritySubmission(@ModelAttribute PassSQuestionsForm passSecurityForm, Model model, HttpSession session) {

        User currentUser = (User) session.getAttribute("currentUser");

        // Ensure that currentUser is not null to avoid NullPointerException
        if (currentUser != null) {
            // Check if the answers provided match the user's stored answers
            if (currentUser.getAnswer1().equals(passSecurityForm.getAnswer1()) && currentUser.getAnswer2().equals(passSecurityForm.getAnswer2())) {
                log.info("handleSecuritySubmission: {} got security questions right. Redirect to change password", currentUser.getUsername());
                return "changePassword";  // Redirect to the change password page
            } else {
                // Provide feedback to the user
                log.info("handleSecuritySubmission: User answered Security Questions Wrong");
                model.addAttribute("question1", currentUser.getQuestion1());  // Assuming you have this method
                model.addAttribute("question2", currentUser.getQuestion2());
                model.addAttribute("errorMessage", "Incorrect answers. Please try again.");
                return "passSecurity";  // Return to the security questions page with an error message
            }
        } else {
            log.error("handleSecuritySubmission: No user found in session.");
            model.addAttribute("errorMessage", "No user found. Please log in.");
            return "redirect:/login";  // Redirect to login if no user is found in the session
        }
    }
}