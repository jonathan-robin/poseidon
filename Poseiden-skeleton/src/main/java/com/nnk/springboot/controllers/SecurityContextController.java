package com.nnk.springboot.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for managing security-related actions such as retrieving the authenticated user.
 */
@RestController
@Slf4j
public class SecurityContextController {

    /**
     * Retrieves the currently logged-in user's name and roles.
     * 
     * @return a string representing the logged-in user and their roles, or a message indicating no user is logged in
     */
    @GetMapping("/user")
    public String getUser() {
        log.info("Calling GET /user to retrieve the logged-in user");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {
            log.info("User {} is authenticated with roles: {}", authentication.getName(), authentication.getAuthorities());
            return "Logged-in user: " + authentication.getName() + " role: " + authentication.getAuthorities();
        } else {
            log.info("No user is logged in");
            return "No user logged in";
        }
    }
}
