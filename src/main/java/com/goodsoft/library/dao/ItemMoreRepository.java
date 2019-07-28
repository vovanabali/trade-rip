package com.goodsoft.library.dao;

import com.goodsoft.library.domain.ItemMore;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ItemMoreRepository extends PagingAndSortingRepository<ItemMore, Long> {
    List<ItemMore> findAll();

    ItemMore findFirstByName(String name);

    List<ItemMore> findAllByIsItem(boolean is);
}
