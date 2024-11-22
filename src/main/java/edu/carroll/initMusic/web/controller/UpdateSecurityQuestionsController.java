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
 * Handles mappings for updating a users security questions
 *
 * @author Molly O'Connor
 * @since October 29, 2024
 */
@Controller
public class UpdateSecurityQuestionsController {

    /** Logger object used for logging actions within this controller. */
    private static final Logger log = LoggerFactory.getLogger(UpdateSecurityQuestionsController.class);

    /** Service for user-related operations such as updating passwords. */
    private final UserService userService;

    /**
     * Constructs a new ChangePasswordAlreadyLoggedInController.
     *
     * @param userService the service that handles user operations
     */
    public UpdateSecurityQuestionsController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/updateSecurityQuestions")
    public String showUpdateSecurityQuestions(Model model) {
        model.addAttribute("securityQuestionsForm", new SecurityQuestionsForm());
        return "updateSecurityQuestions"; // Ensure this matches the template name
    }

    @PostMapping("/updateSecurityQuestions")
    public String processUpdateSecurityQuestionsForm(@ModelAttribute SecurityQuestionsForm securityForm, Authentication authentication, Model model) {
        if (authentication.getPrincipal() != null) {
            //Retrieve the current user
            final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            final User currentUser = userDetails.getUser();

            final boolean securityQuestionsUpdated = userService.updateUserSecurityQuestions
                    (currentUser, securityForm.getQuestion1(), securityForm.getAnswer1(), securityForm.getQuestion2(), securityForm.getAnswer2());
            if (!securityQuestionsUpdated) {
                model.addAttribute("errorMessage", "Security Question Update failed");
            }
            return "securityQuestionsUpdated"; // Redirect to the password changed confirmation page
        }
        model.addAttribute("errorMessage", "No user found in authentication principal");
        return "updateSecurityQuestions";
    }
}
