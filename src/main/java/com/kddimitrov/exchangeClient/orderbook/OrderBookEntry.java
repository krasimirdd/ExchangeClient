package com.kddimitrov.exchangeClient.orderbook;

/**
 * General interface for representing an OrderBook entry.
 * <p></p>
 * @see <a href="https://www.investopedia.com/terms/o/order-book.asp"> What Is an Order Book? </a>
 */
public interface OrderBookEntry {

    Double getPrice();

    Double getAmount();
}
