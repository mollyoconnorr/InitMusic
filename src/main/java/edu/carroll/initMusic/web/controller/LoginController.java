package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.service.LoginService;
import edu.carroll.initMusic.service.UserService;
import edu.carroll.initMusic.web.form.LoginForm;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


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

    /**
     * Logger object used for logging actions within this controller.
     */
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    /** Service used for validating login credentials. */
    private final LoginService loginService;

    /** Service for user-related operations, such as retrieving users. */
    private final UserService userService;

    /**
     * Constructs a LoginController with the specified services.
     *
     * @param loginService the service used for validating user login attempts.
     * @param userService the service for user operations, such as fetching user details.
     */
    public LoginController(LoginService loginService, UserService userService) {
        this.userService = userService;
        this.loginService = loginService;
    }

    /**
     * Displays the login page.
     * <p>
     * This method is invoked when a user requests the login page. It initializes
     * the login form and adds it to the model.
     * </p>
     *
     * @param model the model to be used in the view.
     * @return the name of the login view (Thymeleaf template).
     */
    @GetMapping("/login")
    public String loginGet(Model model) {
        log.info("loginGet: Get login page");
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    /**
     * Processes the login form submission.
     * <p>
     * This method handles POST requests when a user submits the login form. It checks
     * the validity of the submitted form and validates the user's credentials.
     * On success, it redirects to the search page; on failure, it reloads the login page with an error.
     * </p>
     *
     * @param loginForm the login form submitted by the user.
     * @param result    the result of the form validation.
     * @param attrs     attributes to be passed to the redirect.
     * @param httpSession the HTTP session for storing the authenticated user.
     * @param model     the model to add error messages, if necessary.
     * @return the name of the view to render.
     */
    @PostMapping("/login")
    public String loginPost(@Valid @ModelAttribute LoginForm loginForm, BindingResult result,
                            RedirectAttributes attrs, HttpSession httpSession, Model model) {
        return "redirect:/search";  // Redirect to the search page after successful login
    }

    /**
     * Displays the login success page.
     * <p>
     * This method is invoked when the user successfully logs in. It renders
     * the login success view.
     * </p>
     *
     * @return the name of the login success view (Thymeleaf template).
     */
    @GetMapping("/loginSuccess")
    public String loginSuccess() {
        return "loginSuccess";
    }

    /**
     * Displays the login failure page.
     * <p>
     * This method is invoked when a login attempt fails. It renders the login
     * failure view.
     * </p>
     *
     * @return the name of the login failure view (Thymeleaf template).
     */
    @GetMapping("/loginFailure")
    public String loginFailure() {
        return "loginFailure";
    }
}