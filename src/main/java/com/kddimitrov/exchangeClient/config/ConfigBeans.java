package com.kddimitrov.exchangeClient.config;

import com.kddimitrov.exchangeClient.orderbook.OrderBook;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for defining config beans for the application context.
 */
@Configuration
public class ConfigBeans {

    /**
     * Bean definition for OrderBook object.
     * Gets the only instance for OrderBook object in current JVM,
     * or creates one in case such still does not exists with configured size.
     * <p></p>
     *
     * @param config the application config
     * @return the OrderBook instance
     */
    @Bean
    public OrderBook orderBook(ApplicationConfig config) {
        return OrderBook.getInstance(config.getBookSize());
    }

}
