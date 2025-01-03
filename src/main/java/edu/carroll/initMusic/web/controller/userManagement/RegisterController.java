package edu.carroll.initMusic.web.controller.userManagement;

import edu.carroll.initMusic.MethodOutcome;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.service.userManagement.UserService;
import edu.carroll.initMusic.web.form.userManagement.RegistrationForm;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller for handling user registration.
 * This class manages the registration process, including displaying the
 * registration form and processing the submitted registration details.
 */
@Controller
public class RegisterController {
    /**
     * Logger object used for logging actions within this controller.
     */
    private static final Logger log = LoggerFactory.getLogger(RegisterController.class);

    private final UserService userService; // Inject UserService

    /**
     * Constructor for RegisterController.
     *
     * @param userService the service responsible for handling user-related operations.
     */
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

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
     * This method checks if the submitted username and email are unique. If not, it
     * either redirects to an error page or shows an error message on the registration form.
     * If both username and email are unique, the user is saved, and their details are stored
     * in the session.
     *
     * @param registrationForm the registration form submitted by the user.
     * @param model            the model to be used in the view.
     * @return the name of the view or a redirect URL based on the registration result.
     */
    @PostMapping("/register")
    public String registerUser(@ModelAttribute RegistrationForm registrationForm, Model model, HttpSession session) {
        final String username = registrationForm.getUsername();
        final String email = registrationForm.getEmail();

        log.info("registerUser: Attempting to register user with username: {} and email: {}", username, email);

        final MethodOutcome emailUnique = userService.uniqueEmail(email);
        final MethodOutcome usernameUnique = userService.uniqueUserName(username);

        // Check if the email already exists
        if (emailUnique.equals(MethodOutcome.EMAIL_ALREADY_EXISTS)) {
            log.warn("registerUser: Email already exists {}", email);
            // Redirect to a new page for users with existing emails
            return "emailTaken"; // Redirect to email taken page
        }
        //UniqueEmail failed for some other reason
        if(emailUnique.failed()){
            log.warn("registerUser: Error checking for unique email {}, {}", email,emailUnique.getMessage());
            model.addAttribute("errorMessage", emailUnique.getMessage());
            model.addAttribute("registrationForm", new RegistrationForm());
            return "register";
        }
        // Check if the username is already taken
        if (usernameUnique.failed()) {
            log.warn("registerUser: Error checking for unique username {}, {}", username,usernameUnique.getMessage());
            // Set an error message
            if (usernameUnique.equals(MethodOutcome.USER_ALREADY_EXISTS)) {
                model.addAttribute("errorMessage", "Username is taken. Please try a new one.");
            } else {
                model.addAttribute("errorMessage", usernameUnique.getMessage());
            }
            // Return to the registration form with the error message
            model.addAttribute("registrationForm", registrationForm); // Preserve the submitted data
            return "register"; // Return to the registration view
        }
        // If both the username and email are unique
        log.info("registerUser: Unique user. Proceeding with registration for {}", username);

        final String firstName = registrationForm.getFirstName();
        final String lastName = registrationForm.getLastName();
        final String password = registrationForm.getPassword();

        // Save the user
        final User currentUser = userService.saveUser(username, password, email, firstName, lastName);
        session.setAttribute("currentUser", currentUser);
        return "redirect:/securityQuestions"; // Redirect to security questions page
    }
}
