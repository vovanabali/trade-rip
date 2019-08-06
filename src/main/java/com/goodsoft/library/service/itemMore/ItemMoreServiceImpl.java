package com.goodsoft.library.service.itemMore;

import com.goodsoft.library.dao.ItemMoreRepository;
import com.goodsoft.library.domain.ItemMore;
import com.goodsoft.library.service.parse_c5_page_service.ParseC5PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemMoreServiceImpl implements ItemMoreService {

    private final ItemMoreRepository itemMoreRepository;

    @Autowired
    private ParseC5PageService parseC5PageService;

    @Override
    public ItemMore save(ItemMore itemMore) {
        return itemMoreRepository.save(itemMore);
    }

    @Override
    public List<ItemMore> getAll() {
        return itemMoreRepository.findAll();
    }

    @Override
    public boolean delete(Long id) {
        itemMoreRepository.deleteById(id);
        return Boolean.TRUE;
    }

    @Override
    public ItemMore getByItemName(String itemName) {
        return itemMoreRepository.findFirstByName(itemName);
    }

    @Override
    public List<ItemMore> getAllNoItems() {
        return itemMoreRepository.findAllByIsItem(Boolean.FALSE);
    }

    @Override
    public List<ItemMore> getAlItems() {
        return itemMoreRepository.findAllByIsItem(Boolean.TRUE);
    }

    @Override
    public List<ItemMore> loadFromC5() throws IOException, InterruptedException {
        parseC5PageService.loadGems();
        return getAll();
    }

    @Override
    @Transactional
    public void updateWasBuy() {
        itemMoreRepository.findAllByIsItem(true)
                .stream()
                .filter(ItemMore::isCreatedSystem)
                .peek(itemMore -> itemMore.setWasBuy(false))
                .forEach(itemMoreRepository::save);
    }

    @Override
    public void deleteAll() {
        itemMoreRepository.deleteAll();
    }
}
