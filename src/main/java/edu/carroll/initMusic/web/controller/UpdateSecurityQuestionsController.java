package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import edu.carroll.initMusic.jpa.model.User;
import jakarta.servlet.http.HttpSession;
import edu.carroll.initMusic.web.form.SecurityQuestionsForm;

@Controller
public class UpdateSecurityQuestionsController {

    /**
     * Logger object used for logging actions within this controller.
     */
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
    public String processUpdateSecurityQuestionsForm (@ModelAttribute SecurityQuestionsForm securityForm, HttpSession httpSession, Model model) {
        User currentUser = (User) httpSession.getAttribute("currentUser"); // Ensure this attribute is set in the session
        if (currentUser != null) {
            final boolean securityQuestionsUpdated = userService.updateUserSecurityQuestions
                    (currentUser, securityForm.getQuestion1(), securityForm.getAnswer1(), securityForm.getQuestion2(), securityForm.getAnswer2());
            if (!securityQuestionsUpdated) {
                model.addAttribute("errorMessage", "Security Question Update failed");
            }
            return "securityQuestionsUpdated"; // Redirect to the password changed confirmation page
        } else {
            log.error("No user found in session.");
            return "redirect:/login"; // Redirect to login, user wasn't found
        }
    }
}
