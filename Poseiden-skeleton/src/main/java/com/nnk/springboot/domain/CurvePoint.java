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

/**
 * Entity representing a curve point in the system.
 * A curve point holds data related to a specific curve, such as its term, value, and associated timestamp.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "curvepoint")
public class CurvePoint {
    
    /**
     * The unique identifier for the curve point.
     */
    @Id
    @Column(name="Id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    
    /**
     * The ID of the associated curve for this curve point.
     */
    @Column(name="curveId")
    private Integer curveId;
    
    /**
     * The date when the curve point data is effective.
     */
    @Column(name="asOfDate")
    private Timestamp asOfDate;
    
    /**
     * The term associated with the curve point, usually representing a time value (e.g., in years or months).
     */
    @Column(name="term")
    private Double term;

    /**
     * The creation date of the curve point record.
     */
    @Column(name="creationDate")
    private Timestamp creationDate;
    
    /**
     * The value associated with the curve point, which represents the curve's value at the given term.
     */
    @Column(name="value")
    private Double value;
}
