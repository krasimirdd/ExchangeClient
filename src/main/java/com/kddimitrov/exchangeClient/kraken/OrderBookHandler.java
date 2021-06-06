package com.kddimitrov.exchangeClient.kraken;

import com.kddimitrov.exchangeClient.orderbook.AbstractOrderBookHandler;
import com.kddimitrov.exchangeClient.orderbook.attribute.Asks;
import com.kddimitrov.exchangeClient.orderbook.attribute.Bids;
import com.kddimitrov.exchangeClient.orderbook.OrderBook;
import com.kddimitrov.exchangeClient.orderbook.attribute.OrderBookEntityAttribute;
import com.kddimitrov.exchangeClient.orderbook.OrderBookEntry;
import com.kddimitrov.exchangeClient.kraken.model.KrakenOrderBookEntry;

import java.util.ArrayList;
import java.util.List;

class OrderBookHandler extends AbstractOrderBookHandler {

    public OrderBookHandler(OrderBook book) {
        super(book);
    }

    public void handleSnapshotData(final OrderBookEntityAttribute<String[]> array) {
        final List<OrderBookEntry> entries = new ArrayList<>();
        extractEntries(array, entries);

        if (array instanceof Bids) {
            book.updateBids(entries);
        } else if (array instanceof Asks) {
            book.updateAsks(entries);
        }
    }

    public void handleUpdateData(final OrderBookEntityAttribute<String[]> array) {
        if (array.length() == 0) {
            return;
        }

        final List<OrderBookEntry> entries = new ArrayList<>();
        extractEntries(array, entries);

        if (array instanceof Bids) {
            book.updateBids(entries);
        }
        if (array instanceof Asks) {
            book.updateAsks(entries);
        }
    }

    @Override
    protected <T> OrderBookEntry toOrderBookEntry(T arr) {
        String[] parts = (String[]) arr;

        final Double price = Double.parseDouble(parts[0]);
        final Double volume = Double.parseDouble(parts[1]);
        return new KrakenOrderBookEntry(price, volume);
    }
}
