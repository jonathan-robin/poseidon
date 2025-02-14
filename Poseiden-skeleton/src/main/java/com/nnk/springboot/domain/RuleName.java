package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity class representing a RuleName in the system.
 * The class is mapped to the "rulename" table in the database.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "rulename")
public class RuleName {
    
    /**
     * Unique identifier for the rule.
     * Mapped to the "Id" column in the "rulename" table.
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="Id")
    private Integer id;
    
    /**
     * Name of the rule.
     * Mapped to the "name" column in the "rulename" table.
     */
    @Size(max=125,  message= "Too long, must be 125 characters top")
    @NotBlank(message = "Name is mandatory")
    @Column(name="name")
    private String name;
    
    /**
     * Description of the rule.
     * Mapped to the "description" column in the "rulename" table.
     */
    @Size(max=125,  message= "Too long, must be 125 characters top")
    @NotBlank(message = "Description is mandatory")
    @Column(name="description")
    private String description;
    
    /**
     * JSON representation of the rule's configuration.
     * Mapped to the "json" column in the "rulename" table.
     */
    @Size(max=125,  message= "Too long, must be 125 characters top")
    @NotBlank(message = "Json is mandatory")
    @Column(name="json")
    private String json;

    /**
     * Template for the rule.
     * Mapped to the "template" column in the "rulename" table.
     */
    @Size(max=512,  message= "Too long, must be 512 characters top")
    @NotBlank(message = "template is mandatory")
    @Column(name="template")
    private String template;
    
    /**
     * SQL statement associated with the rule.
     * Mapped to the "sqlStr" column in the "rulename" table.
     */
    @Size(max=125,  message= "Too long, must be 125 characters top")
    @NotBlank(message = "sqlStr is mandatory")
    @Column(name="sqlStr")
    private String sqlStr;
    
    /**
     * SQL fragment used in the rule.
     * Mapped to the "sqlPart" column in the "rulename" table.
     */
    @Size(max=125,  message= "Too long, must be 125 characters top")
    @NotBlank(message = "sqlPart is mandatory")
    @Column(name="sqlPart")
    private String sqlPart;
    
    /**
     * Constructor for initializing a RuleName object with specific attributes.
     * 
     * @param name        The name of the rule.
     * @param description The description of the rule.
     * @param json        The JSON representation of the rule's configuration.
     * @param template    The template for the rule.
     * @param sqlStr      The SQL string associated with the rule.
     * @param sqlPart     The SQL part used in the rule.
     */
    public RuleName(String name, String description, String json, String template, String sqlStr, String sqlPart) { 
        this.name = name;
        this.description = description; 
        this.json = json; 
        this.template = template; 
        this.sqlStr = sqlStr; 
        this.sqlPart = sqlPart; 
    }

}
