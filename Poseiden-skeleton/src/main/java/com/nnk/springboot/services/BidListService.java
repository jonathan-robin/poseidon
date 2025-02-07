package com.nnk.springboot.services;

import java.util.List;
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
	
	public List<BidList> findAllBids(){ 
		return this.bidListRepository.findAll();
	}
	
	public List<BidList> saveBid(BidList bid){ 
    	
    	BidList newBid = new BidList(); 
    	newBid.setAccount(bid.getAccount()); 
    	newBid.setType(bid.getType()); 
    	newBid.setBidQuantity(bid.getBidQuantity());
    	
    	log.info("Saving new Bid {}...", newBid);
    	bidListRepository.save(newBid);
    	
    	return this.findAllBids();
	}
	
	public BidList findById(Integer id) throws Exception { 
		Optional<BidList> bidList = bidListRepository.findById(id); 
		
		if (bidList.isPresent())
			return bidList.get(); 
		
		else 
			throw new Exception("can't retrieve Bid with id " + id);

	}
	
	
}
