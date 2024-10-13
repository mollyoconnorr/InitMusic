package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.service.LoginService;
import edu.carroll.initMusic.service.SongService;
import edu.carroll.initMusic.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.carroll.initMusic.web.form.LoginForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for handling login-related requests.
 * <p>
 * This class manages the login process, including displaying the login form,
 * validating user credentials, and redirecting to success or failure pages.
 * </p>
 */
@Controller
public class LoginController {
    /**
     * Logger object used for logging actions within this controller.
     */
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final LoginService loginService;

    private final UserService userService;

    /**
     * Constructs a LoginController with the specified LoginService.
     *
     * @param loginService the service used for validating user login attempts.
     */
    public LoginController(LoginService loginService, UserService userService) {
        this.userService = userService;
        this.loginService = loginService;
    }

    /**
     * Displays the login page.
     *
     * @param model the model to be used in the view.
     * @return the name of the login view.
     */
    @GetMapping("/login")
    public String loginGet(Model model) {
        log.info("Get login page");
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    /**
     * Processes the login form submission.
     *
     * @param loginForm the login form submitted by the user.
     * @param result    the result of the validation.
     * @param attrs     attributes to be passed to the redirect.
     * @return the name of the view to render.
     */
    @PostMapping("/login")
    public String loginPost(@Valid @ModelAttribute LoginForm loginForm, BindingResult result, RedirectAttributes attrs, HttpSession httpSession,Model model) {
        log.info("User '{}' attempted login", loginForm.getUsername());

        if (result.hasErrors()) {
            log.info("Validation errors: {}", result.getAllErrors());
            return "login";
        }
        if (!loginService.validateUser(loginForm.getUsername(), loginForm.getPassword())) {
            log.info("Username and password don't match for user '{}'", loginForm.getUsername());
            // Add error message to the model
            model.addAttribute("errorMessage", "That username and password don't match.");
            return "login";
        }
        User foundUser = userService.getUser(loginForm.getUsername());
        attrs.addAttribute("username", foundUser.getUsername());
        httpSession.setAttribute("currentUser", foundUser);
        log.info("User '{}' logged in, showing loginSuccess page", foundUser.getUsername());


        return "redirect:/search";
    }

    /**
     * Displays the login success page.
     *
     * @return the name of the login success view.
     */
    @GetMapping("/loginSuccess")
    public String loginSuccess() {
        return "loginSuccess";
    }

    /**
     * Displays the login failure page.
     *
     * @return the name of the login failure view.
     */
    @GetMapping("/loginFailure")
    public String loginFailure() {
        return "loginFailure";
    }
}