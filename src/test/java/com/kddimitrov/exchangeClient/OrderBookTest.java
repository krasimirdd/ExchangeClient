package com.kddimitrov.exchangeClient;

import com.kddimitrov.exchangeClient.bitfinex.model.BitfinexOrderBookEntry;
import com.kddimitrov.exchangeClient.config.ApplicationConfig;
import com.kddimitrov.exchangeClient.kraken.model.KrakenOrderBookEntry;
import com.kddimitrov.exchangeClient.orderbook.OrderBook;
import com.kddimitrov.exchangeClient.orderbook.OrderBookEntry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(properties = {"exchange.bookSize=2"})
class OrderBookTest {
    OrderBookEntry bitfinex_ask = new BitfinexOrderBookEntry(1000.0, 1.0);
    OrderBookEntry kraken_ask = new KrakenOrderBookEntry(1000.1, 1.1);
    OrderBookEntry bitfinex_bid = new BitfinexOrderBookEntry(2000.0, 2.0);
    OrderBookEntry kraken_bid = new KrakenOrderBookEntry(2000.2, 2.2);
    List<OrderBookEntry> bids = new ArrayList<OrderBookEntry>() {{
        add(bitfinex_bid);
        add(kraken_bid);
    }};
    List<OrderBookEntry> asks = new ArrayList<OrderBookEntry>() {{
        add(bitfinex_ask);
        add(kraken_ask);
    }};

    @Autowired
    OrderBook orderBook;

    @Autowired
    ApplicationConfig config;

    @BeforeEach
    public void beforeEach() {
        orderBook.updateAsks(asks);
        orderBook.updateBids(bids);
    }

    @AfterEach
    public void afterEach() {
        orderBook.replace(0, new LinkedList<>());
        orderBook.replace(1, new LinkedList<>());
    }

    @Test
    void addEntries() {
        assertTrue(orderBook.toString().contains("Best Bid: K [price=2000.2, amount=2.2]"));
        assertTrue(orderBook.toString().contains("Best Ask: B [price=1000.0, amount=1.0]"));
    }

    @Test
    void updateEntries() {
        OrderBookEntry updateEntry = new KrakenOrderBookEntry(2000.2, 3.2);
        orderBook.addBid(updateEntry);

        assertTrue(orderBook.get(0).contains(updateEntry));
    }

    @Test
    void updateEntries2() {
        // add best bid -> expect to be added and worst removed
        OrderBookEntry updateEntry = new KrakenOrderBookEntry(2000.2, 3.2);
        orderBook.updateBids(Collections.singletonList(updateEntry));

        assertTrue(orderBook.get(0).contains(updateEntry));
        assertTrue(orderBook.toString().contains("Best Bid: K [price=2000.2, amount=3.2]"));

        // add second better bid -> expect to be added and worst removed
        updateEntry = new KrakenOrderBookEntry(2001.2, 1.0);
        orderBook.updateBids(Collections.singletonList(updateEntry));

        assertTrue(orderBook.get(0).contains(updateEntry));
        assertTrue(orderBook.toString().contains("Best Bid: K [price=2001.2, amount=1.0]"));
        assertEquals(config.getBookSize(), orderBook.get(0).size());

        // add new asks which are worst than the current -> expect to not be added
        orderBook.updateAsks(
                Arrays.asList(new BitfinexOrderBookEntry(1002.0, 1.0),
                              new BitfinexOrderBookEntry(1002.1, 1.0))
        );
        assertEquals(2, orderBook.get(1).size());
        assertTrue(orderBook.toString().contains("Asks: \n" +
                                                         "K [price=1000.1, amount=1.1]\n" +
                                                         "B [price=1000.0, amount=1.0]"));

        // add new asks which are better than current -> expect to be added
        orderBook.updateAsks(
                Arrays.asList(new BitfinexOrderBookEntry(999.0, 1.0),
                              new BitfinexOrderBookEntry(999.1, 1.0))
        );
        assertEquals(2, orderBook.get(1).size());
        assertTrue(orderBook.toString().contains("Best Ask: B [price=999.0, amount=1.0]"));
        assertTrue(orderBook.toString().contains("Asks: \n" +
                                                         "B [price=999.1, amount=1.0]\n" +
                                                         "B [price=999.0, amount=1.0]"));
    }
}
