package com.goodsoft.library.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double discountForBuy;
    private double discountForSell;
    private String marketApi;
    private String tradeLink;
    private double buyProfit;
    private int maxLastGetC5Price;
    /**
     * Min itm price for buy
     */
    private double minItemPrice;

    private boolean listen;

    private double c5ToMarket;

    private double currentCurrency;

    /**
     * Min profit in currency
     */
    private double minProfit;

    public Settings(String s) {
        setMarketApi("");
    }
}
