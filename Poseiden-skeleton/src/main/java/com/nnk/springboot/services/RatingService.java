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
    
    /**
     * Retrieves all ratings from the repository.
     * 
     * @return a list of all ratings
     */
    public List<Rating> findAllRatings(){ 
        return ratingRepository.findAll();
    }
    
    /**
     * Saves a new rating in the repository.
     * 
     * @param rating the rating to save
     */
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
    
    /**
     * Finds a rating by its ID.
     * 
     * @param id the ID of the rating to find
     * @return the rating with the specified ID
     * @throws Exception if the rating is not found
     */
    public Rating findById(Integer id) throws Exception { 
        Optional<Rating> rating = ratingRepository.findById(id); 
        
        if (rating.isPresent())
            return rating.get(); 
        
        else 
            throw new Exception("can't retrieve rating with id " + id);
    }
    
    /**
     * Updates an existing rating by comparing fields and saving changes.
     * 
     * @param rating the updated rating
     * @return the updated rating
     * @throws Exception if the rating to update is not found
     */
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
    
    /**
     * Deletes a rating by its ID.
     * 
     * @param id the ID of the rating to delete
     * @throws Exception if the rating is not found
     */
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
