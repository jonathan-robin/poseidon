package com.nnk.springboot.services;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BidListService {

	@Autowired
	private BidListRepository bidListRepository;
	
	/**
	 * <p>This is meant to send back all the bidList saved in DB. . . </p>
	 * @param no Parameters
	 * @return List of BidList
	 * @since 1.0
	 */
	public List<BidList> findAllBids(){ 
		return this.bidListRepository.findAll();
	}
	
	/**
	 * <p>Method to save BidList in DB</p>
	 * <p>Set-up all the necessary fields</p>
	 * @param The BidlList we want to save in DB
	 * @return List<BidList> all the bidList we have in DB
	 * @since 1.0
	 */
	public BidList saveBid(BidList bid){ 
    	
    	BidList newBid = new BidList(); 
    	newBid.setAccount(bid.getAccount()); 
    	newBid.setType(bid.getType()); 
    	newBid.setBidQuantity(bid.getBidQuantity());
    	
    	log.info("Saving new Bid {}...", newBid);
    	return bidListRepository.save(newBid);
    	
    	
	}
	
	/**
	 * <p>Method to find a bidList with a specific ID</p>
	 * <p>It searches for the bidList in case we don't find throw exception </p>
	 * @param The BidList ID (Integer)
	 * @return The BidList
	 * @throw IllegalArgumentException (in case we don't find by id)
	 * @since 1.0
	 */
	public BidList findById(Integer id) throws Exception { 
		Optional<BidList> bidList = bidListRepository.findById(id); 
		
		if (bidList.isPresent())
			return bidList.get(); 
		
		else 
			throw new Exception("can't retrieve Bid with id " + id);

	}
	
	/**
	 * <p>Method to update a bidList</p>
	 * <p>It searches for the bidList in DB in case we don't find it throw exception </p>
	 * <p>save new BidList </p>
	 * @param The BidList to save (BidList)
	 * @return The saved BidList
	 * @throw New Exception (in case we don't find by id)
	 * @since 1.0
	 */
	public BidList updateBidList(BidList bidList) throws Exception { 
		Optional<BidList> optBid = bidListRepository.findById(bidList.getId());
		
		if (optBid.isEmpty())
			throw new Exception("Can't find current Bid");

		return bidListRepository.save(bidList);	
		
	}
	
	/**
	 * <p>Method to delete a BidList with a specific ID</p>
	 * <p>It searches for the BidList in case we don't find throw exception </p>
	 * @param The BidList ID (Integer)
	 * @return void
	 * @throw New Exception (in case we don't find by id)
	 * @since 1.0
	 */
	public void deleteBidById(Integer id) throws Exception { 
		Optional<BidList> bidToDelete = bidListRepository.findById(id); 
		if (bidToDelete.isPresent()) {
			log.info("Deleting Bid with ID: {}", id);
			bidListRepository.deleteById(id);
		}
		else {
			throw new Exception("Can't find the BidList for id: " + id);
		}
		
	}
	
	
}
