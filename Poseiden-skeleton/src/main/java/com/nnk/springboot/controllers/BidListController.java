package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * Controller for managing BidList operations.
 */
@Controller
@Slf4j
public class BidListController {

    @Autowired
    private BidListService bidListService;
    
    /**
     * Gets the username of the currently authenticated user.
     * 
     * @param userDetails the user details of the authenticated user
     * @return the username of the authenticated user
     */
    public String getUser(@AuthenticationPrincipal UserDetails userDetails) {
        return "User Details: " + userDetails.getUsername();
    }

    /**
     * Displays the list of all BidLists.
     * 
     * @param model the model to add attributes to the view
     * @param userDetails the details of the currently authenticated user
     * @param request the HTTP request
     * @return the view name for the bid list
     */
    @RequestMapping("/bidList/list")
    public String home(Model model, @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
        String remoteUser = request.getRemoteUser();
        log.info("Calling GET /bidList/list, remoteUser: {}", remoteUser);
        model.addAttribute("bidLists", bidListService.findAllBids());
        model.addAttribute("remoteUser", remoteUser);
        return "bidList/list";
    }
    
    /**
     * Displays the form to add a new BidList.
     * 
     * @param bid the bid object
     * @param model the model to add attributes to the view
     * @return the view name for the add bid form
     */
    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid, Model model) {
        log.info("Calling GET /bidList/add");
        model.addAttribute("bid", new BidList());
        return "bidList/add";
    }

    /**
     * Validates and saves a new BidList.
     * 
     * @param bid the BidList to save
     * @param userDetails the details of the currently authenticated user
     * @param result the binding result for validation
     * @param model the model to add attributes to the view
     * @return the view name for the list of bid lists
     */
    @PostMapping("/bidList/validate")
    public String validate(@jakarta.validation.Valid BidList bid, @AuthenticationPrincipal UserDetails userDetails, BindingResult result, Model model) {
        log.info("Calling POST /bidList/validate with {}", bid.toString());

        if (result.hasErrors()) {
            return "bidList/add";  
        }

        List<BidList> bids = bidListService.saveBid(bid);
        model.addAttribute("bidLists", bids);
        return "bidList/list";  
    }

    /**
     * Displays the form to update an existing BidList.
     * 
     * @param id the ID of the BidList to update
     * @param model the model to add attributes to the view
     * @return the view name for the update bid form
     */
    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        log.info("Calling GET /bidList/update/{}", id);
        try { 
            BidList bid = bidListService.findById(id);  
            model.addAttribute("bidList", bid);
            return "bidList/update";
        }
        catch (Exception ex) { 
            log.warn("Exception : {}", ex);
            return null;
        }
    }

    /**
     * Updates an existing BidList.
     * 
     * @param id the ID of the BidList to update
     * @param bidList the updated BidList
     * @param result the binding result for validation
     * @param model the model to add attributes to the view
     * @return the view name for the list of bid lists
     * @throws Exception if the update fails
     */
    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @jakarta.validation.Valid BidList bidList,
                             BindingResult result, Model model) throws Exception {
        log.info("Calling POST /bidList/update/{} with {}", id, bidList);

        if (result.hasErrors()) 
            return null; 
        
        if (bidList.getBidQuantity() > 0 && bidList.getType() != null && bidList.getAccount() != null) { 
            bidListService.updateBidList(bidList);
            List<BidList> bids = bidListService.findAllBids();
            model.addAttribute("bidList", bids);
            return "redirect:/bidList/list";
        } else { 
            throw new Exception("Error in form, can't update Bid.");
        }
    }

    /**
     * Deletes a BidList by its ID.
     * 
     * @param id the ID of the BidList to delete
     * @param model the model to add attributes to the view
     * @return the view name for the list of bid lists
     */
    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        log.info("Calling GET /bidList/delete/{}", id);
        try {    
            bidListService.deleteBidById(id);
        }
        catch (Exception e) { 
            log.warn(e.getMessage());
        }
        List<BidList> bids = bidListService.findAllBids();
        model.addAttribute("bidList", bids);
        return "redirect:/bidList/list";
    }
}
