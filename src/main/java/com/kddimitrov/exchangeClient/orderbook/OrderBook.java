package com.kddimitrov.exchangeClient.orderbook;


import org.apache.logging.log4j.util.Strings;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class OrderBook extends HashMap<Integer, List<OrderBookEntry>> {
    private final int BOOK_SIZE;
    private static OrderBook instance;
    public static final Comparator<OrderBookEntry> DESCENDING_COMPARATOR =
            Comparator.comparing(OrderBookEntry::getPrice).reversed();

    private OrderBook(int bookSize) {
        super();
        super.put(0, new LinkedList<>());
        super.put(1, new LinkedList<>());

        this.BOOK_SIZE = bookSize;
    }

    public static OrderBook getInstance(int bookSize) {
        if (instance == null) {
            instance = new OrderBook(bookSize);
        }

        return instance;
    }

    @Override
    public synchronized List<OrderBookEntry> put(Integer key, List<OrderBookEntry> value) {
        List<OrderBookEntry> result = super.put(key, value);
        System.out.println(toString());

        return result;
    }

    @Override
    public synchronized List<OrderBookEntry> computeIfAbsent(Integer key, Function<? super Integer, ? extends List<OrderBookEntry>> mappingFunction) {
        return new LinkedList<>(super.computeIfAbsent(key, mappingFunction));
    }

    @Override
    public synchronized String toString() {
        List<OrderBookEntry> bids = super.get(0);
        List<OrderBookEntry> asks = super.get(1);
        bids.sort(DESCENDING_COMPARATOR);
        asks.sort(DESCENDING_COMPARATOR);

        return String.format("OrderBook: [\n" +
                                     "Asks: \n%s\n" +
                                     "Best Bid: %s\n" +
                                     "Best Ask: %s\n" +
                                     "Bids: \n%s\n" +
                                     "]\n" +
                                     "%s",
                             Strings.join(asks, '\n'),
                             bids.isEmpty() ? "''" : bids.get(0),
                             asks.isEmpty() ? "''" : asks.get(asks.size() - 1),
                             Strings.join(bids, '\n'),
                             LocalDateTime.now()
        );
    }

    public synchronized void addBid(OrderBookEntry or) {
        super.get(0).add(or);
        System.out.println(toString());
    }

    public synchronized void addAsk(OrderBookEntry or) {
        super.get(1).add(or);
        System.out.println(toString());
    }

    public synchronized void updateBids(List<OrderBookEntry> entries) {
        update(entries, super.get(0));
    }

    public synchronized void updateAsks(List<OrderBookEntry> entries) {
        update(entries, super.get(1));
    }

    private synchronized void update(List<OrderBookEntry> entries, List<OrderBookEntry> orderBookEntries) {
        entries.forEach(e -> {
            orderBookEntries.removeIf(or -> or.getPrice().equals(e.getPrice()));
            orderBookEntries.add(e);
        });
        orderBookEntries.sort(DESCENDING_COMPARATOR);
        trimToSize(orderBookEntries, BOOK_SIZE);
        System.out.println(toString());
    }

    private synchronized void trimToSize(List<OrderBookEntry> bids, int size) {
        if (bids.size() > size) {
            bids.remove(bids.size() - 1);
        }

        if (bids.size() <= size) {
            return;
        }

        trimToSize(bids, size);
    }
}
