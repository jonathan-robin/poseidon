package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CurveServiceTest {

    @MockBean
    private CurvePointRepository curveRepository;

    @Autowired
    private CurveService curveService;

    @Test
    public void testFindAllCurves() {
        // Given
        CurvePoint curve1 = new CurvePoint();
        curve1.setCurveId(1);
        curve1.setTerm(1.0);
        curve1.setValue(100.5);

        CurvePoint curve2 = new CurvePoint();
        curve2.setCurveId(2);
        curve2.setTerm(2.0);
        curve2.setValue(200.5);

        when(curveRepository.findAll()).thenReturn(List.of(curve1, curve2));

        List<CurvePoint> curves = curveService.findAllCurves();

        assertNotNull(curves);
        assertEquals(2, curves.size());
        assertEquals(1, curves.get(0).getCurveId());
        assertEquals(2, curves.get(1).getCurveId());
    }

    @Test
    public void testSaveCurve() {
        // Given
        CurvePoint newCurve = new CurvePoint();
        newCurve.setCurveId(1);
        newCurve.setTerm(1.0);
        newCurve.setValue(100.5);

        // When
        curveService.saveCurve(newCurve);

        // Then
        verify(curveRepository, times(1)).save(any(CurvePoint.class)); // Vérifier si la méthode save a été appelée une fois
    }

    @Test
    public void testFindById_whenCurveExists() throws Exception {
        // Given
        Integer id = 1;
        CurvePoint curve = new CurvePoint();
        curve.setCurveId(1);
        curve.setTerm(1.0);
        curve.setValue(100.5);

        // Simuler la réponse du repository
        when(curveRepository.findById(id)).thenReturn(Optional.of(curve));

        // When
        CurvePoint foundCurve = curveService.findById(id);

        // Then
        assertNotNull(foundCurve);
        assertEquals(id, foundCurve.getCurveId());
    }

    @Test
    public void testFindById_whenCurveDoesNotExist() {
        // Given
        Integer id = 999;
        when(curveRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            curveService.findById(id);
        });

        assertEquals("Can't retrieve curvePoint with id 999", exception.getMessage());
    }

    @Test
    public void testUpdateCurvePoint() throws Exception {
        // Given
        CurvePoint existingCurve = new CurvePoint();
        existingCurve.setId(1);
        existingCurve.setCurveId(1);
        existingCurve.setTerm(1.0);
        existingCurve.setValue(100.5);

        CurvePoint updatedCurve = new CurvePoint();
        updatedCurve.setId(1);
        updatedCurve.setCurveId(1);
        updatedCurve.setTerm(2.0);  // Term mis à jour
        updatedCurve.setValue(150.5); // Value mise à jour

        when(curveRepository.findById(1)).thenReturn(Optional.of(existingCurve));
        when(curveRepository.save(existingCurve)).thenReturn(existingCurve);

        // When
        CurvePoint result = curveService.updateCurvePoint(updatedCurve);

        // Then
        assertNotNull(result);
        assertEquals(2.0, result.getTerm());
        assertEquals(150.5, result.getValue());
        verify(curveRepository, times(1)).save(existingCurve);  // Vérifier si save a été appelé une fois
    }

    @Test
    public void testUpdateCurvePoint_whenCurveDoesNotExist() {
        // Given
        CurvePoint updatedCurve = new CurvePoint();
        updatedCurve.setId(1);
        updatedCurve.setCurveId(1);
        updatedCurve.setTerm(2.0);
        updatedCurve.setValue(150.5);

        when(curveRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            curveService.updateCurvePoint(updatedCurve);
        });

        assertEquals("Can't find current CurvePoint", exception.getMessage());
    }

    @Test
    public void testDeleteCurvePointById() throws Exception {
        // Given
        Integer id = 1;
        CurvePoint curve = new CurvePoint();
        curve.setId(id);
        when(curveRepository.findById(id)).thenReturn(Optional.of(curve));

        // When
        curveService.deleteCurvePointById(id);

        // Then
        verify(curveRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteCurvePointById_whenCurveDoesNotExist() {
        // Given
        Integer id = 999;
        when(curveRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            curveService.deleteCurvePointById(id);
        });

        assertEquals("Can't find the curvePoint for id: 999", exception.getMessage());
    }
}
