package com.kddimitrov.exchangeClient.orderbook;

import com.kddimitrov.exchangeClient.orderbook.attribute.OrderBookEntityAttribute;

import java.util.List;

public abstract class AbstractOrderBookHandler {

    protected OrderBook book;

    public AbstractOrderBookHandler(OrderBook book) {
        this.book = book;
    }

    protected <T> void extractEntries(OrderBookEntityAttribute<T> array, List<OrderBookEntry> entries) {
        for (int pos = 0; pos < array.length(); pos++) {
            final T parts = array.getArray(pos);
            OrderBookEntry entry = toOrderBookEntry(parts);
            entries.add(entry);
        }
    }

    protected abstract <T> OrderBookEntry toOrderBookEntry(T parts);
}
