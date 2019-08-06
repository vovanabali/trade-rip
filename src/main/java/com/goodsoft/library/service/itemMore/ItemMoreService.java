package com.goodsoft.library.service.itemMore;

import com.goodsoft.library.domain.ItemMore;

import java.io.IOException;
import java.util.List;

public interface ItemMoreService {
    ItemMore save(ItemMore itemMore);

    List<ItemMore> getAll();

    boolean delete(Long id);

    ItemMore getByItemName(String itemName);

    List<ItemMore> getAllNoItems();

    List<ItemMore> getAlItems();

    List<ItemMore> loadFromC5() throws IOException, InterruptedException;

    void updateWasBuy();

    void deleteAll();
}
