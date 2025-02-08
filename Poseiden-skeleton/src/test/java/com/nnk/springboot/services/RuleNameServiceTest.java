package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RuleNameServiceTest {

    @Mock
    private RuleNameRepository ruleNameRepository;

    @InjectMocks
    private RuleNameService ruleNameService;

    @Test
    public void testFindAllRuleNames() {
        RuleName ruleName = new RuleName();
        ruleName.setName("TestRule");
        when(ruleNameRepository.findAll()).thenReturn(Arrays.asList(ruleName));

        List<RuleName> ruleNames = ruleNameService.findAllRuleNames();

        assertNotNull(ruleNames);
        assertEquals(1, ruleNames.size());
        verify(ruleNameRepository, times(1)).findAll();
    }

    @Test
    public void testSaveRuleName() {
        RuleName ruleName = new RuleName();
        ruleName.setName("TestRule");
        ruleName.setDescription("Description");

        ruleNameService.saveRuleName(ruleName);

        verify(ruleNameRepository, times(1)).save(any(RuleName.class));
    }

    @Test
    public void testFindById() throws Exception {
        RuleName ruleName = new RuleName();
        ruleName.setId(1);
        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(ruleName));

        RuleName foundRuleName = ruleNameService.findById(1);

        assertNotNull(foundRuleName);
        assertEquals(1, foundRuleName.getId());
        verify(ruleNameRepository, times(1)).findById(1);
    }

    @Test
    public void testFindByIdThrowsException() {
        when(ruleNameRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> {
            ruleNameService.findById(1);
        });

        verify(ruleNameRepository, times(1)).findById(1);
    }

    @Test
    public void testUpdateRuleName() throws Exception {
        RuleName oldRuleName = new RuleName();
        oldRuleName.setId(1);
        oldRuleName.setName("OldRule");
        oldRuleName.setDescription("OldDescription");

        RuleName newRuleName = new RuleName();
        newRuleName.setId(1);
        newRuleName.setName("UpdatedRule");
        newRuleName.setDescription("UpdatedDescription");

        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(oldRuleName));

        RuleName updatedRuleName = ruleNameService.updateRuleName(newRuleName);

        assertNotNull(updatedRuleName);
        assertEquals("UpdatedRule", updatedRuleName.getName());
        verify(ruleNameRepository, times(1)).save(oldRuleName);
    }

    @Test
    public void testUpdateRuleNameThrowsException() {
        RuleName ruleName = new RuleName();
        ruleName.setId(1);

        when(ruleNameRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> {
            ruleNameService.updateRuleName(ruleName);
        });

        verify(ruleNameRepository, times(1)).findById(1);
    }

    @Test
    public void testDeleteRuleNameById() throws Exception {
        RuleName ruleName = new RuleName();
        ruleName.setId(1);

        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(ruleName));

        ruleNameService.deleteRuleNameById(1);

        verify(ruleNameRepository, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteRuleNameByIdThrowsException() {
        when(ruleNameRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> {
            ruleNameService.deleteRuleNameById(1);
        });

        verify(ruleNameRepository, times(1)).findById(1);
    }
}

