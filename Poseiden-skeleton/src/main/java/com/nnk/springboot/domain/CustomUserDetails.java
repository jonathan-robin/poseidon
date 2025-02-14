package com.nnk.springboot.domain;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;

/**
 * Custom implementation of the UserDetails interface to represent the details of a user in the system.
 * This class is used by Spring Security to provide authentication and authorization details.
 */
@Component
@NoArgsConstructor
public class CustomUserDetails implements UserDetails {
    
    /**
     * Serial version UID for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The username of the user.
     */
    private String username;
    
    /**
     * The password of the user.
     */
    private String password;
    
    private boolean enabled;
    
    /**
     * The list of authorities (roles) granted to the user.
     */
    private List<GrantedAuthority> authorities;

    /**
     * Retrieves the username of the user.
     * 
     * @return The username of the user.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Retrieves the password of the user.
     * 
     * @return The password of the user.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Indicates whether the account is expired.
     * 
     * @return true if the account is not expired, false otherwise.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the account is locked.
     * 
     * @return true if the account is not locked, false otherwise.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the credentials of the user are expired.
     * 
     * @return true if the credentials are not expired, false otherwise.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled (not disabled).
     * 
     * @return true if the user is enabled, false otherwise.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Retrieves the authorities (roles) granted to the user.
     * 
     * @return A collection of granted authorities for the user.
     */
    @Override
    public java.util.Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }
    
    public CustomUserDetails(String username, String password, List<GrantedAuthority> authorities, boolean enabled) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
    }
}
