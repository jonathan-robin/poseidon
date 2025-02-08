package com.nnk.springboot.controllers;

import com.nnk.springboot.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller for handling login and error pages.
 */
@Controller
@RequestMapping("app")
@Slf4j
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Displays the login page.
     * 
     * @return ModelAndView object with the view set to "home"
     */
    @GetMapping("login")
    public ModelAndView login() {
        log.info("Calling GET /app/login");
        ModelAndView mav = new ModelAndView();
        mav.setViewName("home");
        return mav;
    }

    /**
     * Displays a list of all users' articles.
     * 
     * @return ModelAndView object with the list of users and the view set to "user/list"
     */
    @GetMapping("secure/article-details")
    public ModelAndView getAllUserArticles() {
        log.info("Calling GET /app/secure/article-details");
        ModelAndView mav = new ModelAndView();
        mav.addObject("users", userRepository.findAll());
        mav.setViewName("user/list");
        return mav;
    }

    /**
     * Displays the error page when access is denied.
     * 
     * @param request the HTTP request to get the remote user
     * @return ModelAndView object with the error message and the view set to "403"
     */
    @GetMapping("error")
    public ModelAndView error(HttpServletRequest request) {
        log.info("Calling GET /app/error");
        String remoteUser = request.getRemoteUser();
        ModelAndView mav = new ModelAndView();

        mav.addObject("remoteUser", "Guest");
        if (remoteUser != null) {
            mav.addObject("remoteUser", remoteUser);
        }

        String errorMessage = "You are not authorized for the requested data.";
        mav.addObject("errorMsg", errorMessage);

        mav.setViewName("403");
        return mav;
    }
}
