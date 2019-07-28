package com.goodsoft.library.service.blackItem;

import com.goodsoft.library.dao.BlackItemRepository;
import com.goodsoft.library.domain.BlackItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlackItemServiceImpl implements BlackItemService {

    private final BlackItemRepository blackItemRepository;

    @Override
    public BlackItem save(BlackItem blackItem) {
        return blackItemRepository.save(blackItem);
    }

    @Override
    public List<BlackItem> getAll() {
        return blackItemRepository.findAll();
    }

    @Override
    public boolean delete(Long id) {
        blackItemRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean exitsByName(String name) {
        return blackItemRepository.existsByName(name);
    }

    @Override
    @Transactional
    public boolean exitsByList(List<String> names) {
        return names.stream().map(blackItemRepository::existsByName).collect(Collectors.toList()).contains(true);
    }

    @Override
    public void saveAll(List<BlackItem> blackItems) {
        blackItemRepository.saveAll(blackItems);
    }
}
