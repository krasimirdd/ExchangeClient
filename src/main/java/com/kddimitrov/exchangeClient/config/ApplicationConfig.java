package com.kddimitrov.exchangeClient.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration class for handling custom configuration properties under {@code "exchange"} root.
 */
@ConfigurationProperties(prefix = "exchange")
public class ApplicationConfig {
    static final int DEFAULT_BOOK_SIZE = 10;

    public String bitfinexUrl;
    public String krakenUrl;
    public int bookSize;

    public String getBitfinexUrl() {
        return bitfinexUrl;
    }

    public void setBitfinexUrl(String bitfinexUrl) {
        this.bitfinexUrl = bitfinexUrl;
    }

    public String getKrakenUrl() {
        return krakenUrl;
    }

    public void setKrakenUrl(String krakenUrl) {
        this.krakenUrl = krakenUrl;
    }

    public int getBookSize() {
        return bookSize > 0 ? bookSize : DEFAULT_BOOK_SIZE;
    }

    public void setBookSize(int bookSize) {
        this.bookSize = bookSize;
    }
}
