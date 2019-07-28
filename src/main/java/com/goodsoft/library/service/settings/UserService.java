package com.goodsoft.library.service.settings;

import com.goodsoft.library.domain.Settings;

import java.io.IOException;

public interface UserService {
    Settings getSettings();

    Settings save(Settings user);

    void getAndSetMarketDiscounts() throws IOException;
}
