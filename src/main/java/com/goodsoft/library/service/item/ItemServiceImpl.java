package com.goodsoft.library.service.item;

import com.goodsoft.library.dao.ItemRepository;
import com.goodsoft.library.domain.Item;
import com.goodsoft.library.enums.ItemPriceType;
import com.goodsoft.library.enums.ItemSiteType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Item saveUnique(Item item) {
        Item itemInBd = itemRepository.findByMarketHashNameAndItemSiteTypeAndIdentityAndPriceType(
                item.getMarketHashName(),
                item.getItemSiteType(),
                item.getIdentity(),
                item.getPriceType()
        );
        if (Objects.nonNull(itemInBd)) {
            item.setId(itemInBd.getId());
        }
        return save(item);
    }

    @Override
    public Item getC5ItemByMarketName(String name) {
        return itemRepository.findFirstByMarketHashNameAndItemSiteTypeAndPriceType(
                name,
                ItemSiteType.C5,
                ItemPriceType.SELL
        );
    }

    @Override
    public List<Item> getAllC5Items(ItemPriceType priceType) {
        return itemRepository.findAllByItemSiteTypeAndPriceType(ItemSiteType.C5, priceType);
    }

    @Override
    public List<Item> getAllMarketItemsByMarketHashName(String marketHashName) {
        return itemRepository.findAllByMarketHashNameAndItemSiteType(marketHashName, ItemSiteType.MARKET);
    }
}
