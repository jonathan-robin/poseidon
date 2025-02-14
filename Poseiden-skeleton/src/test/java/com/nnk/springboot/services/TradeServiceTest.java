package com.nnk.springboot.services;


import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TradeServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    @InjectMocks
    private TradeService tradeService;

    @Test
    public void testFindAllTrades() {
        Trade trade = new Trade();
        when(tradeRepository.findAll()).thenReturn(Arrays.asList(trade));

        List<Trade> trades = tradeService.findAllTrades();

        assertNotNull(trades);
        assertEquals(1, trades.size());
        verify(tradeRepository, times(1)).findAll();
    }

    @Test
    public void testSaveTrade() {
        Trade trade = new Trade();
        trade.setAccount("account");
        trade.setType("type");
        trade.setBuyQuantity(1D);

        tradeService.saveTrade(trade);

        verify(tradeRepository, times(1)).save(any(Trade.class));
    }

    @Test
    public void testFindById() throws Exception {
        Trade trade = new Trade();
        trade.setAccount("account");
        trade.setType("type");
        trade.setBuyQuantity(1D);
        trade.setTradeId(1);
        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade));

        Trade foundTrade = tradeService.findById(1);

        assertNotNull(foundTrade);
        assertEquals(1, foundTrade.getTradeId());
        verify(tradeRepository, times(1)).findById(1);
    }

    @Test
    public void testFindByIdThrowsException() {
        when(tradeRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> {
            tradeService.findById(1);
        });

        verify(tradeRepository, times(1)).findById(1);
    }

    @Test
    public void testUpdateTrade() throws Exception {
        Trade oldTrade = new Trade();
        oldTrade.setAccount("Old Account");
        oldTrade.setType("Old type");
        oldTrade.setBuyQuantity(5D);
        oldTrade.setTradeId(1);

        Trade newTrade = new Trade();
        newTrade.setTradeId(1);
        newTrade.setAccount("Updated Account");
        newTrade.setType("Updated Type");
        newTrade.setBuyQuantity(15D);


        when(tradeRepository.findById(1)).thenReturn(Optional.of(oldTrade));
        when(tradeRepository.save(newTrade)).thenReturn(newTrade);

        Trade updatedTrade = tradeService.updateTrade(newTrade);

        assertNotNull(updatedTrade);
        assertEquals("Updated Account", updatedTrade.getAccount());
        assertEquals("Updated Type", updatedTrade.getType());
        assertEquals(15.0, updatedTrade.getBuyQuantity());

    }

    @Test
    public void testUpdateTradeThrowsException() {
        Trade trade = new Trade();
        trade.setTradeId(1);
        trade.setAccount("Account Test");
        trade.setType("Type Test");
        trade.setBuyQuantity(10D);

        when(tradeRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> {
            tradeService.updateTrade(trade);
        });

        verify(tradeRepository, times(1)).findById(1);
    }

    @Test
    public void testDeleteTradeById() throws Exception {
        Trade trade = new Trade();
        trade.setTradeId(1);
        trade.setAccount("Account Test");
        trade.setType("Type Test");
        trade.setBuyQuantity(10D);

        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade));

        tradeService.deleteTradeById(1);

        verify(tradeRepository, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteTradeByIdThrowsException() {
        when(tradeRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> {
            tradeService.deleteTradeById(1);
        });

        verify(tradeRepository, times(1)).findById(1);
    }
}

