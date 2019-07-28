package com.goodsoft.library.web;

import com.goodsoft.library.domain.ItemMore;
import com.goodsoft.library.service.itemMore.ItemMoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/item-more")
@RequiredArgsConstructor
public class ItemMoreController {

    private final ItemMoreService itemMoreService;

    @PostMapping("/save")
    public ItemMore getSettings(@RequestBody ItemMore itemMore) {
        return itemMoreService.save(itemMore);
    }

    @GetMapping("/")
    public List<ItemMore> getAll() {
        return itemMoreService.getAll();
    }

    @GetMapping("/delete")
    public Boolean getAll(@RequestParam("id") Long id) {
        return itemMoreService.delete(id);
    }

    @GetMapping("/load_from_c5")
    public List<ItemMore> loadFromC5() throws IOException, InterruptedException {
        return itemMoreService.loadFromC5();
    }

    @GetMapping("/delete_all")
    public void deleteAll() {
        itemMoreService.deleteAll();
    }
}
