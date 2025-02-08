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
	
	public List<RuleName> findAllRuleNames(){ 
		return ruleNameRepository.findAll();
	}
	
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
	
	public RuleName findById(Integer id) throws Exception { 
		Optional<RuleName> ruleName = ruleNameRepository.findById(id); 
		
		if (ruleName.isPresent())
			return ruleName.get(); 
		
		else 
			throw new Exception("can't retrieve ruleName with id " + id);
	}
	
	
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


