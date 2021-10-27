package org.acme;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.webcerebrium.binance.api.BinanceApi;
import com.webcerebrium.binance.api.BinanceApiException;
import com.webcerebrium.binance.datatype.BinanceOrder;
import com.webcerebrium.binance.datatype.BinanceOrderPlacement;
import com.webcerebrium.binance.datatype.BinanceOrderSide;
import com.webcerebrium.binance.datatype.BinanceOrderType;
import com.webcerebrium.binance.datatype.BinanceSymbol;

import static java.lang.Math.round;

public class Account {

    private String apiKey = "";
    private String apiSecret = "";
    
    //TODO get orders and count
    public List<BinanceOrder> getOrders() throws BinanceApiException {
    
    BinanceApi binance = new BinanceApi(apiKey, apiSecret);
        return binance.openOrders(BinanceSymbol.valueOf("ADAUSDT"));
    };

    public String makeSellOrder(double price, double quantity) throws BinanceApiException {
        double roundedQuantity = new BigDecimal(quantity).setScale(2, RoundingMode.UP).doubleValue();
        BinanceOrderSide sell = BinanceOrderSide.SELL;

        BinanceApi api = new BinanceApi();
        BinanceSymbol symbol = new BinanceSymbol("ADAUSDT");

        BinanceOrderPlacement placement = new BinanceOrderPlacement();


        placement.setSide(sell);
        placement.setSymbol(symbol);
        placement.setType(BinanceOrderType.LIMIT);
        placement.setPrice(BigDecimal.valueOf(price));
        placement.setQuantity(BigDecimal.valueOf(roundedQuantity));
        //placement.setQuantity(BigDecimal.valueOf(10000)); // buy 10000 of asset for 0.00001 BTC
        BinanceOrder order = api.getOrderById(symbol, api.createOrder(placement).get("orderId").getAsLong());
        System.out.println(order.toString());

        return order.toString();
    }

    public String makeBuyOrder(double price, double quantity) throws BinanceApiException {
        double roundedQuantity = new BigDecimal(quantity).setScale(2, RoundingMode.UP).doubleValue();


        BinanceOrderSide buy = BinanceOrderSide.BUY;

        BinanceApi api = new BinanceApi();
        BinanceSymbol symbol = new BinanceSymbol("ADAUSDT");

        BinanceOrderPlacement placement = new BinanceOrderPlacement();

        placement.setSide(buy);
        placement.setSymbol(symbol);
        placement.setType(BinanceOrderType.MARKET);
        placement.setPrice(BigDecimal.valueOf(price));
        placement.setQuantity(BigDecimal.valueOf(roundedQuantity));


        BinanceOrder order = api.getOrderById(symbol, api.createOrder(placement).get("orderId").getAsLong());

        System.out.println(order.toString());

        return order.toString();
    }


}


