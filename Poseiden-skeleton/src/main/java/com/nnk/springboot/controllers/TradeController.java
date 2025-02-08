package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
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

/**
 * Controller for managing Trade related operations.
 */
@Controller
@Slf4j
public class TradeController {

    @Autowired
    private TradeService tradeService;

    /**
     * Retrieves the details of the authenticated user.
     * 
     * @param userDetails the authenticated user details
     * @return a string containing user details
     */
    public String getUser(@AuthenticationPrincipal UserDetails userDetails) {
        return "User Details: " + userDetails.getUsername();
    }

    /**
     * Displays the list of all trades.
     * 
     * @param model the model to add attributes
     * @param userDetails the authenticated user details
     * @param request the HTTP request
     * @return the view name for displaying the trade list
     */
    @RequestMapping("/trade/list")
    public String home(Model model, @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
    	log.info("call to GET /trade/list");
        String remoteUser = request.getRemoteUser();
        model.addAttribute("trades", tradeService.findAllTrades());
        model.addAttribute("remoteUser", remoteUser);
        return "trade/list";
    }

    /**
     * Displays the form to add a new trade.
     * 
     * @param trade the trade object
     * @param model the model to add attributes
     * @return the view name for the trade add form
     */
    @GetMapping("/trade/add")
    public String addRatingForm(Trade trade, Model model) {
    	log.info("call to GET /trade/add with {}", trade.toString());
        model.addAttribute("trade", new Trade());
        return "trade/add";
    }

    /**
     * Validates and saves a new trade.
     * 
     * @param trade the trade to be saved
     * @param result the result of binding the trade object
     * @param model the model to add attributes
     * @return the view name for displaying the updated trade list
     */
    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result, Model model) {
        log.info("call to POST /trade/validate with {}", trade.toString());

        if (trade.getBuyQuantity() < 0)
            result.rejectValue("buyQuantity", "error.buyQuantity", "buyQuantity cannot be negative...");
        
        if (result.hasErrors()) {
            model.addAttribute("error", true); 
            model.addAttribute("message", result.getAllErrors());
            return "trade/add";  
        }

        tradeService.saveTrade(trade);
        List<Trade> trades = tradeService.findAllTrades();

        model.addAttribute("trades", trades);
        return "trade/list";
    }

    /**
     * Displays the form to update an existing trade.
     * 
     * @param id the ID of the trade to update
     * @param model the model to add attributes
     * @return the view name for the trade update form
     */
    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        log.info("call to GET /trade/update/{}", id);
        try {
            Trade trade = tradeService.findById(id);
            model.addAttribute("trade", trade);
            return "trade/update";
        } catch (Exception ex) {
            log.warn("Exception : {}", ex);
            return null;
        }
    }

    /**
     * Validates and updates an existing trade.
     * 
     * @param id the ID of the trade to update
     * @param trade the trade object with updated data
     * @param result the result of binding the trade object
     * @param model the model to add attributes
     * @return the view name for redirecting to the trade list after update
     * @throws Exception if there is an error in the form submission
     */
    @PostMapping("/trade/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @jakarta.validation.Valid Trade trade,
                               BindingResult result, Model model) throws Exception {
        log.info("Calling GET /trade/update/{} with {}", id, trade.toString());
        
        if (trade.getBuyQuantity() < 0)
            result.rejectValue("buyQuantity", "error.buyQuantity", "buyQuantity cannot be negative...");
        
        if (result.hasErrors()) {
            model.addAttribute("error", true); 
            model.addAttribute("message", result.getAllErrors());
            return "trade/update";  
        }

        if (trade.getAccount() != null && trade.getType() != null && trade.getBuyQuantity() != null) {
            tradeService.updateTrade(trade);
            List<Trade> trades = tradeService.findAllTrades();
            model.addAttribute("trades", trades);
            return "redirect:/trade/list";
        } else {
            throw new Exception("Error in form, can't update trade.");
        }
    }

    /**
     * Deletes a trade by its ID.
     * 
     * @param id the ID of the trade to delete
     * @param model the model to add attributes
     * @return the view name for redirecting to the trade list after deletion
     */
    @GetMapping("/trade/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        log.info("Calling GET /trade/delete/{}", id);
        try {
            tradeService.deleteTradeById(id);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        List<Trade> trades = tradeService.findAllTrades();
        model.addAttribute("trades", trades);
        return "redirect:/trade/list";
    }
}
