package com.nnk.springboot.validation;

import java.util.Optional;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.helper.SpringContext;
import com.nnk.springboot.repositories.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String>{

	@Autowired
    private UserRepository userRepository;
//
//    @Autowired
//    public UniqueUsernameValidator(UserRepository userRepository) { // Injection par constructeur
//        this.userRepository = userRepository;
//    }
//    
    
	
	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) {
		if (username == null || username.trim().isEmpty()) {
			return true;
		}
		
		Optional<User> user = userRepository.findByUsernameOptional(username);
		if (user.isPresent())
				return false;
		
		return true;

	}
	
}
