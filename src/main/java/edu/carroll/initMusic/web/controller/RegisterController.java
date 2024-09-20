package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.web.form.RegistrationForm; // Ensure correct import
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import edu.carroll.initMusic.service.UserService; // Import UserService
import org.springframework.beans.factory.annotation.Autowired; // Import Autowired

/**
 * Controller for handling user registration.
 * <p>
 * This class manages the registration process, including displaying the
 * registration form and processing the submitted registration details.
 * </p>
 */
@Controller
public class RegisterController {

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
    public String registerUser(@ModelAttribute RegistrationForm registrationForm) {
        // Add logic to save the registration details to the database
        userService.saveUser(registrationForm);

        // Redirect to login page after successful registration
        return "redirect:/login";
    }
}