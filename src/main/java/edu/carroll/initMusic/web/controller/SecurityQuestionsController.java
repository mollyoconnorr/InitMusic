package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.config.CustomUserDetails;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.service.UserService;
import edu.carroll.initMusic.web.form.SecurityQuestionsForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * Controller for handling the submission and display of security questions.
 * This controller manages the process where users set up their security questions
 * during registration, or answer them as part of their account security process.
 *
 * @author Molly O'Connor
 *
 * @since October 8, 2024
 */
@Controller
public class SecurityQuestionsController {
    /** Logger object used for logging actions within this controller. */
    private static final Logger log = LoggerFactory.getLogger(SecurityQuestionsController.class);

    /** User Service */
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
     *
     * @param model the model to be used in the view.
     * @return the name of the security questions view.
     */
    @GetMapping("/securityQuestions")
    public String showSecurityQuestionsForm(Model model) {
        model.addAttribute("securityQuestionsForm", new SecurityQuestionsForm());
        return "securityQuestions";  // Name of the Thymeleaf template (securityQuestions.html)
    }

    /**
     * Handles the submission of the security questions form.
     * <p>
     * This method checks if the current user exists in the session, logs the submission,
     * and updates the user's security questions with the provided answers.
     * If no user is found in the session, or there are validation issues, an error message is displayed.
     * </p>
     *
     * @param securityQuestionsForm the form containing the user's answers to the security questions.
     * @param authentication        the current authentication token, if any user is attached
     * @param model                 the model to be used in the view.
     * @return the name of the view or a redirect URL based on the result of the form submission.
     */
    @PostMapping("/securityQuestions")
    public String submitSecurityQuestions(@ModelAttribute SecurityQuestionsForm securityQuestionsForm, Authentication authentication, Model model) {

        if (authentication.getPrincipal() != null) {
            //Retrieve the current user
            final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            final User currentUser = userDetails.getUser();
            log.info("submitSecurityQuestions: Security questions submitted for user: {}", currentUser.getUsername());
            // Call the updated updateUser method, passing the user and the form
            final String question1 = securityQuestionsForm.getQuestion1();
            final String question2 = securityQuestionsForm.getQuestion2();
            final String answer1 = securityQuestionsForm.getAnswer1();
            final String answer2 = securityQuestionsForm.getAnswer2();
            final boolean questionsUpdated = userService.updateUserSecurityQuestions(currentUser,question1,answer1,question2,answer2);
            // Redirect to the user registered confirmation page
            if(questionsUpdated){
                model.addAttribute("username", currentUser.getUsername());
            }else{
                model.addAttribute("errorMessage","One of the security question fields is blank");
            }
            return "userRegistered";  // Redirect to the user registered confirmation page
        } else {
            log.warn("submitSecurityQuestions: No user found in current httpSession");
            // Add an error message to the model
            model.addAttribute("errorMessage", "No user found in current httpSession");
        }

        // Add security questions to the model to display them again
        model.addAttribute("securityQuestionsForm", securityQuestionsForm); // Preserve the submitted answers
        return "securityQuestions"; // Return to the security questions view with the error message
    }
}

