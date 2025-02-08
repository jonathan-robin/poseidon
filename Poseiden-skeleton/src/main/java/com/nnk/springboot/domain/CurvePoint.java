package com.nnk.springboot.domain;

import org.hibernate.validator.constraints.Length;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "curvepoint")
public class CurvePoint {
	
    @Id
    @Column(name="Id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer id;
    
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
