package com.nnk.springboot.domain;

import jakarta.persistence.*;
import java.sql.Timestamp;
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
    @Column(name="account")
    private String account;
    
    /**
     * Type of the trade (e.g., Buy/Sell).
     * Mapped to the "type" column in the "trade" table.
     */
    @Column(name="type")
    private String type;
    
    /**
     * Quantity of the asset being bought.
     * Mapped to the "buyQuantity" column in the "trade" table.
     */
    @Column(name="buyQuantity")
    private Double buyQuantity;

    /**
     * Quantity of the asset being sold.
     * Mapped to the "sellQuantity" column in the "trade" table.
     */
    @Column(name="sellQuantity")
    private Double sellQuantity;
    
    /**
     * Price at which the asset is being bought.
     * Mapped to the "buyPrice" column in the "trade" table.
     */
    @Column(name="buyPrice")
    private Double buyPrice;
    
    /**
     * Price at which the asset is being sold.
     * Mapped to the "sellPrice" column in the "trade" table.
     */
    @Column(name="sellPrice")
    private Double sellPrice;
    
    /**
     * Benchmark associated with the trade.
     * Mapped to the "benchmark" column in the "trade" table.
     */
    @Column(name="benchmark")
    private String benchmark;

    /**
     * Date when the trade occurred.
     * Mapped to the "tradeDate" column in the "trade" table.
     */
    @Column(name="tradeDate")
    private Timestamp tradeDate;
    
    /**
     * Security associated with the trade.
     * Mapped to the "security" column in the "trade" table.
     */
    @Column(name="security")
    private String security;
    
    /**
     * Status of the trade (e.g., completed, pending).
     * Mapped to the "status" column in the "trade" table.
     */
    @Column(name="status")
    private String status;

    /**
     * Trader associated with the trade.
     * Mapped to the "trader" column in the "trade" table.
     */
    @Column(name="trader")
    private String trader;

    /**
     * Book associated with the trade.
     * Mapped to the "book" column in the "trade" table.
     */
    @Column(name="book")
    private String book;
    
    /**
     * Name of the creator of the trade.
     * Mapped to the "creationName" column in the "trade" table.
     */
    @Column(name="creationName")
    private String creationName;
    
    /**
     * Date when the trade was created.
     * Mapped to the "creationDate" column in the "trade" table.
     */
    @Column(name="creationDate")
    private Timestamp creationDate;
   
    /**
     * Name of the person who revised the trade.
     * Mapped to the "revisionName" column in the "trade" table.
     */
    @Column(name="revisionName")
    private String revisionName;
    
    /**
     * Date when the trade was revised.
     * Mapped to the "revisionDate" column in the "trade" table.
     */
    @Column(name="revisionDate")
    private Timestamp revisionDate;
    
    /**
     * Name of the deal associated with the trade.
     * Mapped to the "dealName" column in the "trade" table.
     */
    @Column(name="dealName")
    private String dealName;

    /**
     * Type of the deal associated with the trade.
     * Mapped to the "dealType" column in the "trade" table.
     */
    @Column(name="dealType")
    private String dealType;
    
    /**
     * Source list identifier associated with the trade.
     * Mapped to the "sourceListId" column in the "trade" table.
     */
    @Column(name="sourceListId")
    private String sourceListId;
    
    /**
     * Side of the trade (e.g., Buy/Sell).
     * Mapped to the "side" column in the "trade" table.
     */
    @Column(name="side")
    private String side;
}
