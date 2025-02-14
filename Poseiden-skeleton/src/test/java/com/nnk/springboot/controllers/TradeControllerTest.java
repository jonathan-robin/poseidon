package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeService tradeService;

    @Test
    @WithMockUser
    public void testListTrades() throws Exception {
        Trade trade = new Trade();
        trade.setAccount("account test");
        trade.setBuyQuantity(1D);
        trade.setType("type test");
        when(tradeService.findAllTrades()).thenReturn(Arrays.asList(trade));

        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(model().attributeExists("trades"))
                .andExpect(model().attribute("trades", Arrays.asList(trade)));

        verify(tradeService, times(1)).findAllTrades();
    }

    @Test
    @WithMockUser
    public void testAddTradeForm() throws Exception {
        mockMvc.perform(get("/trade/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"));
    }

    @Test
    @WithMockUser
    public void testValidateTrade() throws Exception {
        mockMvc.perform(post("/trade/validate")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("account", "Account Test")
                .param("type", "Type Test")
                .param("buyQuantity", "10.0")
                .with(csrf()))
                .andExpect(view().name("trade/list"));

        verify(tradeService, times(1)).saveTrade(any(Trade.class));
    }

    @Test
    @WithMockUser
    public void testUpdateTradeForm() throws Exception {
        Trade trade = new Trade();
        trade.setTradeId(1);
        when(tradeService.findById(1)).thenReturn(trade);

        mockMvc.perform(get("/trade/update/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attribute("trade", trade));

        verify(tradeService, times(1)).findById(1);
    }

    @Test
    @WithMockUser
    public void testUpdateTrade() throws Exception {
        Trade trade = new Trade();
        trade.setTradeId(1);

        when(tradeService.findById(1)).thenReturn(trade);

        mockMvc.perform(put("/trade/update/{id}", 1)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("account", "Updated Account")
                .param("type", "Updated Type")
                .param("buyQuantity", "15.0")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/trade/list"));

        verify(tradeService, times(1)).updateTrade(any(Trade.class));
    }

    @Test
    @WithMockUser
    public void testDeleteTrade() throws Exception {
        mockMvc.perform(delete("/trade/delete/{id}", 1).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService, times(1)).deleteTradeById(1);
    }

    @Test
    @WithMockUser
    public void testTradeValidationError() throws Exception {
    	
    	BindingResult result = mock(BindingResult.class);
    	result.setNestedPath("Account");
    	
        mockMvc.perform(post("/trade/validate")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("type", "Type Test")
                .param("buyQuantity", "-10.0")
                .with(csrf()))
                .andExpect(view().name("trade/add"));
    }
}

