package com.kddimitrov.exchangeClient.kraken.model;

import com.kddimitrov.exchangeClient.orderbook.OrderBookEntry;

public class KrakenOrderBookEntry implements OrderBookEntry {

    private final Double price;
    private final Double volume;

    public KrakenOrderBookEntry(Double price, Double volume) {
        this.price = price;
        this.volume = volume;
    }

    @Override
    public Double getPrice() {
        return price;
    }

    @Override
    public Double getAmount() {
        return volume;
    }

    @Override
    public String toString() {
        return "K [price=" + price + ", amount=" + getAmount() + "]";
    }

}
