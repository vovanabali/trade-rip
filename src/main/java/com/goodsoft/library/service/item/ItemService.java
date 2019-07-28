package com.goodsoft.library.service.item;

import com.goodsoft.library.domain.Item;
import com.goodsoft.library.enums.ItemPriceType;

import java.util.List;

public interface ItemService {
    Item save(Item item);

    Item saveUnique(Item item);

    Item getC5ItemByMarketName(String name);

    List<Item> getAllC5Items(ItemPriceType priceType);

    List<Item> getAllMarketItemsByMarketHashName(String marketHashName);
}
