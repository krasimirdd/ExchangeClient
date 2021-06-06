package com.kddimitrov.exchangeClient.orderbook.attribute;

public interface OrderBookEntityAttribute<T> {
    int length();

    T getArray(int pos);
}
