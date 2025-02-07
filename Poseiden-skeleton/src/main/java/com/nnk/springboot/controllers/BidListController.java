package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
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

@Controller
@Slf4j
public class BidListController {

    @Autowired
    private BidListService bidListService;
    
    @Autowired
    private BidListRepository bidListRepository;
    
    public String getUser(@AuthenticationPrincipal UserDetails userDetails) {
        return "User Details: " + userDetails.getUsername();
    }

    @RequestMapping("/bidList/list")
    public String home(Model model, @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
    	String remoteUser = request.getRemoteUser();
        model.addAttribute("bidLists", bidListService.findAllBids());
        model.addAttribute("remoteUser", remoteUser);
        return "bidList/list";
    }
    
    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid, Model model) {
    	model.addAttribute("bid", new BidList());
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@jakarta.validation.Valid BidList bid, @AuthenticationPrincipal UserDetails userDetails, BindingResult result, Model model) {
    	log.info("call to POST /bidList/validate with {}", bid.toString());

    	  // Si des erreurs de validation sont présentes, renvoyer la vue avec les erreurs
        if (result.hasErrors()) {
            return "bidList/add";  // Ou toute autre vue qui montre les erreurs de validation
        }

        List<BidList> bids = bidListService.saveBid(bid);
        model.addAttribute("bidLists", bids);
        return "bidList/list";  // Rediriger vers la liste si tout va bien

    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // TODO: get Bid by Id and to model then show to the form
    	log.info("call to GET /bidList/update/{}", id);
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

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @jakarta.validation.Valid BidList bidList,
                             BindingResult result, Model model) throws Exception{
        // TODO: check required fields, if valid call service to update Bid and return list Bid
    	if (result.hasErrors()) 
    		return null; 
    	
    	if (bidList.getBidQuantity() > 0 && bidList.getType() != null && bidList.getAccount() != null) { 
    		bidListService.updateBidList(bidList);
    		List<BidList> bids = bidListRepository.findAll();
    		model.addAttribute("bidList", bids);
            return "redirect:/bidList/list";
    	} else { 
    		throw new Exception("Error in form, can't update Bid.");
    	}


    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        // TODO: Find Bid by Id and delete the bid, return to Bid list
        return "redirect:/bidList/list";
    }
}
