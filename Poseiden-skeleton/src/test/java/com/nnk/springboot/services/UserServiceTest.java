package com.nnk.springboot.services;

import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private User user;

    @Test
    @WithMockUser
    public void testFindAll() {
    	   User user = new User();
           user.setId(1);
           user.setUsername("testuser");
           user.setPassword("password");  // Mot de passe initial pour test
        when(userRepository.findAll()).thenReturn(List.of(user));

        var result = userService.findAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    public void testSaveUser() {
        // Simuler la sauvegarde du user avec un mot de passe crypté
        // Assurez-vous que l'objet 'user' est correctement initialisé ici
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setPassword("password");  // Mot de passe initial pour test
    	
        when(userRepository.save(any(User.class))).thenReturn(user);
        userService.saveUser(user);

        // Vérifiez que le mot de passe a bien été crypté
        assertNotNull(user.getPassword());
        assertNotEquals("password", user.getPassword());
    }

    @Test
    public void testSaveUserWithId() {
	   User user = new User();
       user.setId(1);
       user.setUsername("testuser");
       user.setPassword("password");  // Mot de passe initial pour test
	    when(userRepository.save(any(User.class))).thenReturn(user);
	    userService.saveUser(user, user.getId());
	    assertEquals(Integer.valueOf(1), user.getId());
    }

    @Test
    public void testFindById() {
    	   User user = new User();
           user.setId(1);
           user.setUsername("testuser");
           user.setPassword("password");  // Mot de passe initial pour test
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User result = userService.findById(1);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    public void testFindByIdThrowsException() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Tester l'exception levée
        try {
            userService.findById(1);
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid user Id:1", e.getMessage());
        }
    }

    @Test
    public void testDeleteUserById() throws Exception {
    	   User user = new User();
           user.setId(1);
           user.setUsername("testuser");
           user.setPassword("password");  // Mot de passe initial pour test
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        userService.deleteUserById(1);

        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteUserByIdThrowsException() throws Exception {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Tester l'exception levée lors de la suppression
        try {
            userService.deleteUserById(1);
            fail("Exception should have been thrown");
        } catch (Exception e) {
            assertEquals("Can't find the user for id: 1", e.getMessage());
        }
    }
}
