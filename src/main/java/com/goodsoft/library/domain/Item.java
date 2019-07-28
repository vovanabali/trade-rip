package com.goodsoft.library.domain;

import com.goodsoft.library.enums.ItemPriceType;
import com.goodsoft.library.enums.ItemSiteType;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Item name
     */
    private String marketHashName;
    /**
     * Identity it's main info for get item from c5 or market
     */
    private String identity;
    /**
     * Item site type
     */
    @Enumerated(EnumType.STRING)
    private ItemSiteType itemSiteType;
    /**
     * Item price
     */
    private double price;
    /**
     * Item last  date
     */
    private Date lastDateUpdate;
    /**
     * Item price type
     */
    @Enumerated(EnumType.STRING)
    private ItemPriceType priceType;
}
