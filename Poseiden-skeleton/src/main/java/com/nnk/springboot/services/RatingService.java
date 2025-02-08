package com.nnk.springboot.services;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.repositories.RatingRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RatingService {

	@Autowired
	private RatingRepository ratingRepository;
	
	public List<Rating> findAllRatings(){ 
		return ratingRepository.findAll();
	}
	
	public void saveRating(Rating rating){ 
    	
    	Rating newRating = new Rating(); 
    	newRating.setMoodysRating(rating.getMoodysRating()); 
    	newRating.setOrderNumber(rating.getOrderNumber()); 
    	newRating.setSandPRating(rating.getSandPRating());
    	newRating.setOrderNumber(rating.getOrderNumber());
    	newRating.setFitchRating(rating.getFitchRating());
    	
    	log.info("Saving new rating {}...", newRating);
    	ratingRepository.save(newRating);
	}
	
	public Rating findById(Integer id) throws Exception { 
		Optional<Rating> rating = ratingRepository.findById(id); 
		
		if (rating.isPresent())
			return rating.get(); 
		
		else 
			throw new Exception("can't retrieve rating with id " + id);
	}
	
	
	public Rating updateRating(Rating rating) throws Exception { 
		
		Optional<Rating> optRating = ratingRepository.findById(rating.getId());
		Map<String, String> tmpUpdates = new HashMap<>();
		if (optRating.isPresent()) { 
			Rating oldRating = optRating.get();

		    for (Field field : Rating.class.getDeclaredFields()) {
		        field.setAccessible(true);
		        try {
		            Object originalValue = field.get(oldRating);
		            Object updatedValue = field.get(rating);

		            if (!Objects.equals(originalValue, updatedValue)) {
		            	tmpUpdates.put(field.toString(), updatedValue.toString());
		                field.set(oldRating, updatedValue);
		            }
		        } catch (IllegalAccessException e) {
		            e.printStackTrace();
		        }
		    }
		    log.info("Updating rating id: {} with updates {}", rating.getId(), tmpUpdates);
		    ratingRepository.save(oldRating);
		    return findById(oldRating.getId());
		}
		else 
			throw new Exception("Can't find current Bid");
		
		
	}
	
	public void deleteRatingById(Integer id) throws Exception { 
		Optional<Rating> ratingToDelete = ratingRepository.findById(id); 
		if (ratingToDelete.isPresent()) {
			log.info("Deleting rating with ID: {}", id);
			ratingRepository.deleteById(id);
		}
		else {
			throw new Exception("Can't find the rating for id: " + id);
		}
		
	}
	
}
