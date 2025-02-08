package com.nnk.springboot.controllers;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Empty;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RatingService;
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

@Controller
@Slf4j
public class RuleNameController {

 	@Autowired
 	private RuleNameService ruleNameService;
 	
 	 public String getUser(@AuthenticationPrincipal UserDetails userDetails) {
 	        return "User Details: " + userDetails.getUsername();
     }

     @RequestMapping("/ruleName/list")
     public String home(Model model, @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
     	String remoteUser = request.getRemoteUser();
         model.addAttribute("ruleNames", ruleNameService.findAllRuleNames());
         model.addAttribute("remoteUser", remoteUser);
         return "ruleName/list";
     }
     
     @GetMapping("/ruleName/add")
     public String addRatingForm(RuleName ruleName, Model model) {
     	model.addAttribute("ruleName", new RuleName());
         return "ruleName/add";
     }

     @PostMapping("/ruleName/validate")
     public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {
         // TODO: check data valid and save to db, after saving return Curve list
     	log.info("call to POST /ruleName/validate with {}", ruleName.toString());

   	  // Si des erreurs de validation sont présentes, renvoyer la vue avec les erreurs
       if (result.hasErrors() ) {
     	  log.info("errors: {}", result.getAllErrors());
     	  return "ruleName/add"; // Ou toute autre vue qui montre les erreurs de validation
       }
       else if (ruleName.getName() == null || ruleName.getName().isEmpty()) {
      	  log.info("name can't be null");
      	  return "ruleName/add"; // Ou toute autre vue qui montre les erreurs de validation
       }
       
       ruleNameService.saveRuleName(ruleName);
       List<RuleName> ruleNames = ruleNameService.findAllRuleNames();
       
       model.addAttribute("ruleNames", ruleNames);
       return "ruleName/list";  // Rediriger vers la liste si tout va bien
     }
     
     @GetMapping("/ruleName/update/{id}")
     public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
         // TODO: get CurvePoint by Id and to model then show to the form
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
     
     @PostMapping("/ruleName/update/{id}")
     public String updateRating(@PathVariable("id") Integer id, @jakarta.validation.Valid RuleName ruleName,
                              BindingResult result, Model model) throws Exception{
         // TODO: check required fields, if valid call service to update Curve and return Curve list
     	if (result.hasErrors()) 
     		return null; 
     	
     	if (ruleName.getDescription() != null && ruleName.getJson() != null  && ruleName.getName() != null && ruleName.getSqlPart() != null
     			&& ruleName.getSqlStr() != null && ruleName.getTemplate() != null) { 
     		ruleNameService.updateRuleName(ruleName);
     		List<RuleName> ruleNames = ruleNameService.findAllRuleNames();
     		model.addAttribute("ruleNames", ruleNames);
             return "redirect:/ruleName/list";
     	} else { 
     		throw new Exception("Error in form, can't update ruleName.");
     	}
     }
     
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
