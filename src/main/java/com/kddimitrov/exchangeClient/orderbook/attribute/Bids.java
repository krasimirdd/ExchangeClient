package com.kddimitrov.exchangeClient.orderbook.attribute;

import java.util.Optional;

public class Bids extends BaseOrderBookEntityAttribute implements OrderBookEntityAttribute<String[]> {

    public Bids(String[][] bs) {
        super(bs);
    }

    public Bids(Optional<String[][]> b) {
        super(b.orElseGet(() -> new String[][]{}));
    }

}
