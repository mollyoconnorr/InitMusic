package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.service.UserService;
import edu.carroll.initMusic.web.form.SecurityQuestionsForm; // Ensure correct import
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpSession;



@Controller
public class SecurityQuestionsController {
    /**
     * Logger object used for logging actions within this controller.
     */
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService; // Inject UserService


    @GetMapping("/securityQuestions")
    public String showSecurityQuestionsForm(Model model) {
        model.addAttribute("securityQuestionsForm", new SecurityQuestionsForm());
        return "securityQuestions";
    }

    @PostMapping("/securityQuestions")
    public String submitSecurityQuestions(@ModelAttribute SecurityQuestionsForm securityQuestionsForm, HttpSession httpSession, Model model) {
        User currentUser = (User) httpSession.getAttribute("currentUser");
        if (currentUser != null) {
            log.info("Security questions submitted for user: {}", currentUser.getUsername());
            // Call the updated updateUser method, passing the user and the form
            userService.updateUser(currentUser, securityQuestionsForm);
            model.addAttribute("username", currentUser.getUsername());
            return "userRegistered";
        } else {
            log.warn("Security questions answered incorrectly for user: {}", currentUser.getUsername());
            // Add an error message to the model
            model.addAttribute("errorMessage", "Your answers to the security questions were incorrect. Please try again.");
        }
        // Add security questions to the model to display them again
        model.addAttribute("securityQuestionsForm", securityQuestionsForm); // Preserve the submitted answers
        return "securityQuestions"; // Return to the security questions view
    }
}
