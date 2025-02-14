package com.nnk.springboot.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.sql.Date;
import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Represents an item in the bid list.
 * <p>
 * This entity is mapped to the "bidlist" table in the database.
 * It contains information about the bid and ask prices and quantities for specific securities in a trading book.
 * </p>
 */
@Entity
@Getter
@NoArgsConstructor
@Setter
@Table(name = "bidlist")
public class BidList {

    /**
     * The unique identifier for the bid (primary key).
     */
    @Id
    @Column(name="BidListId")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    /**
     * The account associated with the bid.
     */
    @Column(name="account")
    @NotBlank(message="Account is mandatory")
    @Size(max=50)
    private String account;

    /**
     * The type of the bid (e.g., "Buy" or "Sell").
     */
    @Column(name="type")
    @NotBlank(message="Type is mandatory")
    @Size(max=50)
    private String type;

    /**
     * The quantity for the bid offer.
     */
    @Positive(message= "Must be a positive double number")
    @Column(name="bidQuantity")
    private Double bidQuantity;

    /**
     * The quantity for the ask offer.
     */
    @Positive(message= "Must be a positive double number")
    @Column(name="askQuantity")
    private Double askQuantity;

    /**
     * The bid price.
     */
    @Positive(message= "Must be a positive double number")
    @Column(name="bid")
    private Double bid;

    /**
     * The ask price.
     */
    @Column(name="ask")
    private Double ask;

    /**
     * The benchmark reference used for the bid.
     */
    @Column(name="benchmark")
    @Size(max=125)
    private String benchmark;

    /**
     * The date the bid was recorded in the bid list.
     */
    @FutureOrPresent(message = "The date should be a date in the future or now")
    @Column(name = "bidListDate")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Timestamp bidListDate;

    /**
     * Commentary associated with the bid.
     */
    @Size(max=125)
    @Column(name="commentary")
    private String commentary;

    /**
     * The security or financial instrument associated with the bid.
     */
    @Column(name="security")
    @Size(max=125)
    private String security;

    /**
     * The current status of the bid (e.g., "Active", "Closed").
     */
    @Column(name="status")
    @Size(max=10)
    private String status;

    /**
     * The name of the trader who created the bid.
     */
    @Column(name="trader")
    @Size(max=125)
    private String trader;

    /**
     * The book to which the bid belongs.
     */
    @Column(name="book")
    @Size(max=125)
    private String book;

    /**
     * The name of the person who created the bid.
     */
    @Column(name="creationName")
    @Size(max=125)
    private String creationName;

    /**
     * The date the bid was created.
     */
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="creationDate", updatable=false, nullable=false)
    private Timestamp creationDate;

    /**
     * The name of the person who revised the bid.
     */
    @Column(name="revisionName")
    @Size(max=125)
    private String revisionName;

    /**
     * The date the bid was revised.
     */
    @Column(name="revisionDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp revisionDate;

    /**
     * The name of the deal associated with the bid.
     */
    @Column(name="dealName")
    @Size(max=125)
    private String dealName;

    /**
     * The type of deal associated with the bid.
     */
    @Column(name="dealType")
    @Size(max=125)
    private String dealType;

    /**
     * The identifier of the source list for the bid.
     */
    @Column(name="sourceListId")
    @Size(max=125)
    private String sourceListId;

    /**
     * The side of the bid
     */
    @Column(name="side")
    @Size(max=125)
    private String side;

    /**
     * Constructor for creating a new BidList instance.
     * 
     * @param account The account associated with the bid.
     * @param type The type of the bid.
     * @param bidQuantity The quantity of the bid offer.
     */
    public BidList(String account, String type, double bidQuantity) {
        this.account = account;
        this.type = type;
        this.bidQuantity = bidQuantity;
    }

}
