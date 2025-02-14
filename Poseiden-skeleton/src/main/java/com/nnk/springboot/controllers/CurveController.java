package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurveService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for managing CurvePoint operations.
 */
@Controller
@Slf4j
public class CurveController {
    
    @Autowired
    private CurveService curveService;

    /**
     * Gets the username of the currently authenticated user.
     * 
     * @param userDetails the user details of the authenticated user
     * @return the username of the authenticated user
     */
    public String getUser(@AuthenticationPrincipal UserDetails userDetails) {
        return "User Details: " + userDetails.getUsername();
    }

    /**
     * Displays the list of all CurvePoints.
     * 
     * @param model the model to add attributes to the view
     * @param userDetails the details of the currently authenticated user
     * @param request the HTTP request
     * @return the view name for the curve point list
     */
    @RequestMapping("/curvePoint/list")
    public String home(Model model, @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
        String remoteUser = request.getRemoteUser();
        log.info("Calling GET /curvePoint/list, remoteUser: {}", remoteUser);
        model.addAttribute("curvePoints", curveService.findAllCurves());
        model.addAttribute("remoteUser", remoteUser);
        return "curvePoint/list";
    }

    /**
     * Displays the form to add a new CurvePoint.
     * 
     * @param bid the bid object
     * @param model the model to add attributes to the view
     * @return the view name for the add curve point form
     */
    @GetMapping("/curvePoint/add")
    public String addCurvePointForm(CurvePoint curvePoint, Model model) {
        log.info("Calling GET /curvePoint/add");
        model.addAttribute("curve", new CurvePoint());
        return "curvePoint/add";
    }

    /**
     * Validates and saves a new CurvePoint.
     * 
     * @param curvePoint the CurvePoint to save
     * @param result the binding result for validation
     * @param model the model to add attributes to the view
     * @return the view name for the list of curve points
     */
    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {
        log.info("Calling POST /curvePoint/validate with {}", curvePoint.toString());

        if (curvePoint.getValue() < 0)
            result.rejectValue("value", "error.value", "Value cannot be negative...");
        
        if (result.hasErrors()) {
            model.addAttribute("error", true); 
            model.addAttribute("message", result.getAllErrors());
            return "curvePoint/add";  
        }
        

        curveService.saveCurve(curvePoint);
        List<CurvePoint> curves = curveService.findAllCurves();
        model.addAttribute("curvePoints", curves);
        return "curvePoint/list";
    }

    /**
     * Displays the form to update an existing CurvePoint.
     * 
     * @param id the ID of the CurvePoint to update
     * @param model the model to add attributes to the view
     * @return the view name for the update curve point form
     */
    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        log.info("Calling GET /curvePoint/update/{}", id);
        try {
            CurvePoint curve = curveService.findById(id);  
            model.addAttribute("curvePoint", curve);
            return "curvePoint/update";
        }
        catch (Exception ex) { 
            log.warn("Exception : {}", ex);
            return null;
        }
    }

    /**
     * Updates an existing CurvePoint.
     * 
     * @param id the ID of the CurvePoint to update
     * @param curvePoint the updated CurvePoint
     * @param result the binding result for validation
     * @param model the model to add attributes to the view
     * @return the view name for the list of curve points
     * @throws Exception if the update fails
     */
    @PostMapping("/curvePoint/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @jakarta.validation.Valid CurvePoint curvePoint,
                             BindingResult result, Model model) throws Exception {
        log.info("Calling POST /curvePoint/update/{} with {}", id, curvePoint);
        
        if (curvePoint.getValue() < 0)
            result.rejectValue("value", "error.value", "Value cannot be negative...");
        
        if (result.hasErrors()) {
            model.addAttribute("error", true); 
            model.addAttribute("message", result.getAllErrors());
            return "curvePoint/update";  
        }
        
        if (curvePoint.getValue() > 0 && curvePoint.getTerm() != null && curvePoint.getCurveId() != null) { 
            curveService.updateCurvePoint(curvePoint);
            List<CurvePoint> curvePoints = curveService.findAllCurves();
            model.addAttribute("curvePoints", curvePoints);
            return "redirect:/curvePoint/list";
        } else { 
            throw new Exception("Error in form, can't update curve.");
        }
    }

    /**
     * Deletes a CurvePoint by its ID.
     * 
     * @param id the ID of the CurvePoint to delete
     * @param model the model to add attributes to the view
     * @return the view name for the list of curve points
     */
    @Transactional
    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurvePoint(@PathVariable("id") Integer id, Model model) {
        log.info("Calling GET /curvePoint/delete/{}", id);
        try {    
            curveService.deleteCurvePointById(id);
        }
        catch (Exception e) { 
            log.warn(e.getMessage());
        }
        List<CurvePoint> curves = curveService.findAllCurves();
        model.addAttribute("curvePoints", curves);
        return "redirect:/curvePoint/list";
    }
}
