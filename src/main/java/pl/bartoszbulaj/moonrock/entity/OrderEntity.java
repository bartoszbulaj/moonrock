package pl.bartoszbulaj.moonrock.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class OrderEntity {

    @Id
    private String orderID;
    private String symbol;
    private String side;
    private String simpleOrderQty;
    private String orderQty;
    private String price;
    private String displayQty;
            //"stopPx": 0,
            //"pegOffsetValue": 0,
            //"pegPriceType": "string",
    private String currency;
            //"settlCurrency": "string",
    private String ordType;
            //"timeInForce": "string",
            //"execInst": "string",
            //"contingencyType": "string",
            //"exDestination": "string",
            //"ordStatus": "string",
            //"triggered": "string",
            //"workingIndicator": true,
            //"ordRejReason": "string",
            //"simpleLeavesQty": 0,
            //"leavesQty": 0,
            //"simpleCumQty": 0,
            //"cumQty": 0,
            //"avgPx": 0,
            //"multiLegReportingType": "string",
            //"text": "string",
    private String transactTime; // "2019-06-24T06:11:25.680Z"
    private String timestamp;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getSimpleOrderQty() {
        return simpleOrderQty;
    }

    public void setSimpleOrderQty(String simpleOrderQty) {
        this.simpleOrderQty = simpleOrderQty;
    }

    public String getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(String orderQty) {
        this.orderQty = orderQty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDisplayQty() {
        return displayQty;
    }

    public void setDisplayQty(String displayQty) {
        this.displayQty = displayQty;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOrdType() {
        return ordType;
    }

    public void setOrdType(String ordType) {
        this.ordType = ordType;
    }

    public String getTransactTime() {
        return transactTime;
    }

    public void setTransactTime(String transactTime) {
        this.transactTime = transactTime;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
