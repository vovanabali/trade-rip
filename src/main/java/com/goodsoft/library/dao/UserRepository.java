package com.goodsoft.library.dao;

import com.goodsoft.library.domain.Settings;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends PagingAndSortingRepository<Settings, Long> {
    List<Settings> findAll();
}
