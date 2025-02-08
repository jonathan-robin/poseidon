package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * Entity class representing a User in the system.
 * The class is mapped to the "users" table in the database.
 * 
 * @author me
 */
@Entity
@Table(name = "users")
public class User {
    
    /**
     * Auto-increment ID used to uniquely identify a user.
     * Mapped to the "id" column in the "users" table.
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    
    /**
     * Username of the user.
     * It is a mandatory field, validated with a not-blank constraint.
     * Mapped to the "username" column in the "users" table.
     */
    @NotBlank(message = "Username is mandatory")
    private String username;
    
    /**
     * Password for the user.
     * It is a mandatory field, validated with a not-blank constraint.
     * Mapped to the "password" column in the "users" table.
     */
    @NotBlank(message = "Password is mandatory")
    private String password;
    
    /**
     * Full name of the user.
     * It is a mandatory field, validated with a not-blank constraint.
     * Mapped to the "fullname" column in the "users" table.
     */
    @NotBlank(message = "FullName is mandatory")
    private String fullname;
    
    /**
     * Role assigned to the user (e.g., ADMIN/USER).
     * It is a mandatory field, validated with a not-blank constraint.
     * Some pages are visible only by ADMIN users.
     * Mapped to the "role" column in the "users" table.
     */
    @NotBlank(message = "Role is mandatory")
    private String role;

    /**
     * Gets the unique identifier for the user.
     * 
     * @return the user ID.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the user.
     * 
     * @param id the user ID.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the username of the user.
     * 
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for the user.
     * 
     * @param username the username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the user.
     * 
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the user.
     * 
     * @param password the password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the full name of the user.
     * 
     * @return the full name.
     */
    public String getFullname() {
        return fullname;
    }

    /**
     * Sets the full name for the user.
     * 
     * @param fullname the full name.
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    /**
     * Gets the role of the user.
     * 
     * @return the role.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role for the user.
     * 
     * @param role the role.
     */
    public void setRole(String role) {
        this.role = role;
    }
}
