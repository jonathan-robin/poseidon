package com.nnk.springboot.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

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
    @Positive(message = "CurveId must be a postive double")
    @NotNull(message = "Curve point Id is mandatory")
    @Column(name="curveId")
    private Integer curveId;
    
    /**
     * The date when the curve point data is effective.
     */
    @FutureOrPresent(message = "Must be a future date")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    @Column(name="asOfDate")
    private Timestamp asOfDate;
 
    /**
     * The term associated with the curve point, usually representing a time value (e.g., in years or months).
     */
    @NotNull(message = "Term is mandatory")
    @Positive(message = "Term  must be a postive double")
    @Column(name="term")
    private Double term;

    /**
     * The creation date of the curve point record.
     */
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="creationDate", updatable=false, nullable=false)
    private Timestamp creationDate;
    
    /**
     * The value associated with the curve point, which represents the curve's value at the given term.
     */
    @Column(name="value")
    @Positive(message = "value must be a postive double")
    @NotNull(message = "value is mandatory")
    private Double value;
}
