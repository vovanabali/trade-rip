package com.goodsoft.library.service.parse_c5_page_service;

import java.io.IOException;

public interface ParseC5PageService {
    /**
     * Load end parse c5 page
     * @return true - success parse all c5 pages
     */
    boolean loadEndParseC5Pages() throws IOException, InterruptedException;

    void loadGems() throws IOException, InterruptedException;

    void checkC5Pages();
}
