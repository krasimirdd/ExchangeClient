package com.kddimitrov.exchangeClient.orderbook.attribute;

public abstract class BaseOrderBookEntityAttribute {
    private final String[][] array;

    protected BaseOrderBookEntityAttribute(String[][] array) {
        this.array = array;
    }

    public int length() {
        return array.length;
    }

    public String[] getArray(int pos) {
        return array[pos];
    }
}
