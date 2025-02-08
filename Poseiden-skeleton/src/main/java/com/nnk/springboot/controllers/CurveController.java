package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
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
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class CurveController {
    // TODO: Inject Curve Point service
	@Autowired
	private CurveService curveService;
	
	 public String getUser(@AuthenticationPrincipal UserDetails userDetails) {
	        return "User Details: " + userDetails.getUsername();
    }

    @RequestMapping("/curvePoint/list")
    public String home(Model model, @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
    	String remoteUser = request.getRemoteUser();
        model.addAttribute("curvePoints", curveService.findAllCurves());
        model.addAttribute("remoteUser", remoteUser);
        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addCurvePointForm(CurvePoint bid, Model model) {
    	model.addAttribute("curve", new CurvePoint());
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {
        // TODO: check data valid and save to db, after saving return Curve list
    	log.info("call to POST /curvePoint/validate with {}", curvePoint.toString());

  	  // Si des erreurs de validation sont présentes, renvoyer la vue avec les erreurs
      if (result.hasErrors()) {
    	  log.info("errors: {}", result.getAllErrors());
          return "curvePoint/add";  // Ou toute autre vue qui montre les erreurs de validation
      }
      
      curveService.saveCurve(curvePoint);
      List<CurvePoint> curves = curveService.findAllCurves();
      
      model.addAttribute("curvePoints", curves);
      return "curvePoint/list";  // Rediriger vers la liste si tout va bien
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // TODO: get CurvePoint by Id and to model then show to the form
    	log.info("call to GET /curvePoint/update/{}", id);
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

    @PostMapping("/curvePoint/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @jakarta.validation.Valid CurvePoint curvePoint,
                             BindingResult result, Model model) throws Exception{
        // TODO: check required fields, if valid call service to update Curve and return Curve list
    	if (result.hasErrors()) 
    		return null; 
    	
    	if (curvePoint.getValue() > 0 && curvePoint.getTerm() != null && curvePoint.getCurveId() != null) { 
    		curveService.updateCurvePoint(curvePoint);
    		List<CurvePoint> curvePoints = curveService.findAllCurves();
    		model.addAttribute("curvePoints", curvePoints);
            return "redirect:/curvePoint/list";
    	} else { 
    		throw new Exception("Error in form, can't update curve.");
    	}


    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurvePoint(@PathVariable("id") Integer id, Model model) {
        // TODO: Find Curve by Id and delete the Curve, return to Curve list
    	   // TODO: Find Bid by Id and delete the bid, return to Bid list
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
