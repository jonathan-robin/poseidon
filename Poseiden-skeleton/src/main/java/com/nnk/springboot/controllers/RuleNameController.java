package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;

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
 * Controller for managing rule names.
 */
@Controller
@Slf4j
public class RuleNameController {

    @Autowired
    private RuleNameService ruleNameService;

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
     * Displays the list of all rule names.
     * 
     * @param model the model to be passed to the view
     * @param request the HTTP request to get the remote user
     * @return the view displaying the list of rule names
     */
    @RequestMapping("/ruleName/list")
    public String home(Model model, @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
        log.info("Calling GET /ruleName/list");
        String remoteUser = request.getRemoteUser();
        model.addAttribute("ruleNames", ruleNameService.findAllRuleNames());
        model.addAttribute("remoteUser", remoteUser);
        return "ruleName/list";
    }

    /**
     * Displays the form to add a new rule name.
     * 
     * @param ruleName the rule name object
     * @param model the model to be passed to the view
     * @return the form for adding a new rule name
     */
    @GetMapping("/ruleName/add")
    public String addRatingForm(RuleName ruleName, Model model) {
        log.info("Calling GET /ruleName/add");
        model.addAttribute("ruleName", new RuleName());
        return "ruleName/add";
    }

    /**
     * Validates and saves the rule name submitted via the form.
     * 
     * @param ruleName the rule name object to be saved
     * @param result the binding result of the form submission
     * @param model the model to be passed to the view
     * @return the view displaying the updated list of rule names
     */
    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {
        log.info("call to POST /ruleName/validate with {}", ruleName.toString());

        if (result.hasErrors()) {
            model.addAttribute("error", true); 
            model.addAttribute("message", result.getAllErrors());
            return "ruleName/add";  
        }
        
        ruleNameService.saveRuleName(ruleName);
        List<RuleName> ruleNames = ruleNameService.findAllRuleNames();
        model.addAttribute("ruleNames", ruleNames);
        return "ruleName/list";
    }

    /**
     * Displays the form to update an existing rule name.
     * 
     * @param id the ID of the rule name to be updated
     * @param model the model to be passed to the view
     * @return the form to update the rule name
     */
    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        log.info("call to GET /ruleName/update/{}", id);
        try {
            RuleName ruleName = ruleNameService.findById(id);  
            model.addAttribute("ruleName", ruleName);
            return "ruleName/update";
        }
        catch (Exception ex) {
            log.warn("Exception : {}", ex);
            return null;
        }
    }

    /**
     * Updates an existing rule name based on the form submission.
     * 
     * @param id the ID of the rule name to be updated
     * @param ruleName the rule name object with updated values
     * @param result the binding result of the form submission
     * @param model the model to be passed to the view
     * @return the updated list of rule names
     * @throws Exception if the form data is invalid
     */
    @PostMapping("/ruleName/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                              BindingResult result, Model model) throws Exception {
        
    	if (result.hasErrors()) {
            model.addAttribute("error", true); 
            model.addAttribute("message", result.getAllErrors());
            return "ruleName/update";  
        }

        ruleNameService.updateRuleName(ruleName);
        List<RuleName> ruleNames = ruleNameService.findAllRuleNames();
        model.addAttribute("ruleNames", ruleNames);
        return "redirect:/ruleName/list";
        
    }

    /**
     * Deletes a rule name by its ID.
     * 
     * @param id the ID of the rule name to be deleted
     * @param model the model to be passed to the view
     * @return the updated list of rule names
     */
    @GetMapping("/ruleName/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        log.info("Calling GET /ruleName/delete/{}", id);
        try {    
            ruleNameService.deleteRuleNameById(id);
        }
        catch (Exception e) { 
            log.warn(e.getMessage());
        }
        List<RuleName> ruleNames = ruleNameService.findAllRuleNames();
        model.addAttribute("ruleNames", ruleNames);
        return "redirect:/ruleName/list";
    }
}
