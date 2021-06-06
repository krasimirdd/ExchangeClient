package com.kddimitrov.exchangeClient.bitfinex.model;

import com.kddimitrov.exchangeClient.orderbook.attribute.OrderBookEntityAttribute;
import org.json.JSONArray;

public class BitfinexPayload implements OrderBookEntityAttribute<JSONArray> {

    private final JSONArray array;

    public BitfinexPayload(JSONArray object) {
        this.array = object;
    }

    @Override
    public int length() {
        return array.length();
    }

    @Override
    public JSONArray getArray(int pos) {
        return pos == -1 ? array : array.optJSONArray(pos);
    }
}
