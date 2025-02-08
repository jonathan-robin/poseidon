package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.RuleNameService;
import com.nnk.springboot.services.TradeService;

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
public class TradeController {
	
	@Autowired
 	private TradeService tradeService;
 	
 	 public String getUser(@AuthenticationPrincipal UserDetails userDetails) {
 	        return "User Details: " + userDetails.getUsername();
     }
 	 
	@RequestMapping("/trade/list")
	public String home(Model model, @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
	  String remoteUser = request.getRemoteUser();
      model.addAttribute("trades", tradeService.findAllTrades());
      model.addAttribute("remoteUser", remoteUser);
      return "trade/list";
	}
	
	@GetMapping("/trade/add")
    public String addRatingForm(Trade trade, Model model) {
    	model.addAttribute("trade", new Trade());
        return "trade/add";
    }
	
    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result, Model model) {
        // TODO: check data valid and save to db, after saving return Curve list
   	 log.info("call to POST /ruleName/validate with {}", trade.toString());

  	  // Si des erreurs de validation sont présentes, renvoyer la vue avec les erreurs
      if (result.hasErrors() ) {
    	  log.info("errors: {}", result.getAllErrors());
    	  return "trade/add"; // Ou toute autre vue qui montre les erreurs de validation
      }
      else if (trade.getAccount() == null || trade.getAccount().isEmpty()) {
     	  log.info("Account can't be null");
     	  return "trade/add"; // Ou toute autre vue qui montre les erreurs de validation
      }
      
      tradeService.saveTrade(trade);
      List<Trade> trades = tradeService.findAllTrades();
      
      model.addAttribute("trades", trades);
      return "trade/list";  // Rediriger vers la liste si tout va bien
    }
      
    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // TODO: get CurvePoint by Id and to model then show to the form
    	log.info("call to GET /trade/update/{}", id);
    	try { 
    		Trade trade = tradeService.findById(id);  
    		model.addAttribute("trade", trade);
    		return "trade/update";
    	}
    	catch (Exception ex) { 
    		log.warn("Exception : {}", ex);
    		return null;
    	}
    }


    @PostMapping("/trade/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @jakarta.validation.Valid Trade trade,
                             BindingResult result, Model model) throws Exception{
        // TODO: check required fields, if valid call service to update Curve and return Curve list
    	if (result.hasErrors()) 
    		return null; 
    	
    	if (trade.getAccount() != null && trade.getType() != null  && trade.getBuyQuantity() != null) { 
    		tradeService.updateTrade(trade);
    		List<Trade> trades = tradeService.findAllTrades();
    		model.addAttribute("trades", trades);
            return "redirect:/trade/list";
    	} else { 
    		throw new Exception("Error in form, can't update trade.");
    	}
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
    	log.info("Calling GET /trade/delete/{}", id);
    	try {	
	    	tradeService.deleteTradeById(id);
    	}
    	catch (Exception e) { 
    		log.warn(e.getMessage());
    	}
    	List<Trade> trades = tradeService.findAllTrades();
    	model.addAttribute("trades", trades);
        return "redirect:/trade/list";
    }
    
}
