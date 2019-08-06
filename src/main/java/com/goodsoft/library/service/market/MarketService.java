package com.goodsoft.library.service.market;


import java.net.Proxy;

public interface MarketService {
    String sendAndGetresponse(String url, Proxy proxy);
}
