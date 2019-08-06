package com.goodsoft.library.dao;

import com.goodsoft.library.domain.BlackItem;
import com.goodsoft.library.domain.ItemMore;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BlackItemRepository extends PagingAndSortingRepository<BlackItem, Long> {
    List<BlackItem> findAll();

    BlackItem findFirstByName(String name);

    boolean existsByName(String name);

    List<BlackItem> findAllByNameIsNotNull();
}
