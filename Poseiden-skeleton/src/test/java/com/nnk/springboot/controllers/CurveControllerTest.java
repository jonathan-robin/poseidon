package com.nnk.springboot.controllers;


import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurveService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class CurveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurveService curveService;

    @Test
    @WithMockUser
    public void testListCurvePoints() throws Exception {
        // Simuler la récupération de la liste des courbes
        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isOk())  // Vérifie que la réponse HTTP est 200 OK
                .andExpect(view().name("curvePoint/list"));  // Vérifie que la vue est "curvePoint/list"
    }

    @Test
    @WithMockUser
    public void testAddCurvePointForm() throws Exception {
        // Tester le formulaire d'ajout d'un CurvePoint
        mockMvc.perform(get("/curvePoint/add"))
                .andExpect(status().isOk())  // Vérifie que la réponse HTTP est 200 OK
                .andExpect(view().name("curvePoint/add"));  // Vérifie que la vue est "curvePoint/add"
    }

    @Test
    @WithMockUser
    public void testValidateCurvePoint() throws Exception {
        // Simuler l'envoi d'un formulaire valide pour ajouter un CurvePoint
        CurvePoint validCurvePoint = new CurvePoint();
        validCurvePoint.setCurveId(1);
        validCurvePoint.setTerm(1D);
        validCurvePoint.setValue(100.5);

        mockMvc.perform(post("/curvePoint/validate")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("term", "1")
                .with(csrf())
                .param("curveId", "5")
                .param("value", "100.5"))
                .andExpect(view().name("curvePoint/list")); 
    }

    @Test
    @WithMockUser
    public void testUpdateCurvePointForm() throws Exception {
        // Tester la mise à jour d'un CurvePoint (on suppose ici que le CurvePoint avec l'ID 1 existe)
        CurvePoint validCurvePoint = new CurvePoint();
        validCurvePoint.setCurveId(1);
        validCurvePoint.setId(1);
        validCurvePoint.setTerm(1D);
        validCurvePoint.setValue(100.5);
        
        when(curveService.findById(1)).thenReturn(validCurvePoint);

        mockMvc.perform(get("/curvePoint/update/{id}", 1))
                .andExpect(status().isOk())  // Vérifie que la réponse HTTP est 200 OK
                .andExpect(view().name("curvePoint/update"));  // Vérifie que la vue est "curvePoint/update"
    }

    @Test
    @WithMockUser
    public void testUpdateCurvePoint() throws Exception {
        // Simuler la mise à jour d'un CurvePoint valide
        CurvePoint curvePointToUpdate = new CurvePoint();
        curvePointToUpdate.setCurveId(1);  // Exemple d'ID pour mettre à jour
        curvePointToUpdate.setTerm(5D);
        curvePointToUpdate.setId(1);
        curvePointToUpdate.setValue(150.75);
        
        when(curveService.findById(1)).thenReturn(curvePointToUpdate);

        mockMvc.perform(post("/curvePoint/update/{id}", curvePointToUpdate.getId())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("curveId", String.valueOf(curvePointToUpdate.getCurveId()))
                .param("term", String.valueOf(curvePointToUpdate.getTerm()))
                .param("value", String.valueOf(curvePointToUpdate.getValue()))
                .with(csrf()))
                .andExpect(redirectedUrl("/curvePoint/list"));  // Vérifie que la redirection est vers "/curvePoint/list"
    }

    @Test
    @WithMockUser
    public void testDeleteCurvePoint() throws Exception {
        // Supposons qu'il existe un CurvePoint avec l'ID 1
        int curvePointId = 1;

        mockMvc.perform(get("/curvePoint/delete/{id}", curvePointId))
                .andExpect(status().is3xxRedirection())  // Vérifie la redirection (code 302)
                .andExpect(redirectedUrl("/curvePoint/list"));  // Vérifie la redirection vers la liste après suppression
    }

    @Test
    @WithMockUser
    public void testCurvePointValidationError() throws Exception {
        // Tester un formulaire avec des erreurs de validation
        mockMvc.perform(post("/curvePoint/validate")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("term", "p")
                .param("value", "")
                .with(csrf()))
                .andExpect(view().name("curvePoint/add"));  // Vérifie que la vue affichée est "curvePoint/add"
    }
}
