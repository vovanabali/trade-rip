package com.goodsoft.library.web;

import com.goodsoft.library.service.parse_c5_page_service.ParseC5PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainController {
    private final ParseC5PageService parseC5PageService;

    @GetMapping("/check-c5-pages")
    public void checkC5Pages() {
        parseC5PageService.checkC5Pages();
    }
}
