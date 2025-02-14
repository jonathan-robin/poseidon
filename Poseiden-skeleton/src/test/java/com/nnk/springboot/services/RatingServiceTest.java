package com.nnk.springboot.services;


import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RatingServiceTest {

    // Utiliser @MockBean pour injecter un mock de RatingRepository
    @MockBean
    private RatingRepository ratingRepository;

    // Le service sera automatiquement injecté par Spring
    @Autowired
    private RatingService ratingService;

    @Test
    public void testFindAllRatings() {
        // Given
        Rating rating1 = new Rating();
        rating1.setId(1);
        rating1.setMoodysRating("AAA");
        rating1.setSandPRating("AAA");
        rating1.setFitchRating("AAA");
        rating1.setOrderNumber(1);

        Rating rating2 = new Rating();
        rating2.setId(2);
        rating2.setMoodysRating("AA");
        rating2.setSandPRating("AA");
        rating2.setFitchRating("AA");
        rating2.setOrderNumber(2);

        // Quand ratingRepository.findAll() est appelé, on retourne une liste de ratings simulées
        when(ratingRepository.findAll()).thenReturn(List.of(rating1, rating2));

        // When
        List<Rating> ratings = ratingService.findAllRatings();

        // Then
        assertNotNull(ratings);
        assertEquals(2, ratings.size());
        assertEquals(1, ratings.get(0).getId());
        assertEquals(2, ratings.get(1).getId());
    }

    @Test
    public void testSaveRating() {
        // Given
        Rating newRating = new Rating();
        newRating.setMoodysRating("AAA");
        newRating.setSandPRating("AAA");
        newRating.setFitchRating("AAA");
        newRating.setOrderNumber(1);

        // When
        ratingService.saveRating(newRating);

        // Then
        verify(ratingRepository, times(1)).save(any(Rating.class)); // Vérifier si la méthode save a été appelée une fois
    }

    @Test
    public void testFindById_whenRatingExists() throws Exception {
        // Given
        Integer id = 1;
        Rating rating = new Rating();
        rating.setId(1);
        rating.setMoodysRating("AAA");
        rating.setSandPRating("AAA");
        rating.setFitchRating("AAA");
        rating.setOrderNumber(1);

        // Simuler la réponse du repository
        when(ratingRepository.findById(id)).thenReturn(Optional.of(rating));

        // When
        Rating foundRating = ratingService.findById(id);

        // Then
        assertNotNull(foundRating);
        assertEquals(id, foundRating.getId());
    }

    @Test
    public void testFindById_whenRatingDoesNotExist() {
        // Given
        Integer id = 999;
        when(ratingRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            ratingService.findById(id);
        });

        assertEquals("can't retrieve rating with id 999", exception.getMessage());
    }

    @Test
    public void testUpdateRating() throws Exception {
        // Given
        Rating existingRating = new Rating();
        existingRating.setId(1);
        existingRating.setMoodysRating("AAA");
        existingRating.setSandPRating("AAA");
        existingRating.setFitchRating("AAA");
        existingRating.setOrderNumber(1);

        Rating updatedRating = new Rating();
        updatedRating.setId(1);
        updatedRating.setMoodysRating("AA");  // Moody's rating mis à jour
        updatedRating.setSandPRating("AA");  // S&P rating mis à jour
        updatedRating.setFitchRating("AA");  // Fitch rating mis à jour
        updatedRating.setOrderNumber(1);

        when(ratingRepository.findById(1)).thenReturn(Optional.of(existingRating));
        when(ratingRepository.save(updatedRating)).thenReturn(updatedRating);

        // When
        Rating result = ratingService.updateRating(updatedRating);

        // Then
        assertNotNull(result);
        assertEquals("AA", result.getMoodysRating());
        assertEquals("AA", result.getSandPRating());
        assertEquals("AA", result.getFitchRating());
        verify(ratingRepository, times(1)).save(updatedRating);  // Vérifier si save a été appelé une fois
    }

    @Test
    public void testUpdateRating_whenRatingDoesNotExist() {
        // Given
        Rating updatedRating = new Rating();
        updatedRating.setId(1);
        updatedRating.setMoodysRating("AA");
        updatedRating.setSandPRating("AA");
        updatedRating.setFitchRating("AA");
        updatedRating.setOrderNumber(1);

        when(ratingRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            ratingService.updateRating(updatedRating);
        });

        assertEquals("Can't find current rating", exception.getMessage());
    }

    @Test
    public void testDeleteRatingById() throws Exception {
        // Given
        Integer id = 1;
        Rating rating = new Rating();
        rating.setId(id);
        when(ratingRepository.findById(id)).thenReturn(Optional.of(rating));

        // When
        ratingService.deleteRatingById(id);

        // Then
        verify(ratingRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteRatingById_whenRatingDoesNotExist() {
        // Given
        Integer id = 999;
        when(ratingRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            ratingService.deleteRatingById(id);
        });

        assertEquals("Can't find the rating for id: 999", exception.getMessage());
    }
}
