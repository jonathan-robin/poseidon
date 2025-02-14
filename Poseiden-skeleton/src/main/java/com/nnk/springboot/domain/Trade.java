package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity class representing a Trade in the system.
 * The class is mapped to the "trade" table in the database.
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "trade")
public class Trade {

    /**
     * Unique identifier for the trade.
     * Mapped to the "TradeId" column in the "trade" table.
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="TradeId")
    private Integer tradeId;

    /**
     * Account associated with the trade.
     * Mapped to the "account" column in the "trade" table.
     */
    @NotBlank(message="Account is mandatory")
    @Size(max=30, message= "Too long, must be 30 characters top")
    @Column(name="account")
    private String account;
    
    /**
     * Type of the trade (e.g., Buy/Sell).
     * Mapped to the "type" column in the "trade" table.
     */
    @NotBlank(message="Type is mandatory")
    @Size(max=30, message= "Too long, must be 30 characters top")
    @Column(name="type")
    private String type;
    
    /**
     * Quantity of the asset being bought.
     * Mapped to the "buyQuantity" column in the "trade" table.
     */
    @NotNull(message="Quantity cannot be null")
    @Column(name="buyQuantity")
    private Double buyQuantity;

    /**
     * Quantity of the asset being sold.
     * Mapped to the "sellQuantity" column in the "trade" table.
     */
    @Column(name="sellQuantity")
    @Positive(message = "sellQuantity  must be a postive double")
    private Double sellQuantity;
    
    /**
     * Price at which the asset is being bought.
     * Mapped to the "buyPrice" column in the "trade" table.
     */
    @Column(name="buyPrice")
    @Positive(message = "buyPrice  must be a postive double")
    private Double buyPrice;
    
    /**
     * Price at which the asset is being sold.
     * Mapped to the "sellPrice" column in the "trade" table.
     */
    @Column(name="sellPrice")
    @Positive(message = "sellPrice  must be a postive double")
    private Double sellPrice;
    
    /**
     * Benchmark associated with the trade.
     * Mapped to the "benchmark" column in the "trade" table.
     */
    @Column(name="benchmark")
    @Size(max=125,  message= "Too long, must be 125 characters top")
    private String benchmark;

    /**
     * Date when the trade occurred.
     * Mapped to the "tradeDate" column in the "trade" table.
     */
    @Column(name="tradeDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp tradeDate;
    
    /**
     * Security associated with the trade.
     * Mapped to the "security" column in the "trade" table.
     */
    @Column(name="security")
    @Size(max=125,  message= "Too long, must be 125 characters top")
    private String security;
    
    /**
     * Status of the trade (e.g., completed, pending).
     * Mapped to the "status" column in the "trade" table.
     */
    @Column(name="status")
    @Size(max=10,  message= "Too long, must be 10 characters top")
    private String status;

    /**
     * Trader associated with the trade.
     * Mapped to the "trader" column in the "trade" table.
     */
    @Column(name="trader")
    @Size(max=125,  message= "Too long, must be 125 characters top")
    private String trader;

    /**
     * Book associated with the trade.
     * Mapped to the "book" column in the "trade" table.
     */
    @Column(name="book")
    @Size(max=125,  message= "Too long, must be 125 characters top")
    private String book;
    
    /**
     * Name of the creator of the trade.
     * Mapped to the "creationName" column in the "trade" table.
     */
    @Column(name="creationName")
    @Size(max=125,  message= "Too long, must be 125 characters top")
    private String creationName;
    
    /**
     * Date when the trade was created.
     * Mapped to the "creationDate" column in the "trade" table.
     */
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="creationDate", updatable=false, nullable=false)
    private Timestamp creationDate;
   
    /**
     * Name of the person who revised the trade.
     * Mapped to the "revisionName" column in the "trade" table.
     */
    @Column(name="revisionName")
    @Size(max=125,  message= "Too long, must be 125 characters top")
    private String revisionName;
    
    /**
     * Date when the trade was revised.
     * Mapped to the "revisionDate" column in the "trade" table.
     */
    @Column(name="revisionDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp revisionDate;
    
    /**
     * Name of the deal associated with the trade.
     * Mapped to the "dealName" column in the "trade" table.
     */
    @Column(name="dealName")
    @Size(max=125,  message= "Too long, must be 125 characters top")
    private String dealName;

    /**
     * Type of the deal associated with the trade.
     * Mapped to the "dealType" column in the "trade" table.
     */
    @Column(name="dealType")
    @Size(max=125,  message= "Too long, must be 125 characters top")
    private String dealType;
    
    /**
     * Source list identifier associated with the trade.
     * Mapped to the "sourceListId" column in the "trade" table.
     */
    @Column(name="sourceListId")
    @Size(max=125,  message= "Too long, must be 125 characters top")
    private String sourceListId;
    
    /**
     * Side of the trade
     * Mapped to the "side" column in the "trade" table.
     */
    @Column(name="side")
    @Size(max=125,  message= "Too long, must be 125 characters top")
    private String side;
}
