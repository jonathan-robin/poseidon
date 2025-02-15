package com.nnk.springboot.controllers;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;

import com.nnk.springboot.domain.CustomUserDetails;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.UserService;
import com.nnk.springboot.validation.UniqueUsernameValidator;

import jakarta.validation.ConstraintValidatorContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private UserService userService;
    
    @Mock
    private UserDetails userDetails;
    
    @Mock
    private UniqueUsernameValidator uniqueUsernameValidator;

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
        user.setPassword("password123!");
        // bypass validator
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
        when(userRepository.findByUsernameOptional(any(String.class))).thenReturn(Optional.empty()); // No user found
        when(uniqueUsernameValidator.isValid(any(String.class), any(ConstraintValidatorContext.class))).thenReturn(true);

        userController.validate(user, mock(BindingResult.class), mock(Model.class));
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
        when(userRepository.findByUsernameOptional("updatedUser")).thenReturn(Optional.empty()); // No user found

        userController.validate(user, mock(BindingResult.class), mock(Model.class));
    }



    @Test
    public void testUpdateUserWithErrors() throws Exception {
        // Given
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // When + Then
        mockMvc.perform(put("/user/update/{id}", 1)
                        .param("username", "")
                        .param("password", ""))
               .andExpect(status().isOk())
               .andExpect(view().name("user/update"));
    }

    @Test
    @WithMockUser
    public void testDeleteUser() throws Exception {
        // Given
    	CustomUserDetails details = new CustomUserDetails("differentUser", "password", null);
    	when(userDetails.getUsername()).thenReturn("differentUser");
        when(userService.findById(1)).thenReturn(user);
        userController.deleteUser(1, mock(Model.class), details);

        
        verify(userService, times(1)).deleteUserById(1);

    }

    @Test
    public void testDeleteUserNotFound() throws Exception {
        // Given: Simulation d'un utilisateur inexistant
        doThrow(new Exception("Invalid user Id:1")).when(userService).deleteUserById(1);
        Model model = mock(Model.class);  // Mock du Model

        // When: Vérification que l'exception est bien levée
        Exception exception = assertThrows(Exception.class, () -> {
            userController.deleteUser(1, model, mock(UserDetails.class));
        });
    }
}

