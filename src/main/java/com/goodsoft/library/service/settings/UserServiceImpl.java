package com.goodsoft.library.service.settings;

import com.goodsoft.library.dao.UserRepository;
import com.goodsoft.library.domain.Settings;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36";
    private final UserRepository userRepository;

    @Override
    public Settings getSettings() {
        List<Settings> settings = userRepository.findAll();
        if (Objects.nonNull(settings) && !settings.isEmpty()) {
            return settings.get(0);
        }
        return new Settings();
    }

    @Override
    public Settings save(Settings user) {
        return userRepository.save(user);
    }

    @Override
    public void getAndSetMarketDiscounts() throws IOException {
        Settings settings = getSettings();
        String url = "https://market.dota2.net/api/GetDiscounts/?key=" + settings.getMarketApi();

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String response = in.lines().collect(Collectors.joining());
        in.close();
        JsonElement jelement = new JsonParser().parse(response);
        JsonObject discounts = jelement.getAsJsonObject().getAsJsonObject("discounts");
        if (Objects.nonNull(discounts)) {
            settings.setDiscountForBuy(Double.valueOf(discounts.get("buy_discount").getAsString().split("%")[0]));
            settings.setDiscountForSell(Double.valueOf(discounts.get("sell_fee").getAsString().split("%")[0]));
            save(settings);
        }
        log.info(response);
    }
}
