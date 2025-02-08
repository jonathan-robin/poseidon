package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.services.BidListService;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(BidListController.class)
public class BidListControllerTest {
	
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BidListService bidListService;
    
    @MockBean
    private BidListRepository bidListRepository; 
    
    @InjectMocks
    private BidListController bidListController;

    @Test
    @WithMockUser
    void testHome() throws Exception {

        // Mock des données de bid list si nécessaire
        List<BidList> mockBidList = Arrays.asList(new BidList(), new BidList());
        when(bidListService.findAllBids()).thenReturn(mockBidList);

        // Simulation de la requête avec un utilisateur authentifié
        mockMvc.perform(get("/bidList/list")
        		.with(SecurityMockMvcRequestPostProcessors.user("diffblue")))
        		.andExpect(status().isOk())
                .andExpect(view().name("bidList/list"))
                .andExpect(model().attributeExists("bidLists", "remoteUser"));
  
    }

    @Test
    @WithMockUser
    void testAddBidForm() throws Exception {
        mockMvc.perform(get("/bidList/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"))
                .andExpect(model().attributeExists("bid"));
    }

    @Test
    @WithMockUser(username="test",password="test",roles={"ADMIN"})
    void testValidateBid() throws Exception {
        BidList bid = new BidList();
        bid.setBidQuantity(100D);
        bid.setType("type");
        bid.setAccount("account");

        
        List<BidList> bids = new ArrayList(); 
        bids.add(bid);

        // Stubbing pour matcher n'importe quel objet BidList
        when(bidListService.saveBid(any(BidList.class))).thenReturn(bids);

        mockMvc.perform(post("/bidList/validate")
        		.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("bidQuantity", "5")
                .param("type", "type")
                .param("account", "account"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/list"))
                .andExpect(model().attributeExists("bidLists"));
    }
    
    
}
