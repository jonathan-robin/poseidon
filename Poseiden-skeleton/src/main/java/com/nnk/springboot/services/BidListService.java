package com.nnk.springboot.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BidListService {

	@Autowired
	private BidListRepository bidListRepo;
	
	public List<BidList> findAllBids(){ 
		return this.bidListRepo.findAll();
	}
	
	
}
