package com.nnk.springboot.services;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.CustomUserDetails;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;

    /**
     * Loads a user by their username from the database.
     * 
     * @param username the username of the user to load
     * @return UserDetails object containing the user's information
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	
        User user = userRepository.findByUsername(username);

        if (user == null) 
        	throw new UsernameNotFoundException("Invalid credentials");	

        return new CustomUserDetails(user.getUsername(),
                user.getPassword(), getGrantedAuthorities(user.getRole()), userService.isUserEnabled(username));

    }

    /**
     * Retrieves the granted authorities based on the user's role.
     * 
     * @param role the role of the user (e.g., "ADMIN", "USER")
     * @return a list of granted authorities corresponding to the user's role
     */
    private List<GrantedAuthority> getGrantedAuthorities(String role) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        return authorities;
    }
}
