package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for managing ratings.
 */
@Controller
@Slf4j
public class RatingController {

    @Autowired
    private RatingService ratingService;

    /**
     * Retrieves the details of the current user.
     * 
     * @param userDetails the current user's details
     * @return a string with user details
     */
    public String getUser(@AuthenticationPrincipal UserDetails userDetails) {
        return "User Details: " + userDetails.getUsername();
    }

    /**
     * Displays the list of all ratings.
     * 
     * @param model the model to be passed to the view
     * @param request the HTTP request to get the remote user
     * @return the view displaying the list of ratings
     */
    @RequestMapping("/rating/list")
    public String home(Model model, @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
        log.info("Calling GET /rating/list");
        String remoteUser = request.getRemoteUser();
        model.addAttribute("ratings", ratingService.findAllRatings());
        model.addAttribute("remoteUser", remoteUser);
        return "rating/list";
    }

    /**
     * Displays the form to add a new rating.
     * 
     * @param rating the rating object
     * @param model the model to be passed to the view
     * @return the form for adding a new rating
     */
    @GetMapping("/rating/add")
    public String addRatingForm(Rating rating, Model model) {
        log.info("Calling GET /rating/add");
        model.addAttribute("rating", new Rating());
        return "rating/add";
    }

    /**
     * Validates and saves the rating submitted via the form.
     * 
     * @param rating the rating object to be saved
     * @param result the binding result of the form submission
     * @param model the model to be passed to the view
     * @return the view displaying the updated list of ratings
     */
    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model) {
        log.info("call to POST /rating/validate with {}", rating.toString());

        if (rating.getOrderNumber() < 0)
            result.rejectValue("orderNumber", "error.orderNumber", "orderNumber cannot be negative...");
        
        if (result.hasErrors()) {
            model.addAttribute("error", true); 
            model.addAttribute("message", result.getAllErrors());
            return "rating/add";  
        }
        
        ratingService.saveRating(rating);
        List<Rating> ratings = ratingService.findAllRatings();
        model.addAttribute("ratings", ratings);
        return "rating/list"; 
    }


    /**
     * Displays the form to update an existing rating.
     * 
     * @param id the ID of the rating to be updated
     * @param model the model to be passed to the view
     * @return the form to update the rating
     */
    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        log.info("call to GET /rating/update/{}", id);
        try {
            Rating rating = ratingService.findById(id);  
            model.addAttribute("rating", rating);
            return "rating/update";
        }
        catch (Exception ex) {
            log.warn("Exception : {}", ex);
            return null;
        }
    }

    /**
     * Updates an existing rating based on the form submission.
     * 
     * @param id the ID of the rating to be updated
     * @param rating the rating object with updated values
     * @param result the binding result of the form submission
     * @param model the model to be passed to the view
     * @return the updated list of ratings
     * @throws Exception if the form data is invalid
     */
    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                             BindingResult result, Model model) throws Exception {
    	
        if (rating.getOrderNumber() < 0)
            result.rejectValue("orderNumber", "error.orderNumber", "orderNumber cannot be negative...");
        
        if (result.hasErrors()) {
            model.addAttribute("error", true); 
            model.addAttribute("message", result.getAllErrors());
            return "rating/update";  
        }

        if (rating.getFitchRating() != null && rating.getMoodysRating() != null 
            && rating.getSandPRating() != null && rating.getOrderNumber() > 0) { 
            ratingService.updateRating(rating);
            List<Rating> ratings = ratingService.findAllRatings();
            model.addAttribute("ratings", ratings);
            return "redirect:/rating/list";
        } else { 
            throw new Exception("Error in form, can't update rating.");
        }
    }

    /**
     * Deletes a rating by its ID.
     * 
     * @param id the ID of the rating to be deleted
     * @param model the model to be passed to the view
     * @return the updated list of ratings
     */
    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        log.info("Calling GET /rating/delete/{}", id);
        try { 
            ratingService.deleteRatingById(id);
        }
        catch (Exception e) {
            log.warn(e.getMessage());
        }
        List<Rating> ratings = ratingService.findAllRatings();
        model.addAttribute("ratings", ratings);
        return "redirect:/rating/list";
    }

}
