package com.kddimitrov.exchangeClient.orderbook;

import org.apache.logging.log4j.util.Strings;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * Class representing an Order Book.
 * <p></p>
 * The implementation provides singleton as there should be only one instance of the order book.
 * This order book should be created once at application startup and populated by exchange clients.
 * <p></p>
 * The order book contains mapping between integer keys, representing {@link com.kddimitrov.exchangeClient.orderbook.attribute.Bids} and
 * {@link com.kddimitrov.exchangeClient.orderbook.attribute.Asks}, and collection of {@link com.kddimitrov.exchangeClient.orderbook.OrderBookEntry}s,
 * which are stored in memory as {@code HashMap} with fully synchronized methods.
 * <p></p>
 *
 * @see <a href="https://www.investopedia.com/terms/o/order-book.asp"> What Is an Order Book? </a>
 * @see com.kddimitrov.exchangeClient.orderbook.OrderBookEntry
 */
public class OrderBook extends HashMap<Integer, List<OrderBookEntry>> {
    private final int BOOK_SIZE;
    private static volatile OrderBook instance = null;
    private static final Comparator<OrderBookEntry> DESCENDING_COMPARATOR =
            Comparator.comparing(OrderBookEntry::getPrice).reversed();

    private OrderBook(int bookSize) {
        super();
        super.put(0, new LinkedList<>());
        super.put(1, new LinkedList<>());

        this.BOOK_SIZE = bookSize;
    }

    /**
     * Gets the reference to the OrderBook object or creates one if it does not exists.
     *
     * @param bookSize the size of bids and asks
     * @return the only OrderBook instance
     */
    public static synchronized OrderBook getInstance(int bookSize) {
        if (instance == null) {
            instance = new OrderBook(bookSize);
        }

        return instance;
    }

    /**
     * Invokes {@link java.util.HashMap#put} synchronously and prints the order book to the {@code System.out}
     */
    @Override
    public synchronized List<OrderBookEntry> put(Integer key, List<OrderBookEntry> value) {
        List<OrderBookEntry> result = super.put(key, value);
        System.out.println(toString());

        return result;
    }

    /**
     * Invokes {@link java.util.HashMap#computeIfAbsent} synchronously.
     */
    @Override
    public synchronized List<OrderBookEntry> computeIfAbsent(Integer key,
                                                             Function<? super Integer, ? extends List<OrderBookEntry>> mappingFunction) {
        return new LinkedList<>(super.computeIfAbsent(key, mappingFunction));
    }

    /**
     * Sorts the order book and returns it string representation synchronously.
     *
     * @return order book string representation.
     */
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

    /**
     * Adds bid entry to bids and print order book synchronously.
     *
     * @param or orderbook bid entry to add
     */
    public synchronized void addBid(OrderBookEntry or) {
        super.get(0).add(or);
        System.out.println(toString());
    }

    /**
     * Adds ask entry to asks and print order book synchronously.
     *
     * @param or orderbook ask entry to add
     */
    public synchronized void addAsk(OrderBookEntry or) {
        super.get(1).add(or);
        System.out.println(toString());
    }

    /**
     * Adds list of bid entries to current bids and print order book synchronously.
     *
     * @param entries orderbook bid entries to add
     */
    public synchronized void updateBids(List<OrderBookEntry> entries) {
        update(entries, super.get(0), true);
    }

    /**
     * Adds list of ask entries to current asks and print order book synchronously.
     *
     * @param entries orderbook ask entries to add
     */
    public synchronized void updateAsks(List<OrderBookEntry> entries) {
        update(entries, super.get(1), false);
    }

    /**
     * Apply replace entries logic to order book and prints it synchronously.
     *
     * @param entries          new entries
     * @param orderBookEntries current entries
     */
    private synchronized void update(List<OrderBookEntry> entries, List<OrderBookEntry> orderBookEntries, boolean removeLast) {
        entries.forEach(e -> {
            orderBookEntries.removeIf(or -> or.getPrice().equals(e.getPrice()));
            orderBookEntries.add(e);
        });
        orderBookEntries.sort(DESCENDING_COMPARATOR);
        trimToSize(orderBookEntries, BOOK_SIZE, removeLast);
        System.out.println(toString());
    }

    /**
     * Recursive function to remove extra entries.
     *
     * @param entries    current entries
     * @param size       desired size
     * @param removeLast
     */
    private synchronized void trimToSize(List<OrderBookEntry> entries, int size, boolean removeLast) {
        if (entries.size() > size) {
            entries.remove(removeLast ? entries.size() - 1 : 0);
        }

        if (entries.size() <= size) {
            return;
        }

        trimToSize(entries, size, removeLast);
    }
}
