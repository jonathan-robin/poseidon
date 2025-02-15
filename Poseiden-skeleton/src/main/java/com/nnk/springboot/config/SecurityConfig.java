package com.nnk.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.nnk.springboot.services.CustomUserDetailsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
;

/**
 * Configuration class for Spring Security settings in the application.
 * Configures authentication, authorization, password encoding, session management, and static resource handling.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Bean for AuthenticationManager used for authenticating users.
     * 
     * @param http The HttpSecurity object used to configure the security settings.
     * @param bCryptPasswordEncoder The password encoder used to validate user passwords.
     * @return The AuthenticationManager bean.
     * @throws Exception If there is an error configuring the AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }

    /**
     * Configures HTTP security for the application, including authentication and authorization.
     * Defines which URLs are accessible to which roles, configures login/logout behaviors, session management,
     * and exception handling for access-denied scenarios.
     * 
     * @param http The HttpSecurity object used to configure the HTTP security settings.
     * @return The configured SecurityFilterChain.
     * @throws Exception If there is an error configuring the HTTP security.
     */
    @SuppressWarnings("removal")
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	
        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/", "/login", "/app/login", "/app/error", "/user", "/user/list/**", "/user/**").permitAll()  // Permit all users to access these paths
//                .requestMatchers("/user/*").hasAuthority("ROLE_ADMIN")  // Only allow users with "ROLE_ADMIN" to access user-related pages
                .anyRequest().authenticated()  // Require authentication for any other request
            )
            .formLogin((formLogin) -> formLogin
                .defaultSuccessUrl("/bidList/list", true)  // Redirect to the bid list on successful login
            )
            .logout((logout) -> logout
            	.permitAll()
                .logoutRequestMatcher(new AntPathRequestMatcher("/app-logout"))  // Specify the logout URL
                .logoutSuccessUrl("/")  // Redirect to home page on logout success
                .invalidateHttpSession(true)  // Invalidate the session on logout
                .deleteCookies("JSESSIONID")  // Delete the session cookie on logout
            ) 
            .exceptionHandling((exceptionHandling) -> exceptionHandling.accessDeniedPage("/app/error"))  // Handle access denied exceptions
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // Create session only if required
                .maximumSessions(1)  // Limit to one session per user
                .expiredUrl("/login?expired=true");  // Redirect to login page if the session expires

        return http.build();
    }

    /**
     * Bean for encoding passwords using the BCrypt hash algorithm.
     * 
     * @return A PasswordEncoder configured with the BCrypt algorithm.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Allows static resources (e.g., CSS files) to be loaded without security constraints.
     * 
     * @return The WebSecurityCustomizer to customize web security for static resources.
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web
            .ignoring()
            .requestMatchers("/css/**");  // Allow access to static resources in the "/css" directory without authentication
    }
    

    
}
