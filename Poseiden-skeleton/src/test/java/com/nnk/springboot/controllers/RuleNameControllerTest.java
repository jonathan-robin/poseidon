package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;
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
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class RuleNameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RuleNameService ruleNameService;

    @Test
    @WithMockUser
    public void testListRuleNames() throws Exception {
        // Simuler la récupération de la liste des RuleNames
        RuleName ruleName = new RuleName("Rule 1", "Description 1", "SQL 1", "JSON 1", "Template 1", "SQLPart 1");
        when(ruleNameService.findAllRuleNames()).thenReturn(Arrays.asList(ruleName));

        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().isOk())  // Vérifie que la réponse HTTP est 200 OK
                .andExpect(view().name("ruleName/list"))  // Vérifie que la vue est "ruleName/list"
                .andExpect(model().attributeExists("ruleNames"))  // Vérifie que les ruleNames existent dans le modèle
                .andExpect(model().attribute("ruleNames", Arrays.asList(ruleName)));  // Vérifie que la liste contient l'objet "ruleName"
        
        verify(ruleNameService, times(1)).findAllRuleNames();
    }

    @Test
    @WithMockUser
    public void testAddRuleNameForm() throws Exception {
        // Tester le formulaire d'ajout d'un RuleName
        mockMvc.perform(get("/ruleName/add"))
                .andExpect(status().isOk())  // Vérifie que la réponse HTTP est 200 OK
                .andExpect(view().name("ruleName/add"));  // Vérifie que la vue est "ruleName/add"
    }

    @Test
    @WithMockUser
    public void testValidateRuleName() throws Exception {
        // Simuler l'envoi d'un formulaire valide pour ajouter un RuleName
        RuleName validRuleName = new RuleName("Rule 1", "Description 1", "SQL 1", "JSON 1", "Template 1", "SQLPart 1");

        mockMvc.perform(post("/ruleName/validate")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", validRuleName.getName())
                .param("description", validRuleName.getDescription())
                .param("sqlStr", validRuleName.getSqlStr())
                .param("json", validRuleName.getJson())
                .param("template", validRuleName.getTemplate())
                .param("sqlPart", validRuleName.getSqlPart())
                .with(csrf()))  // Ajouter csrf pour tester la sécurité
                .andExpect(view().name("ruleName/list"));  // Vérifie que la redirection est vers "ruleName/list"
        
        verify(ruleNameService, times(1)).saveRuleName(any(RuleName.class));
    }

    @Test
    @WithMockUser
    public void testUpdateRuleNameForm() throws Exception {
        // Tester la mise à jour d'un RuleName (on suppose ici que le RuleName avec l'ID 1 existe)
        RuleName validRuleName = new RuleName("Rule 1", "Description 1", "SQL 1", "JSON 1", "Template 1", "SQLPart 1");
        validRuleName.setId(1);
        when(ruleNameService.findById(1)).thenReturn(validRuleName);

        mockMvc.perform(put("/ruleName/update/{id}", 1))
                .andExpect(status().isOk())  // Vérifie que la réponse HTTP est 200 OK
                .andExpect(view().name("ruleName/update"))  // Vérifie que la vue est "ruleName/update"
                .andExpect(model().attribute("ruleName", validRuleName));  // Vérifie que l'objet "ruleName" est présent dans le modèle

        verify(ruleNameService, times(1)).findById(1);
    }

    @Test
    @WithMockUser
    public void testUpdateRuleName() throws Exception {
        // Simuler la mise à jour d'un RuleName valide
        RuleName ruleNameToUpdate = new RuleName("Rule 1", "Description 1", "SQL 1", "JSON 1", "Template 1", "SQLPart 1");
        ruleNameToUpdate.setId(1);

        when(ruleNameService.findById(any(Integer.class))).thenReturn(ruleNameToUpdate);

        mockMvc.perform(put("/ruleName/update/{id}", 1)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", ruleNameToUpdate.getName())
                .param("description", ruleNameToUpdate.getDescription())
                .param("sqlStr", ruleNameToUpdate.getSqlStr())
                .param("json", ruleNameToUpdate.getJson())
                .param("template", ruleNameToUpdate.getTemplate())
                .param("sqlPart", ruleNameToUpdate.getSqlPart())
                .with(csrf()))  // Ajouter csrf pour tester la sécurité
                .andExpect(status().is3xxRedirection())  // Vérifie que la redirection est vers "ruleName/list"
                .andExpect(view().name("redirect:/ruleName/list"));

        verify(ruleNameService, times(1)).updateRuleName(any(RuleName.class));
    }

    @Test
    @WithMockUser
    public void testDeleteRuleName() throws Exception {
        // Supposons qu'il existe un RuleName avec l'ID 1
        int ruleNameId = 1;

        mockMvc.perform(delete("/ruleName/delete/{id}", ruleNameId))
                .andExpect(status().is3xxRedirection())  // Vérifie la redirection après suppression
                .andExpect(redirectedUrl("/ruleName/list"));  // Vérifie la redirection vers la liste après suppression

        verify(ruleNameService, times(1)).deleteRuleNameById(ruleNameId);
    }

    @Test
    @WithMockUser
    public void testRuleNameValidationError() throws Exception {
        // Tester un formulaire avec des erreurs de validation
        mockMvc.perform(post("/ruleName/validate")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("description", "Description 1")
                .param("sqlStr", "SQL 1")
                .param("json", "JSON 1")
                .param("template", "Template 1")
                .param("sqlPart", "SQLPart 1")
                .with(csrf()))  // Ajouter csrf pour tester la sécurité
                .andExpect(view().name("ruleName/add"));  // Vérifie que la vue affichée est "ruleName/add" en cas d'erreur de validation
    }
}
