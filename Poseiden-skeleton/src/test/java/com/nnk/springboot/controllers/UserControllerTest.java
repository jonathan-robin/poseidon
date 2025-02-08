package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private User user;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        user = new User();
        user.setId(1);
        user.setUsername("testUser");
        user.setPassword("password");
    }

    @Test
    public void testHome() throws Exception {
        // Given
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        // When + Then
        mockMvc.perform(get("/user/list"))
               .andExpect(status().isOk())
               .andExpect(view().name("user/list"));
    }

    @Test
    public void testAddUser() throws Exception {
        // When + Then
        mockMvc.perform(get("/user/add"))
               .andExpect(status().isOk())
               .andExpect(view().name("user/add"));
    }

    @Test
    public void testValidateUser() throws Exception {
        // Given
        when(userRepository.save(any(User.class))).thenReturn(user);
        

        // When + Then
        mockMvc.perform(post("/user/validate")
                        .param("username", "testUser")
                        .param("password", "abcdefgt125!*P")
                        .param("fullname", "testFullName")// Données valides
                        .param("Role", "testRole"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/user/list"));
    }

    @Test
    public void testValidateUserWithErrors() throws Exception {
        // Given
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When + Then
        mockMvc.perform(post("/user/validate")
                        .param("username", "")
                        .param("password", ""))
               .andExpect(status().isOk())
               .andExpect(view().name("user/add"));
    }

    @Test
    public void testShowUpdateForm() throws Exception {
        // Given
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userService.findById(any(Integer.class))).thenReturn(new User());

        // When + Then
        mockMvc.perform(get("/user/update/{id}", 1))
               .andExpect(status().isOk())
               .andExpect(view().name("user/update"));
    }

    @Test
    public void testUpdateUserWithValidData() throws Exception {
        // Given: Initialisation de l'utilisateur pour le test
        User user = new User();
        user.setId(15);
        user.setUsername("existingUser");
        user.setPassword("oldPassword");

        // Mock du repository
        when(userRepository.findById(15)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When + Then : Simulation de la soumission du formulaire
        mockMvc.perform(post("/user/update/{id}", 15)
                .param("username", "updatedUser")  // Données valides
                .param("password", "abcdefghi1!*P") 
                .param("fullname", "updatedFullName")// Données valides
                .param("Role", "updatedRole"))
                .andExpect(status().is3xxRedirection())  // Vérifie la redirection
                .andExpect(redirectedUrl("/user/list"));  // Vérifie la redirection vers la liste des utilisateurs
    }



    @Test
    public void testUpdateUserWithErrors() throws Exception {
        // Given
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // When + Then
        mockMvc.perform(post("/user/update/{id}", 1)
                        .param("username", "")
                        .param("password", ""))
               .andExpect(status().isOk())
               .andExpect(view().name("user/update"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        // Given
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // When + Then
        mockMvc.perform(get("/user/delete/{id}", 1))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/user/list"));
    }

    @Test
    public void testDeleteUserNotFound() throws Exception {
        // Given: Simulation d'un utilisateur inexistant
        doThrow(new Exception("Invalid user Id:1")).when(userService).deleteUserById(1);
        Model model = mock(Model.class);  // Mock du Model

        // When: Vérification que l'exception est bien levée
        Exception exception = assertThrows(Exception.class, () -> {
            userController.deleteUser(1, model);
        });

        // Then: Vérification du message d'erreur
        assertEquals("Invalid user Id:1", exception.getMessage());

        // Vérifier que le service a bien été appelé une fois avec l'ID 1
        verify(userService, times(1)).deleteUserById(1);
    }
}

