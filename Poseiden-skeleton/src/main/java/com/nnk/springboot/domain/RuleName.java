package com.nnk.springboot.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "rulename")
public class RuleName {
	
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="Id")
	private Integer id;
    
    @Column(name="name")
	private String name;
    
    @Column(name="description")
	private String description;
    
    @Column(name="json")
	private String json;

    @Column(name="template")
 	private String template;
    
    @Column(name="sqlStr")
 	private String sqlStr;
    
    @Column(name="sqlPart")
 	private String sqlPart;

}
