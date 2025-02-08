package com.nnk.springboot.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import java.sql.Date;
import java.sql.Timestamp;

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
    private String account;

    /**
     * The type of the bid (e.g., "Buy" or "Sell").
     */
    @Column(name="type")
    private String type;

    /**
     * The quantity for the bid offer.
     */
    @Column(name="bidQuantity")
    private Double bidQuantity;

    /**
     * The quantity for the ask offer.
     */
    @Column(name="askQuantity")
    private Double askQuantity;

    /**
     * The bid price.
     */
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
    private String benchmark;

    /**
     * The date the bid was recorded in the bid list.
     */
    @Column(name="bidListDate")
    private Timestamp bidListDate;

    /**
     * Commentary associated with the bid.
     */
    @Column(name="commentary")
    private String commentary;

    /**
     * The security or financial instrument associated with the bid.
     */
    @Column(name="security")
    private String security;

    /**
     * The current status of the bid (e.g., "Active", "Closed").
     */
    @Column(name="status")
    private String status;

    /**
     * The name of the trader who created the bid.
     */
    @Column(name="trader")
    private String trader;

    /**
     * The book to which the bid belongs.
     */
    @Column(name="book")
    private String book;

    /**
     * The name of the person who created the bid.
     */
    @Column(name="creationName")
    private String creationName;

    /**
     * The date the bid was created.
     */
    @Column(name="creationDate")
    private Timestamp creationDate;

    /**
     * The name of the person who revised the bid.
     */
    @Column(name="revisionName")
    private String revisionName;

    /**
     * The date the bid was revised.
     */
    @Column(name="revisionDate")
    private Timestamp revisionDate;

    /**
     * The name of the deal associated with the bid.
     */
    @Column(name="dealName")
    private String dealName;

    /**
     * The type of deal associated with the bid.
     */
    @Column(name="dealType")
    private String dealType;

    /**
     * The identifier of the source list for the bid.
     */
    @Column(name="sourceListId")
    private String sourceListId;

    /**
     * The side of the bid (e.g., "Buy" or "Sell").
     */
    @Column(name="side")
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
