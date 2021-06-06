package com.kddimitrov.exchangeClient;

@FunctionalInterface
public interface Lambda<T> {

    /**
     * Provides T object
     *
     * @return
     */
    T doConnect() throws Exception;
}
