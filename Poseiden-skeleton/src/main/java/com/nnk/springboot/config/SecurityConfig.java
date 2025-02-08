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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.nnk.springboot.services.CustomUserDetailsService;

import org.springframework.security.core.userdetails.UserDetails;


@Configuration
@EnableWebSecurity
public class SecurityConfig{
	

   @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }

    /**
     * Secure the http access
     *
     * @param http HttpSecurity
     * @return http
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
        		
        		.authorizeHttpRequests((requests) -> requests
                .requestMatchers( "/", "/login", "/app/login", "/app/error").permitAll()
                .requestMatchers("/user/*").hasAuthority("ADMIN")
                //Authentication request parameters
                .anyRequest().authenticated()
                
                )
                .formLogin((formLogin) -> formLogin
                        //.usernameParameter("user.username")
                        .defaultSuccessUrl("/bidList/list", true)
                )
                //logout
                .logout((logout) -> logout.permitAll()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/app-logout"))
                        //Definition of the default logout success URL
                        .logoutSuccessUrl("/")
                        //Invalidate the current HTTP session and its cookies
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                ).exceptionHandling((exceptionHandling) -> exceptionHandling.accessDeniedPage("/app/error"))
                
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // Create session if needed
                .maximumSessions(1)  // Limit to one session per user
                .expiredUrl("/login?expired=true");  // Redirect if the session expires
                //In case of exception of an access denied
               
                

        return http.build();
    }

    /**
     *Bcrypt uses hash algorithm to store password
     *
     * @return passwordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Function for allowing the loading of the static resources
     *
     * @return
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web
                .ignoring()
                .requestMatchers("/css/**");
    }
    
}