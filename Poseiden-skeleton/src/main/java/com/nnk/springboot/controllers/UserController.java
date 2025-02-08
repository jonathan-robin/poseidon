package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller pour gérer les utilisateurs dans l'application.
 */
@Controller
@Slf4j
public class UserController {
	
    @Autowired
    private UserService userService;

    /**
     * Affiche la liste des utilisateurs.
     * 
     * @param model Le modèle pour passer des données à la vue.
     * @return La vue avec la liste des utilisateurs.
     */
    @RequestMapping("/user/list")
    public String home(Model model) {
        log.info("Calling GET /user/list");
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }

    /**
     * Affiche le formulaire pour ajouter un utilisateur.
     * 
     * @param user L'objet utilisateur à ajouter.
     * @return La vue du formulaire d'ajout d'utilisateur.
     */
    @GetMapping("/user/add")
    public String addUser(User user) {
        log.info("Calling GET /user/add with user: {}", user);
        return "user/add";
    }

    /**
     * Valide et enregistre un nouvel utilisateur.
     * 
     * @param user   L'objet utilisateur à valider et enregistrer.
     * @param result Les résultats de la validation des données utilisateur.
     * @param model  Le modèle pour passer des données à la vue.
     * @return La vue de la liste des utilisateurs si la validation réussit, sinon retourne le formulaire d'ajout.
     */
    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result, Model model) {
        log.info("Calling POST /user/validate with user: {}", user);

        if (!result.hasErrors()) {
        	userService.saveUser(user);
            model.addAttribute("users", userService.findAll());
            return "redirect:/user/list";
        }
        return "user/add";
    }

    /**
     * Affiche le formulaire pour mettre à jour un utilisateur existant.
     * 
     * @param id    L'ID de l'utilisateur à mettre à jour.
     * @param model Le modèle pour passer des données à la vue.
     * @return La vue du formulaire de mise à jour d'utilisateur.
     */
    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        log.info("Calling GET /user/update/{} with id: {}", id);

    	User user = userService.findById(id);
        user.setPassword("");  // Effacer le mot de passe avant de l'afficher
        model.addAttribute("user", user);
        return "user/update";
    }

    /**
     * Met à jour les informations d'un utilisateur.
     * 
     * @param id     L'ID de l'utilisateur à mettre à jour.
     * @param user   L'objet utilisateur mis à jour.
     * @param result Les résultats de la validation des données utilisateur.
     * @param model  Le modèle pour passer des données à la vue.
     * @return La vue de la liste des utilisateurs après mise à jour.
     */
    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result, Model model) {
        log.info("Calling POST /user/update/{} with user: {}", id, user);

        if (result.hasErrors()) {
            return "user/update";
        }

        userService.saveUser(user, id);
        model.addAttribute("users", userService.findAll());
        return "redirect:/user/list";
    }

    /**
     * Supprime un utilisateur.
     * 
     * @param id    L'ID de l'utilisateur à supprimer.
     * @param model Le modèle pour passer des données à la vue.
     * @return La vue de la liste des utilisateurs après suppression.
     * @throws Exception Si une erreur se produit lors de la suppression.
     */
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) throws Exception {
        log.info("Calling GET /user/delete/{} with id: {}", id);

        userService.deleteUserById(id);
        model.addAttribute("users", userService.findAll());
        return "redirect:/user/list";
    }
}
