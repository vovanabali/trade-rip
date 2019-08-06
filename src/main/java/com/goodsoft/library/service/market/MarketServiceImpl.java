package com.goodsoft.library.service.market;

import com.goodsoft.library.domain.Settings;
import com.goodsoft.library.service.settings.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketServiceImpl implements MarketService {

    private final UserService userService;


    @Scheduled(fixedDelay = 60000)
    private void updateSettings() {
        Settings settings = userService.getSettings();
        if (Objects.nonNull(settings) && Objects.nonNull(settings.getMarketApi()) && !settings.getMarketApi().trim().isEmpty()) {
            String ping = "https://market.dota2.net/api/PingPong/?key=" + settings.getMarketApi();
            sendAndGetresponse(ping, null);
        }
    }

    @Override
    public String sendAndGetresponse(String url, Proxy proxy) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con;
            if (Objects.isNull(proxy)) {
                con = (HttpURLConnection) obj.openConnection();
            } else {
                final String authUser = "PTaq07";
                final String authPassword = "ogA1CZ";

//                System.setProperty("http.proxyHost", "hostAddress");
//                System.setProperty("http.proxyPort", "portNumber");
                System.setProperty("https.proxyUser", authUser);
                System.setProperty("https.proxyPassword", authPassword);

                Authenticator.setDefault(
                        new Authenticator() {
                            public PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(authUser, authPassword.toCharArray());
                            }
                        }
                );
                con = (HttpURLConnection) obj.openConnection(proxy);
            }
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            return in.lines().collect(Collectors.joining());
        } catch (Exception e) {
            log.error("Failed execute get query by " + url, e);
            return "";
        }
    }
}
