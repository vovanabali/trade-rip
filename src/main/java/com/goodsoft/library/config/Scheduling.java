package com.goodsoft.library.config;

import com.goodsoft.library.service.parse_c5_page_service.ParseC5PageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class Scheduling {

    @Autowired
    private ParseC5PageService parseC5PageService;


    /**
     * Load c5 items
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    private void loaC5Gems() {
        log.info("Start load gems c5 items");
        try {
            parseC5PageService.loadGems();
            log.info("Success parse all c5 pages");
        } catch (IOException | InterruptedException e) {
            log.error("Failed load c5 page", e);
            log.error("Parse c5 pages error");
        }
    }
}
