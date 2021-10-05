package org.acme;

import java.net.URI;
import java.net.URISyntaxException;

import com.webcerebrium.binance.api.BinanceApiException;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Main {
    public static void main(String[] args) throws URISyntaxException, BinanceApiException {
    DataClient c = new DataClient(new URI(
        "wss://stream.binance.com:9443/ws/adaeur@kline_1m"));
    c.connect();
    //Account account = new Account();
      //  account.getOrders();
    
  }
}
