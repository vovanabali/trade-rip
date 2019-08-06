package com.goodsoft.library.service.parse_c5_page_service;

import com.goodsoft.library.domain.Item;
import com.goodsoft.library.domain.ItemMore;
import com.goodsoft.library.domain.Settings;
import com.goodsoft.library.enums.ItemPriceType;
import com.goodsoft.library.enums.ItemSiteType;
import com.goodsoft.library.service.item.ItemService;
import com.goodsoft.library.service.itemMore.ItemMoreService;
import com.goodsoft.library.service.market.MarketService;
import com.goodsoft.library.service.settings.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.goodsoft.library.enums.ItemPriceType.SELL;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParseC5PageServiceImpl implements ParseC5PageService {

    private final ItemService itemService;
    private final UserService userService;
    private final MarketService marketService;
    private Settings settings;

    @Autowired
    private ItemMoreService itemMoreService;

    @Override
    public boolean loadEndParseC5Pages() throws IOException, InterruptedException {
        settings = userService.getSettings();
        if (Objects.nonNull(settings)) {
            double min = 3;
            double max = 6;
            return loadAndParsePages(min, max);
        }
        return false;
    }

    private boolean loadAndParsePages(double min, double max) throws IOException, InterruptedException {
        int pagesCount = getTotalPagesCount(min, max);
        log.info("Count for min" + min + "; max: " + max + " is " + pagesCount);
        if (pagesCount > 0) {
            boolean parsedAllPages = loadC5Page(1, pagesCount, min, max);
            log.info("Parsed all pages for min: " + min + "; max: " + max + " is " + parsedAllPages);
            if (pagesCount < 20) {
                max += 3;
            }
            if (pagesCount < 10) {
                max += 3;
            }
            loadAndParsePages(max, max + 3);
        } else {
            return true;
        }
        return true;
    }

    private int getTotalPagesCount(double min, double max) throws IOException {
        return getTotalPagesCountByUrl("https://www.c5game.com/dota.html?min=" + min + "&max=" + max);
    }

    private int getTotalPagesCountByUrl(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Element element = doc.getElementById("yw1");
        int pagesCount = 0;
        if (Objects.nonNull(element)) {
            Elements last = element.getElementsByClass("last");
            if (!last.isEmpty()) {
                Elements a = last.get(0).getElementsByTag("a");
                if (!a.isEmpty()) {
                    String href = a.get(0).attr("href");
                    pagesCount = Integer.parseInt(href.split("page=")[1]);
                }
            }
        }
        return pagesCount;
    }

    private boolean loadC5Page(int page, int totalPages, double min, double max) throws InterruptedException {
        try {
            if (page > totalPages) {
                return true;
            }
            log.info("Load {} page", page);
            Document doc = Jsoup.connect("https://www.c5game.com/dota.html?min=" + min + "&max=" + max + "&page=" + page).get();
            Elements elements = doc.getElementsByClass("list-item4");
            if (!elements.isEmpty()) {
                Elements sellingItems = elements.get(0).getElementsByTag("li");
                if (!sellingItems.isEmpty()) {
                    sellingItems.forEach(this::parseEndSaveItem);
                } else {
                    return true;
                }
            } else {
                return true;
            }

            loadC5Page(page + 1, totalPages, min, max);
            return true;
        } catch (Exception e) {
            log.error("Load c5 page error min={}, max={}, page={}, total={}", min, max, page, totalPages);
            Thread.sleep(1000);
            return loadC5Page(page, totalPages, min, max);
        }
    }


    private boolean loadC5PageGem(int page, int totalPages, String k) throws InterruptedException {
        try {
            if (page > totalPages) {
                return true;
            }
            log.info("Load gem {} page", page);
            Document doc = Jsoup.connect("https://www.c5game.com/dota.html?only=on&k=" + k + "&page=" + page).get();
            Elements elements = doc.getElementsByClass("list-item4");
            if (!elements.isEmpty()) {
                Elements sellingItems = elements.get(0).getElementsByTag("li");
                if (!sellingItems.isEmpty()) {
                    sellingItems.stream()
                            .map(this::parseItem)
                            .filter(item -> Objects.equals(item.getPriceType(), SELL))
                            .forEach(this::createEndSaveGem);
                } else {
                    return true;
                }
            } else {
                return true;
            }
            loadC5PageGem(page + 1, totalPages, k);
            return true;
        } catch (Exception e) {
            log.error("Load c5 page gems error page={}, total={}", page, totalPages);
            Thread.sleep(1000);
            return loadC5PageGem(page, totalPages, k);
        }
    }

    private void createEndSaveGem(Item item) {
        if (item.getMarketHashName().contains("Prismatic:") || item.getMarketHashName().contains("Ethereal:")) {
            ItemMore itemMore = itemMoreService.getByItemName(new ItemMore(item).getName());
            if (Objects.isNull(itemMore)) {
                itemMore = new ItemMore(item);
            }
            itemMore.setWasBuy(true);
            itemMore.setPrice(item.getPrice());
            itemMoreService.save(itemMore);
        }
    }

    private void parseEndSaveItem(Element element) {
        itemService.saveUnique(parseItem(element));
    }

    private Item parseItem(Element element) {
        Item item = new Item();
        item.setIdentity(element.getElementsByTag("a").get(0)
                .attr("href")
                .split("/dota/")[1]
                .split(".html")[0]
        );
        String[] elementText = element.text().split(" Price:￥ ");
        item.setMarketHashName(elementText[0].replace(" Purchase", ""));
        item.setItemSiteType(ItemSiteType.C5);
        item.setLastDateUpdate(new Date());
        item.setPrice(Double.parseDouble(elementText[1].split(" ")[0]) * settings.getCurrentCurrency());
        if (element.text().contains("on selling")) {
            item.setPriceType(ItemPriceType.BUY);
        } else {
            item.setPriceType(SELL);
        }
        return item;
    }

    @Override
    public void loadGems() throws IOException, InterruptedException {
        itemMoreService.updateWasBuy();
        settings = userService.getSettings();
        if (Objects.nonNull(settings)) {
            log.info("Start load gems");
            int pagesCount = getTotalPagesCountByUrl("https://www.c5game.com/dota.html?only=on&k=prismatic%3A");
            if (pagesCount > 0) {
                loadC5PageGem(1, pagesCount, "prismatic%3A");
            }
            log.info("Start load ethereals");
            pagesCount = getTotalPagesCountByUrl("https://www.c5game.com/dota.html?only=on&k=ethereal");
            if (pagesCount > 0) {
                loadC5PageGem(1, pagesCount, "ethereal");
            }
        }
    }

    @Override
    public void checkC5Pages() {
        itemMoreService.getAlItems().parallelStream().forEach(itemMore -> new Thread(() -> loadEndParseC5Item(itemMore)).start());
    }

    private void loadEndParseC5Item(ItemMore itemMore) {
        if (Objects.isNull(itemMore.getProxy()) || itemMore.getProxy().isEmpty()) {
            return;
        }

        String[] userProxy = itemMore.getProxy().split(":");
        RestTemplate restTemplate = createRestTemplate("PTaq07", "ogA1CZ", userProxy[0], Integer.parseInt(userProxy[1]));
        ResponseEntity<String> response = restTemplate.getForEntity(itemMore.getUrl(), String.class);
        if (Objects.equals(response.getStatusCode(), HttpStatus.OK)) {
           Document document = Jsoup.parse(response.getBody());
           Elements elements = document.getElementsByClass("img csgo-img-bg");
        }
    }

    private RestTemplate createRestTemplate(
            String username,
            String password,
            String proxyUrl,
            int port
    ) {
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(proxyUrl, port),
                new UsernamePasswordCredentials(username, password));

        HttpHost myProxy = new HttpHost(proxyUrl, port);
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();

        clientBuilder.setProxy(myProxy).setDefaultCredentialsProvider(credsProvider).disableCookieManagement();

        HttpClient httpClient = clientBuilder.build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);

        return new RestTemplate(factory);
    }
}
