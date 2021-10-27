package org.acme;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import com.webcerebrium.binance.api.BinanceApiException;

import org.apache.commons.lang3.StringUtils;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

public class DataClient extends WebSocketClient {

    private ArrayList<Double> datas = new ArrayList<>();
    private boolean alert = true;
    private double price_alert;
    private String from = "";
    private String to = "";
    private String header = "New Binance orders";
    private double midPrice;
    //private int orderCounter = 0;
    private boolean firstOrder = true;
    private double lastBuy;
    private double buy;
    private double sell;
    private double orderPrice = 12;
    private double quantity;

    HashMap<String, Double> firstOrders = new HashMap<String, Double>();
    HashMap<String, Double> orders = new HashMap<String, Double>();

    public DataClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
    };

    public DataClient(URI serverUri) {
        super(serverUri);
    };

    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("Connection opened");
    };

    @Override
    public void onMessage(String message) {
        String raw = StringUtils.substringBetween(message, "c", ",");
        String semi_clean = raw.replaceAll("\"", "");
        double clean = Double.parseDouble(semi_clean.replaceAll(":", ""));
        Account account = new Account();
        System.out.println(clean);
        quantity = calculateQuantity(orderPrice, clean);

        // TODO When an how call setFirstOrders when orderCount = 0
        // TODO How often and when to check order count

        if (firstOrder == true) {
            datas.add(clean);
        }
        if (datas.size() == 30 & firstOrder == true) {
            midPrice = calculateMidPrice();
            System.out.println("Mid price: " + midPrice);
            datas.clear();
            setFirstOrders(midPrice, 0.002);
            double firstBuy = firstOrders.get("buy");
            double firstSell = firstOrders.get("sell");
            System.out.println("First buy at: " + firstBuy);
            System.out.println("First sell at: " + firstSell);
            try {
                account.makeBuyOrder(firstBuy, quantity);
                account.makeSellOrder(firstSell, quantity);
                System.out.println("First orders was created");
            } catch (BinanceApiException e) {
                e.printStackTrace();
            }

            String text = quantity + " of ADA was bought at the price of " + firstBuy + "\n" + "this qantity will be sold at price of " + firstSell;
            Mailer mail = new Mailer(from,to,header,text);
            mail.sendMessage();

            lastBuy = firstBuy;
            firstOrder = false;
            firstOrders.clear();
        }
        if (firstOrder == false & (lastBuy <= clean * 0.998)) {

            setOrders(lastBuy, 0.04);
            buy = orders.get("buy");
            sell = orders.get("sell");
            
            try {
                account.makeBuyOrder(buy, quantity);
                account.makeSellOrder(sell, quantity);
            } catch (BinanceApiException e) {
                e.printStackTrace();
            }

            String text = quantity + " of ADA was bought at the price of " + buy + "\n" + "this qantity will be sold at price of " + sell;
            Mailer mail = new Mailer(from,to,header,text);
            mail.sendMessage();
            lastBuy = buy;

            orders.clear();
            
        }

        /*if (price_alert >= clean && alert == true) {
            String text = "Price droped below " + price_alert + ".\n" + "Current price is " + clean;
            Mailer mail = new Mailer(from,to,header,text);
            mail.sendMessage();
            alert = false;
        }*/
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
    // The codecodes are documented in class org.java_websocket.framing.CloseFrame
    System.out.println(
        "Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: "
            + reason);
    }

    @Override
    public void onError(Exception ex) {
    ex.printStackTrace();
    // if the error is fatal then onClose will be called additionally
    }

    public double calculateMidPrice() {
        double sum = 0;
        double mid = 0;
        for(Double d : datas)
            sum += d;
        //System.out.println("sum: "+ sum);
        mid = sum / datas.size();
        //System.out.println("average: " + mid);
        return mid;
        }

    public HashMap<String, Double> setFirstOrders(double midPrice, double diff){

        double firstBuyOrder = midPrice * (1 - diff);
        double firstSellOrder = midPrice * (1 + diff);
        firstOrders.put("buy", firstBuyOrder);
        firstOrders.put("sell", firstSellOrder);
        return firstOrders;
    }

    public HashMap<String, Double> setOrders(double previousBuyPrice, double buySellDiff){

        double buyOrder = previousBuyPrice * (1 - 0.002);
        double sellOrder = buyOrder * (1 + buySellDiff);
        orders.put("buy", buyOrder);
        orders.put("sell", sellOrder);
        return orders;
    }

    public double calculateQuantity(double orderAmmount, double currentPrice) {
        double quantity = orderAmmount / currentPrice;
        return quantity;
    }

    public ArrayList<Double> getDatas() {
        return this.datas;
    }

    public void setDatas(ArrayList<Double> datas) {
        this.datas = datas;
    }

    public boolean isAlert() {
        return this.alert;
    }

    public boolean getAlert() {
        return this.alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public double getPrice_alert() {
        return this.price_alert;
    }

    public void setPrice_alert(double price_alert) {
        this.price_alert = price_alert;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return this.to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getHeader() {
        return this.header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public double getMidPrice() {
        return this.midPrice;
    }

    public void setMidPrice(double midPrice) {
        this.midPrice = midPrice;
    }
    }
    
