package com.kddimitrov.exchangeClient.orderbook.attribute;

import java.util.Optional;

public class Asks extends BaseOrderBookEntityAttribute implements OrderBookEntityAttribute<String[]> {

    public Asks(String[][] as) {
        super(as);
    }

    public Asks(Optional<String[][]> a) {
        super(a.orElseGet(() -> new String[][]{}));
    }
}
