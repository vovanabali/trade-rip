package com.goodsoft.library.web;

import com.goodsoft.library.domain.Settings;
import com.goodsoft.library.service.settings.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final UserService userService;

    @GetMapping("/")
    public Settings getSettings() {
        return userService.getSettings();
    }

    @PostMapping("/save")
    public Settings save(@RequestBody Settings user) {
        return userService.save(user);
    }
}
