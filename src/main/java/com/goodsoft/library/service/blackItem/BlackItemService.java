package com.goodsoft.library.service.blackItem;

import com.goodsoft.library.domain.BlackItem;

import java.util.List;

public interface BlackItemService {
    BlackItem save(BlackItem itemMore);

    List<BlackItem> getAll();

    boolean delete(Long id);

    boolean exitsByName(String name);

    boolean exitsByList(List<String> names);

    void saveAll(List<BlackItem> blackItems);

}
