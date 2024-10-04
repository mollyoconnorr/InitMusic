package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.service.LoginService;
import edu.carroll.initMusic.web.form.RegistrationForm; // Ensure correct import
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import edu.carroll.initMusic.service.UserService; // Import UserService
import org.springframework.beans.factory.annotation.Autowired; // Import Autowired
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.http.HttpSession;



/**
 * Controller for handling user registration.
 * <p>
 * This class manages the registration process, including displaying the
 * registration form and processing the submitted registration details.
 * </p>
 */
@Controller
public class RegisterController {
    /**
     * Logger object used for logging actions within this controller.
     */
    private static final Logger log = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private UserService userService; // Inject UserService

    /**
     * Displays the registration form.
     *
     * @param model the model to be used in the view.
     * @return the name of the registration view.
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "register";  // Name of your Thymeleaf HTML template (register.html)
    }

    /**
     * Processes the registration form submission.
     *
     * @param registrationForm the registration form submitted by the user.
     * @return a redirect URL to the login page after successful registration.
     */
    @PostMapping("/register")
    public String registerUser(@ModelAttribute RegistrationForm registrationForm, HttpSession httpSession) {
        String username = registrationForm.getUsername();
        String email = registrationForm.getEmail();

        log.info("Attempting to register user with username: {} and email: {}", username, email);

        if (userService.uniqueUser(username, email)) {
            log.info("Unique user. Proceeding with registration for {}", username);
            User currentUser = userService.saveUser(registrationForm); // Make sure this returns the User object

            log.info("Storing current user in session: {}", currentUser);
            // Store user in session
            httpSession.setAttribute("currentUser", currentUser);
            return "redirect:/securityQuestions";
        } else {
            log.info("Username or email already exists for {}", username);
            return "redirect:/login";
        }
    }
}