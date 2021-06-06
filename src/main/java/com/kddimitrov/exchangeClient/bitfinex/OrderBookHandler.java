package com.kddimitrov.exchangeClient.bitfinex;

import com.kddimitrov.exchangeClient.orderbook.AbstractOrderBookHandler;
import com.kddimitrov.exchangeClient.orderbook.OrderBook;
import com.kddimitrov.exchangeClient.orderbook.attribute.OrderBookEntityAttribute;
import com.kddimitrov.exchangeClient.orderbook.OrderBookEntry;
import com.kddimitrov.exchangeClient.bitfinex.model.BitfinexOrderBookEntry;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class OrderBookHandler extends AbstractOrderBookHandler {

    public OrderBookHandler(OrderBook book) {
        super(book);
    }

    public void handleSnapshotData(final OrderBookEntityAttribute<JSONArray> array) {
        final List<OrderBookEntry> entries = new ArrayList<>();
        extractEntries(array, entries);

        entries.forEach(or -> {
            if (or.getAmount() > 0) {                   // positive values mean bid
                book.addBid(or);
            } else {                                    // negative values mean ask.
                book.addAsk(or);
            }
        });
    }

    public void handleUpdateData(final OrderBookEntityAttribute<JSONArray> array) {
        OrderBookEntry entry = toUpdateOrderBookEntry(array.getArray(-1));
        if (entry.getAmount() > 0) {
            book.updateBids(Collections.singletonList(entry));
        }
        if (entry.getAmount() < 0) {
            book.updateAsks(Collections.singletonList(entry));
        }
    }

    @Override
    protected <T> OrderBookEntry toOrderBookEntry(T arr) {
        JSONArray parts = (JSONArray) arr;
        final Double price = parts.optDouble(0);
        final Double amount = parts.optDouble(2);
        return new BitfinexOrderBookEntry(price, amount);
    }

    protected <T> OrderBookEntry toUpdateOrderBookEntry(T arr) {
        JSONArray parts = (JSONArray) arr;
        final Double price = parts.optDouble(1);
        final Double amount = parts.optDouble(3);
        return new BitfinexOrderBookEntry(price, amount);
    }
}
