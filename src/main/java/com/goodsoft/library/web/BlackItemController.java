package com.goodsoft.library.web;

import com.goodsoft.library.domain.BlackItem;
import com.goodsoft.library.domain.ItemMore;
import com.goodsoft.library.service.blackItem.BlackItemService;
import com.goodsoft.library.service.itemMore.ItemMoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/black-item")
@RequiredArgsConstructor
public class BlackItemController {

    private final BlackItemService blackItemService;

    @PostMapping("/save")
    public BlackItem getSettings(@RequestBody BlackItem blackItem) {
        return blackItemService.save(blackItem);
    }

    @GetMapping("/")
    public List<BlackItem> getAll() {
        return blackItemService.getAll();
    }

    @GetMapping("/delete")
    public Boolean delete(@RequestParam("id") Long id) {
        return blackItemService.delete(id);
    }

    @GetMapping("exist")
    public Boolean exist(@RequestParam String name) {
        return blackItemService.exitsByName(name);
    }

    @PostMapping("exist_in_list")
    public Boolean existInList(@RequestBody List<String> names) {
        return blackItemService.exitsByList(names);
    }

    @PostMapping("save_all")
    public void saveAll(@RequestBody List<BlackItem> blackItems) {
        blackItemService.saveAll(blackItems);
    }
}
