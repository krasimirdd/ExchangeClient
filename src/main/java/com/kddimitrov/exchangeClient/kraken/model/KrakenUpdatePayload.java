package com.kddimitrov.exchangeClient.kraken.model;

import java.util.Optional;

public class KrakenUpdatePayload {
    String[][] a;
    String[][] b;
    String c;

    public Optional<String[][]> getA() {
        if (a == null) {
            return Optional.empty();
        }
        return Optional.of(a);
    }


    public Optional<String[][]> getB() {
        if (b == null) {
            return Optional.empty();
        }
        return Optional.of(b);
    }

    public String getC() {
        return c;
    }
}
