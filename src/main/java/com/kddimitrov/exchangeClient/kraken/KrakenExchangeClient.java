package com.kddimitrov.exchangeClient.kraken;

import com.kddimitrov.exchangeClient.AbstractExchangeClient;
import com.kddimitrov.exchangeClient.config.ApplicationConfig;
import com.kddimitrov.exchangeClient.exception.ExchangeClientConnectionError;
import com.kddimitrov.exchangeClient.orderbook.OrderBook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URI;

@Service
public class KrakenExchangeClient extends AbstractExchangeClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(KrakenExchangeClient.class);

    protected KrakenExchangeClient(OrderBook orderBook,
                                   ApplicationConfig config) {
        super(URI.create(config.getKrakenUrl()), new KrakenHandler(orderBook));
    }

    @PostConstruct
    @Override
    public void connect() {
        try {
            super.connect();
        } catch (ExchangeClientConnectionError ecce) {
            LOGGER.error("Fatal error. Cannot connect to Kraken exchange.");
        }
    }
}
