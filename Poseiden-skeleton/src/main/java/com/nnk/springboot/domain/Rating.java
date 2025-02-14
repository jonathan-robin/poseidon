package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity class representing a Rating in the system.
 * The class is mapped to the "rating" table in the database.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "rating")
public class Rating {

    /**
     * Unique identifier for the rating.
     * Mapped to the "Id" column in the "rating" table.
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="Id")
    private Integer id;
    
    /**
     * Moody's rating of the entity.
     * Mapped to the "moodysRating" column in the "rating" table.
     */
    @Size(max=125,  message= "Too long, must be 125 characters top")
    @Column(name="moodysRating")
    private String moodysRating;
    
    /**
     * S&P rating of the entity.
     * Mapped to the "sandPRating" column in the "rating" table.
     */
    @Size(max=125,  message= "Too long, must be 125 characters top")
    @Column(name="sandPRating")
    private String sandPRating;
    
    /**
     * Fitch rating of the entity.
     * Mapped to the "fitchRating" column in the "rating" table.
     */
    @Size(max=125,  message= "Too long, must be 125 characters top")
    @Column(name="fitchRating")
    private String fitchRating;

    /**
     * Order number of the rating.
     * Mapped to the "orderNumber" column in the "rating" table.
     */
    @Column(name="orderNumber")
    @Positive(message = "CurveId must be a postive double")
    private Integer orderNumber;
    
    /**
     * Constructor for initializing a Rating object.
     * 
     * @param moodysRating Moody's rating of the entity.
     * @param sandPRating S&P rating of the entity.
     * @param fitchRating Fitch rating of the entity.
     * @param orderNumber Order number of the rating.
     */
    public Rating(String moodysRating, String sandPRating, String fitchRating, Integer orderNumber) { 
        this.moodysRating = moodysRating;
        this.sandPRating = sandPRating; 
        this.fitchRating = fitchRating; 
        this.orderNumber = orderNumber;
    }

}
