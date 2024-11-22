package edu.carroll.initMusic.web.controller.securityManagement;

import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.service.userManagement.UserService;
import edu.carroll.initMusic.web.form.securityManagement.SecurityQuestionsForm;
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
 * Controller for handling the submission and display of security questions.
 * This controller manages the process where users set up their security questions
 * during registration, or answer them as part of their account security process.*
 * Features include:
 * - Displaying the security questions form during registration or login.
 * - Handling form submissions for setting or answering security questions.
 * - Validating user input and updating the database with security questions and answers.
 */
@Controller
public class SecurityQuestionsController {

    /** Logger object used for logging actions within this controller. */
    private static final Logger log = LoggerFactory.getLogger(SecurityQuestionsController.class);

    /** Service for handling user-related operations. */
    private final UserService userService;

    /**
     * Constructor for SecurityQuestionsController.
     *
     * @param userService the service responsible for handling user-related operations.
     */
    public SecurityQuestionsController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the form where users can set up or answer security questions.
     * This method is primarily used during user registration or account recovery.
     *
     * @param model   the model to be used in the view.
     * @param request the HTTP request to check the referer header.
     * @return the name of the Thymeleaf template for the security questions view.
     */
    @GetMapping("/securityQuestions")
    public String showSecurityQuestionsForm(Model model, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer == null || !referer.endsWith("/register")) {
            log.warn("User didn't come from the register page, not allowed to access security questions");
            // Redirect to an error page or login page if the referer is invalid
            return "redirect:/login";
        }
        model.addAttribute("securityQuestionsForm", new SecurityQuestionsForm());
        return "securityQuestions";  // Name of the Thymeleaf template (securityQuestions.html)
    }

    /**
     * Handles the submission of the security questions form.
     * Validates the user's input, updates the database with the provided answers,
     * and redirects the user to a confirmation page upon successful submission.
     *
     * @param securityQuestionsForm the form containing the user's answers to the security questions.
     * @param model                 the model to be used in the view.
     * @param session               the current HTTP session to retrieve user data.
     * @return the name of the view or a redirect URL based on the result of the form submission.
     */
    @PostMapping("/securityQuestions")
    public String submitSecurityQuestions(@ModelAttribute SecurityQuestionsForm securityQuestionsForm, Model model, HttpSession session) {

        // Retrieve the current user from the session
        User currentUser = (User) session.getAttribute("currentUser");

        // If no user is found in the session, redirect to login
        if (currentUser == null) {
            log.warn("submitSecurityQuestions: No user found in current HTTP session.");
            return "redirect:/login";
        }

        // Log the submission of security questions
        log.info("submitSecurityQuestions: Security questions submitted for user: {}", currentUser.getUsername());

        // Extract the form data
        final String question1 = securityQuestionsForm.getQuestion1();
        final String question2 = securityQuestionsForm.getQuestion2();
        final String answer1 = securityQuestionsForm.getAnswer1();
        final String answer2 = securityQuestionsForm.getAnswer2();

        // Update the user's security questions and answers
        boolean questionsUpdated = userService.updateUserSecurityQuestions(currentUser, question1, answer1, question2, answer2);

        if (questionsUpdated) {
            // Add the username to the model and clear the session attribute
            model.addAttribute("username", currentUser.getUsername());
            session.removeAttribute("currentUser");
        } else {
            // Handle validation errors (e.g., blank fields)
            model.addAttribute("errorMessage", "One of the security question fields is blank.");
        }
        // Redirect to the confirmation page if the update was successful
        return "userRegistered";  // Thymeleaf template for confirmation page
    }
}

