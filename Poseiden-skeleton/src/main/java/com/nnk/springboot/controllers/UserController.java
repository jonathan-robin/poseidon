package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.UserService;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for managing users in the application.
 */
@Controller
@Slf4j
public class UserController {
	
    @Autowired
    private UserService userService;

    /**
     * Displays the list of users.
     * 
     * @param model The model to pass data to the view.
     * @return The view with the list of users.
     */
    @RequestMapping("/user/list")
    public String home(Model model) {
        log.info("Calling GET /user/list");
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }

    /**
     * Displays the form for adding a new user.
     * 
     * @param user The user object to be added.
     * @return The view with the add user form.
     */
    @GetMapping("/user/add")
    public String addUser(User user) {
        log.info("Calling GET /user/add with user: {}", user);
        return "user/add";
    }

    /**
     * Validates and saves a new user.
     * 
     * @param user   The user object to validate and save.
     * @param result The validation result for the user data.
     * @param model  The model to pass data to the view.
     * @return The list view if validation succeeds, otherwise returns the add user form.
     */
    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result, Model model) {
        log.info("Calling POST /user/validate with user: {}", user);

        String password = user.getPassword();
        if (password != null && !Pattern.matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{8,}$", password)) {
            result.rejectValue("password", "error.password", "Password must contain at least 8 characters, one uppercase letter, one symbol, and one number.");
        }

        if (!result.hasErrors()) {
        	userService.saveUser(user);
            model.addAttribute("users", userService.findAll());
            return "redirect:/user/list";
        }
        return "user/add";
    }

    /**
     * Displays the form to update an existing user.
     * 
     * @param id    The ID of the user to update.
     * @param model The model to pass data to the view.
     * @return The view with the update user form.
     */
    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        log.info("Calling GET /user/update/{} with id: {}", id);

    	User user = userService.findById(id);
        user.setPassword("");  // Clear the password before displaying it
        model.addAttribute("user", user);
        return "user/update";
    }

    /**
     * Updates the information of an existing user.
     * 
     * @param id     The ID of the user to update.
     * @param user   The updated user object.
     * @param result The validation result for the user data.
     * @param model  The model to pass data to the view.
     * @return The list view after updating the user.
     */
    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result, Model model) {
        log.info("Calling POST /user/update/{} with user: {}", id, user);
        
        String password = user.getPassword();
        if (password != null && !Pattern.matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{8,}$", password)) {
            result.rejectValue("password", "error.password", "Password must contain at least 8 characters, one uppercase letter, one symbol, and one number.");
        }
        
        if (!result.hasErrors()) {
            userService.saveUser(user, id);
            model.addAttribute("users", userService.findAll());
            return "redirect:/user/list";
        }

        return "user/update";
    }

    /**
     * Deletes a user.
     * 
     * @param id  The ID of the user to delete.
     * @param model The model to pass data to the view.
     * @return The list view after the user is deleted.
     * @throws Exception If an error occurs during deletion.
     */
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) throws Exception {
        log.info("Calling GET /user/delete/{} with id: {}", id);

        userService.deleteUserById(id);
        model.addAttribute("users", userService.findAll());
        return "redirect:/user/list";
    }
}
