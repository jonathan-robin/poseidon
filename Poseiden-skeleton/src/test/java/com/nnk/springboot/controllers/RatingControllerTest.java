package com.nnk.springboot.controllers;


import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingService ratingService;

    @Test
    @WithMockUser
    public void testListRatings() throws Exception {
        // Simuler la récupération de la liste des ratings
        Rating rating = new Rating("Fitch", "AAA", "A", 1);
        when(ratingService.findAllRatings()).thenReturn(Arrays.asList(rating));

        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isOk())  // Vérifie que la réponse HTTP est 200 OK
                .andExpect(view().name("rating/list"))  // Vérifie que la vue est "rating/list"
                .andExpect(model().attributeExists("ratings"))  // Vérifie que les ratings existent dans le modèle
                .andExpect(model().attribute("ratings", Arrays.asList(rating)));  // Vérifie que la liste contient l'objet "rating"
        
        verify(ratingService, times(1)).findAllRatings();
    }

    @Test
    @WithMockUser
    public void testAddRatingForm() throws Exception {
        // Tester le formulaire d'ajout d'un Rating
        mockMvc.perform(get("/rating/add"))
                .andExpect(status().isOk())  // Vérifie que la réponse HTTP est 200 OK
                .andExpect(view().name("rating/add"));  // Vérifie que la vue est "rating/add"
    }

    @Test
    @WithMockUser
    public void testValidateRating() throws Exception {
        Rating validRating = new Rating();
        validRating.setFitchRating("Fitch");
        validRating.setMoodysRating("AAA");
        validRating.setSandPRating("A");
        validRating.setOrderNumber(1);
        
        Model model = mock(Model.class);
        BindingResult bindingResult = mock(BindingResult.class);

        mockMvc.perform(post("/rating/validate", validRating, bindingResult, model)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("fitchRating", "Fitch")
                .param("moodysRating", "AAA")
                .param("sandPRating", "A")
                .param("orderNumber", "1")
                .with(csrf()))
        		.andExpect(status().isOk())
                .andExpect(view().name("rating/list"));
        
        verify(ratingService, times(1)).saveRating(any(Rating.class));
    }

    @Test
    @WithMockUser
    public void testUpdateRatingForm() throws Exception {
        // Tester la mise à jour d'un Rating (on suppose ici que le Rating avec l'ID 1 existe)
        Rating validRating = new Rating("Fitch", "AAA", "A", 1);
        when(ratingService.findById(1)).thenReturn(validRating);

        mockMvc.perform(get("/rating/update/{id}", 1))
                .andExpect(status().isOk())  // Vérifie que la réponse HTTP est 200 OK
                .andExpect(view().name("rating/update"))  // Vérifie que la vue est "rating/update"
                .andExpect(model().attribute("rating", validRating));  // Vérifie que l'objet "rating" est présent dans le modèle

        verify(ratingService, times(1)).findById(1);
    }

    @Test
    @WithMockUser
    public void testUpdateRating() throws Exception {
        // Simuler la mise à jour d'un Rating valide
        Rating ratingToUpdate = new Rating("Fitch", "AAA", "A", 1);
        ratingToUpdate.setFitchRating("Fitch");
        ratingToUpdate.setMoodysRating("AAA");
        ratingToUpdate.setSandPRating("A");
        ratingToUpdate.setOrderNumber(2);
        
        when(ratingService.findById(any(Integer.class))).thenReturn(ratingToUpdate);
        
        mockMvc.perform(post("/rating/update/{id}", 1)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("fitchRating", ratingToUpdate.getFitchRating())
                .param("moodysRating", ratingToUpdate.getMoodysRating())
                .param("sandPRating", ratingToUpdate.getSandPRating())
                .param("orderNumber", String.valueOf(ratingToUpdate.getOrderNumber()))
                .with(csrf())) 
                .andExpect(status().is3xxRedirection())  
                .andExpect(view().name("redirect:/rating/list"));

        verify(ratingService, times(1)).updateRating(any(Rating.class));
    }

    @Test
    @WithMockUser
    public void testDeleteRating() throws Exception {
        
        int ratingId = 1;

        mockMvc.perform(get("/rating/delete/{id}", ratingId))
                .andExpect(status().is3xxRedirection())  
                .andExpect(redirectedUrl("/rating/list")); 
        
        verify(ratingService, times(1)).deleteRatingById(ratingId);
    }

    @Test
    @WithMockUser
    public void testRatingValidationError() throws Exception {
        mockMvc.perform(post("/rating/validate")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("fitchRating", "") 
                .param("moodysRating", "AAA")
                .param("sandPRating", "A")
                .param("orderNumber", "-8")  // Valeur invalide
                .with(csrf())) 
                .andExpect(view().name("rating/add")); 
    }
}
