package com.kddimitrov.exchangeClient.bitfinex;

import com.kddimitrov.exchangeClient.AbstractExchangeClient;
import com.kddimitrov.exchangeClient.config.ApplicationConfig;
import com.kddimitrov.exchangeClient.exception.ExchangeClientConnectionError;
import com.kddimitrov.exchangeClient.orderbook.OrderBook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URI;

@Service
public class BitfinexExchangeClient extends AbstractExchangeClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(BitfinexExchangeClient.class);

    @Autowired
    protected BitfinexExchangeClient(OrderBook orderBook,
                                     ApplicationConfig config) {
        super(URI.create(config.getBitfinexUrl()), new BitfinexHandler(orderBook));
    }

    @PostConstruct
    @Override
    public void connect() {
        try {
            super.connect();
        } catch (ExchangeClientConnectionError ecce) {
            LOGGER.error("Fatal error. Cannot connect to Biftinex exchange.");
        }
    }
}
