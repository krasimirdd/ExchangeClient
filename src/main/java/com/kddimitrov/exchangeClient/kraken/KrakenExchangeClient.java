package com.kddimitrov.exchangeClient.kraken;

import com.kddimitrov.exchangeClient.AbstractExchangeClient;
import com.kddimitrov.exchangeClient.util.ExchangeClient;
import com.kddimitrov.exchangeClient.config.ApplicationConfig;
import com.kddimitrov.exchangeClient.util.exception.ExchangeClientConnectionError;
import com.kddimitrov.exchangeClient.orderbook.OrderBook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;


/**
 * Kraken exchange client provider.
 */
@ExchangeClient
public class KrakenExchangeClient extends AbstractExchangeClient implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(KrakenExchangeClient.class);

    protected KrakenExchangeClient(OrderBook orderBook,
                                   ApplicationConfig config) {
        super(URI.create(config.getKrakenUrl()), new KrakenHandler(orderBook));
    }

    @Override
    public void run() {
        try {
            super.connect();
        } catch (ExchangeClientConnectionError ecce) {
            LOGGER.error("Fatal error. Cannot connect to Kraken exchange.");
        }
    }
}
