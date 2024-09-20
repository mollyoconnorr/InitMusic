package edu.carroll.initMusic;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

/**
 * MainController handles requests for the root URL of the application.
 * <p>
 * This controller is responsible for serving the index page when
 * users navigate to the root URL ("/") of the application.
 * </p>
 */
@Controller
public class MainController {

    /**
     * Handles GET requests to the root URL ("/").
     *
     * @return the name of the view to be rendered, which is "index".
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }
}