package com.kddimitrov.exchangeClient.bitfinex.model;

import com.kddimitrov.exchangeClient.orderbook.OrderBookEntry;

public class BitfinexOrderBookEntry implements OrderBookEntry {

    private final Double price;
    private final Double amount;

    public BitfinexOrderBookEntry(Double price, Double amount) {
        this.price = price;
        this.amount = amount;
    }

    @Override
    public Double getPrice() {
        return price;
    }

    @Override
    public Double getAmount() {
        return amount;
    }

    public Double getAbsoluteAmount() {
        return Math.abs(amount);
    }

    @Override
    public String toString() {
        return "B [price=" + price + ", amount=" + getAbsoluteAmount() + "]";
    }

}
