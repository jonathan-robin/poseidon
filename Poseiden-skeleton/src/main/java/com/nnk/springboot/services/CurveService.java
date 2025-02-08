package com.nnk.springboot.services;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CurveService {

	@Autowired
	private CurvePointRepository curveRepository;
	
	public List<CurvePoint> findAllCurves(){ 
		return curveRepository.findAll();
	}
	
	public void saveCurve(CurvePoint curve){ 
    	
    	CurvePoint newCurve = new CurvePoint(); 
    	newCurve.setTerm(curve.getTerm()); 
    	newCurve.setValue(curve.getValue()); 
    	newCurve.setCurveId(curve.getCurveId());
    	
    	log.info("Saving new curve {}...", newCurve);
    	curveRepository.save(newCurve);
	}
	
	public CurvePoint findById(Integer id) throws Exception { 
	Optional<CurvePoint> curve = curveRepository.findById(id); 
		
		if (curve.isPresent())
			return curve.get(); 
		
		else 
			throw new Exception("can't retrieve curvePoint with id " + id);
	}
	
	
	public CurvePoint updateCurvePoint(CurvePoint curvePoint) throws Exception { 
		
		Optional<CurvePoint> optCurve= curveRepository.findById(curvePoint.getId());
		Map<String, String> tmpUpdates = new HashMap<>();
		if (optCurve.isPresent()) { 
			CurvePoint curve = optCurve.get();

		    for (Field field : CurvePoint.class.getDeclaredFields()) {
		        field.setAccessible(true);
		        try {
		            Object originalValue = field.get(curve);
		            Object updatedValue = field.get(curvePoint);

		            if (!Objects.equals(originalValue, updatedValue)) {
		            	tmpUpdates.put(field.toString(), updatedValue.toString());
		                field.set(curve, updatedValue);
		            }
		        } catch (IllegalAccessException e) {
		            e.printStackTrace();
		        }
		    }
		    log.info("Updating Curve id: {} with updates {}", curve.getId(), tmpUpdates);
		    curveRepository.save(curve);
		    return findById(curve.getId());
		}
		else 
			throw new Exception("Can't find current Bid");
		
		
	}
	
	public void deleteCurvePointById(Integer id) throws Exception { 
		Optional<CurvePoint> curveToDelete = curveRepository.findById(id); 
		if (curveToDelete.isPresent()) {
			log.info("Deleting curvePoint with ID: {}", id);
			curveRepository.deleteById(id);
		}
		else {
			throw new Exception("Can't find the curvePoint for id: " + id);
		}
		
	}
	
	
	
	
}
