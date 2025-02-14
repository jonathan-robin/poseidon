package com.nnk.springboot.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	/**
	 * <p>This is meant to send back all the users saved in DB. . . </p>
	 * @param no Parameters
	 * @return List of User
	 * @since 1.0
	 */
	public List<User> findAll(){ 
		return userRepository.findAll();
	}
	
	/**
	 * <p>Method to save user in DB</p>
	 * <p>It first hashes the password</p>
	 * @param The user we want to save in DB
	 * @return void
	 * @since 1.0
	 */
	public void saveUser(User user) { 
	    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	    user.setPassword(encoder.encode(user.getPassword()));
	    log.info("Saving new user {}...", user.toString());
	    userRepository.save(user); 
	}
	
	/**
	 * <p>Method to update user in DB</p>
	 * <p>It hashes the password. It override the saveUser method by passing a existing user ID so it UPSERT</p>
	 * @param user - The user we want to save in DB
	 * @param id - The user ID we want to update
	 * @return void
	 * @since 1.0
	 */
	public void saveUser(User user, Integer id) { 
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setId(id);
        log.info("Updating user with ID: {}", id);
        userRepository.save(user);
	}
	
	/**
	 * <p>Method to find a user with a specific ID</p>
	 * <p>It searches for the user in case we don't find throw exception </p>
	 * @param The user ID (Integer)
	 * @return The User
	 * @throw IllegalArgumentException (in case we don't find by id)
	 * @since 1.0
	 */
	public User findById(Integer id) { 
		Optional<User> user = userRepository.findById(id); 
		
		if (user.isPresent())
			return user.get(); 
		
		else 
			throw new IllegalArgumentException("Invalid user Id:" + id);
	}
	
	/**
	 * <p>Method to delete a user with a specific ID</p>
	 * <p>It searches for the user in case we don't find throw exception </p>
	 * @param The user ID (Integer)
	 * @return void
	 * @throw New Exception (in case we don't find by id)
	 * @since 1.0
	 */
	public void deleteUserById(Integer id) throws Exception { 
		Optional<User> userToDelete = userRepository.findById(id); 
		if (userToDelete.isPresent()) {
			log.info("Deleting user with ID: {}", id);
			userRepository.deleteById(id);
		}
		else {
			throw new Exception("Can't find the user for id: " + id);
		}
	}

	
    private final Map<String, Boolean> disabledUsers = new ConcurrentHashMap<>();

    public void disableUser(String username) {
        disabledUsers.put(username, true);
    }

    public void enableUser(String username) {
        disabledUsers.remove(username);
    }

    public boolean isUserEnabled(String username) {
        return !disabledUsers.getOrDefault(username, false);
    }
}
