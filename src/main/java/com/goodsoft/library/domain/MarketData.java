package com.goodsoft.library.domain;

import com.goodsoft.library.enums.ItemPriceType;
import com.goodsoft.library.enums.ItemSiteType;
import lombok.Data;

import java.util.Date;

@Data
public class MarketData {
    private String i_quality;
    private String i_name_color;
    private String i_classid;
    private String i_instanceid;
    private String i_market_hash_name;
    private String i_market_name;
    private double ui_price;
    private String ui_currency;
    private String app;

    public Item toItem() {
        Item item = new Item();
        item.setItemSiteType(ItemSiteType.MARKET);
        item.setMarketHashName(getI_market_hash_name());
        item.setPrice(getUi_price());
        item.setLastDateUpdate(new Date());
        item.setIdentity(getI_classid() + "_" + getI_instanceid());
        item.setPriceType(ItemPriceType.BUY);
        return item;
    }
}
