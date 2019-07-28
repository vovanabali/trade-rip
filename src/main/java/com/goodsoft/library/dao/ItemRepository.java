package com.goodsoft.library.dao;

import com.goodsoft.library.domain.Item;
import com.goodsoft.library.enums.ItemPriceType;
import com.goodsoft.library.enums.ItemSiteType;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends PagingAndSortingRepository<Item, Long> {
    /**
     * Find item by market name and type
     *
     * @param name     - Market Hash Name
     * @param type     - site type
     * @param identity - identity
     * @return item
     */
    Item findByMarketHashNameAndItemSiteTypeAndIdentityAndPriceType(String name, ItemSiteType type, String identity, ItemPriceType priceType);

    Item findFirstByMarketHashNameAndItemSiteTypeAndPriceType(String name, ItemSiteType type, ItemPriceType priceType);

    /**
     * Find all items by site type and price type
     *
     * @param siteType - site type
     * @param itemPriceType - item price type
     * @return list of item
     */
    List<Item> findAllByItemSiteTypeAndPriceType(ItemSiteType siteType, ItemPriceType itemPriceType);

    List<Item> findAllByMarketHashNameAndItemSiteType(String name, ItemSiteType type);
}
