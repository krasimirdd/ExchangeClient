package com.kddimitrov.exchangeClient.config;

import com.kddimitrov.exchangeClient.orderbook.OrderBook;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigBeans {

    @Bean
    public OrderBook orderBook(ApplicationConfig config) {
        return OrderBook.getInstance(config.getBookSize());
    }

}
