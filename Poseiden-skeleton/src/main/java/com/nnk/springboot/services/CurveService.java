package com.nnk.springboot.services;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CurveService {

    @Autowired
    private CurvePointRepository curveRepository;

    /**
     * Retrieves all CurvePoint records from the database.
     *
     * @return a list of all CurvePoints
     */
    public List<CurvePoint> findAllCurves() {
        return curveRepository.findAll();
    }

    /**
     * Saves a new CurvePoint to the database.
     *
     * @param curve the CurvePoint to be saved
     */
    public void saveCurve(CurvePoint curve) {

        CurvePoint newCurve = new CurvePoint();
        newCurve.setTerm(curve.getTerm());
        newCurve.setValue(curve.getValue());
        newCurve.setCurveId(curve.getCurveId());

        log.info("Saving new curve {}...", newCurve);
        curveRepository.save(newCurve);
    }

    /**
     * Finds a CurvePoint by its ID.
     *
     * @param id the ID of the CurvePoint to retrieve
     * @return the CurvePoint if found
     * @throws Exception if no CurvePoint is found with the given ID
     */
    public CurvePoint findById(Integer id) throws Exception {
        Optional<CurvePoint> curve = curveRepository.findById(id);

        if (curve.isPresent()) {
            return curve.get();
        } else {
            throw new Exception("Can't retrieve curvePoint with id " + id);
        }
    }

    /**
     * Updates an existing CurvePoint with new values.
     *
     * @param curvePoint the CurvePoint containing updated values
     * @return the updated CurvePoint
     * @throws Exception if the CurvePoint to update is not found
     */
    public CurvePoint updateCurvePoint(CurvePoint curvePoint) throws Exception {

        Optional<CurvePoint> optCurve = curveRepository.findById(curvePoint.getId());
        
		if (optCurve.isEmpty())
			throw new Exception("Can't find current CurvePoint");

		return curveRepository.save(curvePoint);	
    }

    /**
     * Deletes a CurvePoint by its ID.
     *
     * @param id the ID of the CurvePoint to delete
     * @throws Exception if the CurvePoint is not found
     */
    public void deleteCurvePointById(Integer id) throws Exception {
        Optional<CurvePoint> curveToDelete = curveRepository.findById(id);
        if (curveToDelete.isPresent()) {
            log.info("Deleting curvePoint with ID: {}", id);
            curveRepository.deleteById(id);
        } else {
            throw new Exception("Can't find the curvePoint for id: " + id);
        }
    }
}
