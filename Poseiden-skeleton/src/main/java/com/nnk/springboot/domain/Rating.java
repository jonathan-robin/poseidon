package com.nnk.springboot.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "rating")
public class Rating {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="Id")
	private Integer id;
    
    @Column(name="moodysRating")
	private String moodysRating;
    
    @Column(name="sandPRating")
	private String sandPRating;
    
    @Column(name="fitchRating")
	private String fitchRating;

    @Column(name="orderNumber")
 	private Integer orderNumber;
    
    public Rating(String moodysRating, String sandPRating, String fitchRating, Integer orderNumber) { 
    	this.moodysRating = moodysRating;
    	this.sandPRating = sandPRating; 
    	this.fitchRating = fitchRating; 
    	this.orderNumber = orderNumber;
    }

}
