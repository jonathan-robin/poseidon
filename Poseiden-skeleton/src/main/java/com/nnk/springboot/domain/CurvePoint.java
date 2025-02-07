package com.nnk.springboot.domain;

import org.hibernate.validator.constraints.Length;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "curvepoint")
public class CurvePoint {
	
    @Id
    @Column(name="Id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	Integer id;
    
    @Column(name="curveId")
   	private Integer curveId;
       
    @Column(name="asOfDate")
   	private Timestamp asOfDate;
       
    @Column(name="term")
   	private Double term;

    @Column(name="creationDate")
	private Timestamp creationDate;
    
    @Column(name="value")
	private Double value;

}
