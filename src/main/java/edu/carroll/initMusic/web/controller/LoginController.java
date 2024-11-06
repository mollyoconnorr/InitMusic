package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.web.form.LoginForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Controller for handling login-related requests.
 * This class manages the login process, including displaying the login form,
 * validating user credentials, and redirecting to success or failure pages.
 *
 * @author Molly O'Connor
 *
 * @since September 8, 2024
 */
@Controller
@EnableWebSecurity
public class LoginController {
    /** Logger object used for logging actions within this controller. */
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    /**
     * Displays the login page.
     * <p>
     * This method is invoked when a user requests the login page. It initializes
     * the login form and adds it to the model.
     * </p>
     *
     * @param error Any errors that have occurred 
     * @param model the model to be used in the view.
     * @return the name of the login view (Thymeleaf template).
     */
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        log.info("loginGet: Get login page");
        if (error != null) {
            log.warn("loginGet: Error while logging in, Invalid username or password.");
            model.addAttribute("errorMsg", "Invalid username or password. Please try again.");
        }
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }
}