package edu.carroll.initMusic.web.controller;

import edu.carroll.initMusic.web.form.RegistrationForm; // Ensure correct import
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "register";  // Name of your Thymeleaf HTML template (register.html)
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute RegistrationForm registrationForm) {
        System.out.println("User '" + registrationForm.getName() + "' registered with email '" + registrationForm.getEmail() + "'");
        // Add logic to save the registration details to the database

        // Redirect to login page after successful registration
        return "redirect:/login";
    }
}
