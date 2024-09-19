package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.service.LoginService;
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

@Controller
public class LoginController {
    /**
     * Logger object used for logging
     */
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }
    @GetMapping("/login")
    public String loginGet(Model model) {
        log.info("Get login page");
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(@Valid @ModelAttribute LoginForm loginForm, BindingResult result, RedirectAttributes attrs) {
        log.info("User '{}' attempted login",loginForm.getUsername());

        if (result.hasErrors()) {
            log.info("Validation errors ,{}", result.getAllErrors());
            return "login";
        }
        if (!loginService.validateUser(loginForm.getUsername(),loginForm.getPassword())) {
            log.info("Username and password don't match for user '{}'", loginForm.getUsername());
            result.addError(new ObjectError("globalError", "Username and password do not match known users"));
            return "login";
        }
        attrs.addAttribute("username", loginForm.getUsername());
        log.info("User '{}' logged in, showing loginSuccess page", loginForm.getUsername());
        return "redirect:/loginSuccess";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess() {
        return "loginSuccess";
    }

    @GetMapping("/loginFailure")
    public String loginFailure() {
        return "loginFailure";
    }
}
