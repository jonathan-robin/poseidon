package com.nnk.springboot.services;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TradeService {
    
    @Autowired
    private TradeRepository tradeRepository;
    
    /**
     * Retrieves all trades from the repository.
     * 
     * @return a list of all trades
     */
    public List<Trade> findAllTrades(){ 
        return tradeRepository.findAll();
    }
    
    /**
     * Saves a new trade in the repository.
     * 
     * @param trade the trade to save
     */
    public void saveTrade(Trade trade){    
        Trade newTrade = new Trade(); 
        newTrade.setAccount(trade.getAccount());
        newTrade.setBuyQuantity(trade.getBuyQuantity());
        newTrade.setType(trade.getType());

        log.info("Saving new trade {}...", trade);
        tradeRepository.save(trade);
    }
    
    /**
     * Finds a trade by its ID.
     * 
     * @param id the ID of the trade to find
     * @return the trade with the specified ID
     * @throws Exception if the trade is not found
     */
    public Trade findById(Integer id) throws Exception { 
        Optional<Trade> trade = tradeRepository.findById(id); 
        
        if (trade.isPresent())
            return trade.get(); 
        
        else 
            throw new Exception("can't retrieve trade with id " + id);
    }
    
    /**
     * Updates an existing trade by comparing fields and saving changes.
     * 
     * @param trade the updated trade
     * @return the updated trade
     * @throws Exception if the trade to update is not found
     */
    public Trade updateTrade(Trade trade) throws Exception { 
        
        Optional<Trade> opt = tradeRepository.findById(trade.getTradeId());
        Map<String, String> tmpUpdates = new HashMap<>();
        if (opt.isPresent()) { 
            Trade old = opt.get();

            for (Field field : Trade.class.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    Object originalValue = field.get(old);
                    Object updatedValue = field.get(trade);

                    if (!Objects.equals(originalValue, updatedValue)) {
                        tmpUpdates.put(field.toString(), updatedValue.toString());
                        field.set(old, updatedValue);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            log.info("Updating trade id: {} with updates {}", trade.getTradeId(), tmpUpdates);
            tradeRepository.save(old);
            return findById(old.getTradeId());
        }
        else 
            throw new Exception("Can't find current trade");
    }
    
    /**
     * Deletes a trade by its ID.
     * 
     * @param id the ID of the trade to delete
     * @throws Exception if the trade is not found
     */
    public void deleteTradeById(Integer id) throws Exception { 
        Optional<Trade> tradeToDelete = tradeRepository.findById(id); 
        if (tradeToDelete.isPresent()) {
            log.info("Deleting trade with ID: {}", id);
            tradeRepository.deleteById(id);
        }
        else {
            throw new Exception("Can't find the trade for id: " + id);
        }
    }
}
