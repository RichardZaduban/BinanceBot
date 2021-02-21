package org.acme;

import java.math.BigDecimal;
import java.util.List;

import com.webcerebrium.binance.api.BinanceApi;
import com.webcerebrium.binance.api.BinanceApiException;
import com.webcerebrium.binance.datatype.BinanceOrder;
import com.webcerebrium.binance.datatype.BinanceOrderPlacement;
import com.webcerebrium.binance.datatype.BinanceOrderSide;
import com.webcerebrium.binance.datatype.BinanceOrderType;
import com.webcerebrium.binance.datatype.BinanceSymbol;

public class Account {

    private String apiKey = "xsrZ5aByrcx333aBGQ0jKJF5Nmx0Z6Eh9RnW0ciXdJvxHyshQN69zX6WsKyewrk1";
    private String apiSecret = "Tt3WPMMsYYjsW1m1A2h1s9VcyrFPiQK8xrFvWtQWevQVI8YGQNVwBdoAl5r2eeDs";
    
    //TODO get orders and count
    public List<BinanceOrder> getOrders() throws BinanceApiException {
    
    BinanceApi binance = new BinanceApi(apiKey, apiSecret);
    List<BinanceOrder> openOrders = binance.openOrders(BinanceSymbol.valueOf("ADAUSDT"));
    return openOrders;
    };

    public String makeOrder(String type, double price, double quantity) throws BinanceApiException {

        BinanceOrderSide sell = BinanceOrderSide.SELL;
        BinanceOrderSide buy = BinanceOrderSide.BUY;

        BinanceApi api = new BinanceApi();
        BinanceSymbol symbol = new BinanceSymbol("ADAUSDT");
        
        BinanceOrderPlacement placement = new BinanceOrderPlacement();

        if (type == "buy") {
            placement.setSide(buy);}
        if (type == "sell"){
            placement.setSide(sell);}
        placement.setSymbol(symbol);
        placement.setType(BinanceOrderType.LIMIT);
        placement.setPrice(BigDecimal.valueOf(price));
        placement.setQuantity(BigDecimal.valueOf(quantity));
        //placement.setQuantity(BigDecimal.valueOf(10000)); // buy 10000 of asset for 0.00001 BTC
        BinanceOrder order = api.getOrderById(symbol, api.createOrder(placement).get("orderId").getAsLong());
        System.out.println(order.toString());
         
        return order.toString();
    }


}


