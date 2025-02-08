package com.nnk.springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for handling home page requests and redirection to admin page.
 */
@Controller
@Slf4j
public class HomeController {

    /**
     * Displays the home page.
     * 
     * @param model the model to add attributes to the view
     * @return the view name for the home page
     */
    @RequestMapping("/")
    public String home(Model model) {
        log.info("Calling GET /");
        return "home";
    }

    /**
     * Redirects to the bid list page for admin users.
     * 
     * @param model the model to add attributes to the view
     * @return the redirection to the bid list page
     */
    @RequestMapping("/admin/home")
    public String adminHome(Model model) {
        log.info("Calling GET /admin/home - Redirecting to /bidList/list");
        return "redirect:/bidList/list";
    }
}
