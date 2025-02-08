package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    public void setUp() {
        // Si besoin, initialise des objets ici avant chaque test.
    }

    @Test
    @WithMockUser(username="test",password="test",roles={"ADMIN"})
    public void testLoginPage() throws Exception {
        // Teste la méthode pour afficher la page de connexion
        mockMvc.perform(get("/app/login"))
                .andExpect(status().isOk())  // S'attend à une réponse 200 OK
                .andExpect(view().name("home"));  // S'attend à la vue 'login'
    }

    @Test
    @WithMockUser(username="test",password="test",roles={"ADMIN"})
    public void testGetAllUserArticles() throws Exception {
        // Teste la méthode pour afficher la liste des utilisateurs
        when(userRepository.findAll()).thenReturn(List.of(new User(), new User()));  // Simule des utilisateurs

        mockMvc.perform(get("/app/secure/article-details"))
                .andExpect(status().isOk())  // S'attend à une réponse 200 OK
                .andExpect(view().name("user/list"))  // S'attend à la vue 'user/list'
                .andExpect(model().attributeExists("users"));  // Vérifie que l'attribut "users" est ajouté au modèle
    }

    @Test
    public void testErrorPage() throws Exception {
        // Teste la méthode pour afficher la page d'erreur
        mockMvc.perform(get("/app/error"))
                .andExpect(status().isOk())  // S'attend à une réponse 200 OK
                .andExpect(view().name("403"))  // S'attend à la vue '403'
                .andExpect(model().attributeExists("errorMsg"))  // Vérifie que l'attribut "errorMsg" est ajouté au modèle
                .andExpect(model().attribute("errorMsg", "You are not authorized for the requested data."));  // Vérifie le message d'erreur
    }
}

