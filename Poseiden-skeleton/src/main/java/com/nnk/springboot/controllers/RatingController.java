package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.CurveService;
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

@Controller
@Slf4j
public class RatingController {
    // TODO: Inject Curve Point service
	@Autowired
	private RatingService ratingService;
	
	 public String getUser(@AuthenticationPrincipal UserDetails userDetails) {
	        return "User Details: " + userDetails.getUsername();
    }

    @RequestMapping("/rating/list")
    public String home(Model model, @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
    	String remoteUser = request.getRemoteUser();
        model.addAttribute("ratings", ratingService.findAllRatings());
        model.addAttribute("remoteUser", remoteUser);
        return "rating/list";
    }


    @GetMapping("/rating/add")
    public String addRatingForm(Rating rating, Model model) {
    	model.addAttribute("rating", new Rating());
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model) {
        // TODO: check data valid and save to db, after saving return Curve list
    	log.info("call to POST /rating/validate with {}", rating.toString());

  	  // Si des erreurs de validation sont présentes, renvoyer la vue avec les erreurs
      if (result.hasErrors()) {
    	  log.info("errors: {}", result.getAllErrors());
    	  return "rating/add"; // Ou toute autre vue qui montre les erreurs de validation
      }
      
      ratingService.saveRating(rating);
      List<Rating> ratings = ratingService.findAllRatings();
      
      model.addAttribute("ratings", ratings);
      return "rating/list";  // Rediriger vers la liste si tout va bien
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // TODO: get CurvePoint by Id and to model then show to the form
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

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @jakarta.validation.Valid Rating rating,
                             BindingResult result, Model model) throws Exception{
        // TODO: check required fields, if valid call service to update Curve and return Curve list
    	if (result.hasErrors()) 
    		return null; 
    	
    	if (rating.getFitchRating() != null && rating.getMoodysRating() != null  && rating.getSandPRating() != null && rating.getOrderNumber() > 0) { 
    		ratingService.updateRating(rating);
    		List<Rating> ratings = ratingService.findAllRatings();
    		model.addAttribute("ratings", ratings);
            return "redirect:/rating/list";
    	} else { 
    		throw new Exception("Error in form, can't update rating.");
    	}
    }

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
