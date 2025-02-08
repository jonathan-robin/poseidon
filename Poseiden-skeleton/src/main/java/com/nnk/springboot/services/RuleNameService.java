package com.nnk.springboot.services;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RuleNameService {
    
    @Autowired
    private RuleNameRepository ruleNameRepository;
    
    /**
     * Retrieves all rule names from the repository.
     * 
     * @return a list of all rule names
     */
    public List<RuleName> findAllRuleNames(){ 
        return ruleNameRepository.findAll();
    }
    
    /**
     * Saves a new rule name in the repository.
     * 
     * @param ruleName the rule name to save
     */
    public void saveRuleName(RuleName ruleName){ 
        
        RuleName newRuleName = new RuleName(); 
        newRuleName.setDescription(ruleName.getDescription());
        newRuleName.setJson(ruleName.getJson());
        newRuleName.setName(ruleName.getName());
        newRuleName.setSqlPart(ruleName.getSqlPart());
        newRuleName.setSqlStr(ruleName.getSqlStr());
        newRuleName.setTemplate(ruleName.getTemplate());
        
        log.info("Saving new ruleName {}...", newRuleName);
        ruleNameRepository.save(newRuleName);
    }
    
    /**
     * Finds a rule name by its ID.
     * 
     * @param id the ID of the rule name to find
     * @return the rule name with the specified ID
     * @throws Exception if the rule name is not found
     */
    public RuleName findById(Integer id) throws Exception { 
        Optional<RuleName> ruleName = ruleNameRepository.findById(id); 
        
        if (ruleName.isPresent())
            return ruleName.get(); 
        
        else 
            throw new Exception("can't retrieve ruleName with id " + id);
    }
    
    /**
     * Updates an existing rule name by comparing fields and saving changes.
     * 
     * @param ruleName the updated rule name
     * @return the updated rule name
     * @throws Exception if the rule name to update is not found
     */
    public RuleName updateRuleName(RuleName ruleName) throws Exception { 
        
        Optional<RuleName> opt = ruleNameRepository.findById(ruleName.getId());
        Map<String, String> tmpUpdates = new HashMap<>();
        if (opt.isPresent()) { 
            RuleName old = opt.get();

            for (Field field : RuleName.class.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    Object originalValue = field.get(old);
                    Object updatedValue = field.get(ruleName);

                    if (!Objects.equals(originalValue, updatedValue)) {
                        tmpUpdates.put(field.toString(), updatedValue.toString());
                        field.set(old, updatedValue);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            log.info("Updating ruleName id: {} with updates {}", ruleName.getId(), tmpUpdates);
            ruleNameRepository.save(old);
            return findById(old.getId());
        }
        else 
            throw new Exception("Can't find current Bid");
    }
    
    /**
     * Deletes a rule name by its ID.
     * 
     * @param id the ID of the rule name to delete
     * @throws Exception if the rule name is not found
     */
    public void deleteRuleNameById(Integer id) throws Exception { 
        Optional<RuleName> ruleNameToDelete = ruleNameRepository.findById(id); 
        if (ruleNameToDelete.isPresent()) {
            log.info("Deleting ruleName with ID: {}", id);
            ruleNameRepository.deleteById(id);
        }
        else {
            throw new Exception("Can't find the ruleName for id: " + id);
        }
    }
}
