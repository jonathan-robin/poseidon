package com.nnk.springboot.services;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setPassword("password");
    }

    @Test
    public void testFindAll() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        // Appel de la méthode de service
        var result = userService.findAll();

        // Vérification du résultat
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    public void testSaveUser() {
        // Simuler la sauvegarde d'un utilisateur
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Appel de la méthode de service
        userService.saveUser(user);

        // Vérification que le mot de passe est bien crypté
        assertNotNull(user.getPassword());
        assertNotEquals("password", user.getPassword());
    }

    @Test
    public void testSaveUserWithId() {
        // Simuler la mise à jour d'un utilisateur
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Appel de la méthode de service
        userService.saveUser(user, user.getId());

        // Vérification que l'id est correctement mis à jour
        assertEquals(Integer.valueOf(1), user.getId());
    }

    @Test
    public void testFindById() {
        // Simuler la recherche d'un utilisateur par ID
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Appel de la méthode de service
        User result = userService.findById(1);

        // Vérification du résultat
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByIdThrowsException() {
        // Simuler l'absence d'un utilisateur avec un ID spécifique
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Appel de la méthode de service, il doit lancer une exception
        userService.findById(1);
    }

    @Test
    public void testDeleteUserById() throws Exception {
        // Simuler la présence d'un utilisateur à supprimer
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Appel de la méthode de service
        userService.deleteUserById(1);

        // Vérifier que la méthode de suppression a été appelée
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test(expected = Exception.class)
    public void testDeleteUserByIdThrowsException() throws Exception {
        // Simuler l'absence d'un utilisateur à supprimer
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Appel de la méthode de service, il doit lancer une exception
        userService.deleteUserById(1);
    }
}

