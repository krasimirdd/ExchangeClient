package com.kddimitrov.exchangeClient;

import com.kddimitrov.exchangeClient.bitfinex.BitfinexExchangeClient;
import com.kddimitrov.exchangeClient.kraken.KrakenExchangeClient;

public class SubscribeMessage {
    private String event;
    private String chanel;
    private String symbol;
    private String prec;

    public SubscribeMessage(String event, String chanel, String symbol, String prec) {
        this.event = event;
        this.chanel = chanel;
        this.symbol = symbol;
        this.prec = prec;
    }

    private String toBitfinexString() {
        return String.format("{\n" +
                                     "  \"event\": \"%s\",\n" +
                                     "  \"channel\": \"%s\",\n" +
                                     "  \"pair\": \"%s\",\n" +
                                     "  \"prec\": \"%s\"\n" +
                                     "}",
                             event, chanel, symbol, prec);
    }

    private String toKrakenString() {
        return String.format("{\n" +
                                     "  \"event\": \"%s\",\n" +
                                     "  \"pair\": [\n" +
                                     "    \"%s\"\n" +
                                     "  ],\n" +
                                     "  \"subscription\": {\n" +
                                     "    \"name\": \"%s\"\n" +
                                     "  }\n" +
                                     "}",
                             event, symbol, chanel);
    }

    public static String getSubscribeMessageString(AbstractExchangeClient instance) {
        Builder builder = new Builder();
        builder.setEvent("subscribe").setChanel("book");
        if (instance instanceof BitfinexExchangeClient) {
            return builder
                    .setSymbol("tBTCUSD")
                    .setPrec("P1")
                    .build()
                    .toBitfinexString();
        } else if (instance instanceof KrakenExchangeClient) {
           return builder
                    .setSymbol("BTC/USD")
                    .build()
                    .toKrakenString();
        }

        throw new IllegalStateException("Unexpected exchange server.");
    }

    private static class Builder {
        private String event;
        private String chanel;
        private String symbol;
        private String prec;

        public Builder setEvent(String event) {
            this.event = event;
            return this;
        }

        public Builder setChanel(String chanel) {
            this.chanel = chanel;
            return this;
        }

        public Builder setSymbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder setPrec(String prec) {
            this.prec = prec;
            return this;
        }

        public SubscribeMessage build() {
            return new SubscribeMessage(this.event, this.chanel, this.symbol, this.prec);
        }
    }
}
