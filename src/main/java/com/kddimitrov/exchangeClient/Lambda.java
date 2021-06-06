package com.kddimitrov.exchangeClient;

import java.util.Optional;

@FunctionalInterface
public interface Lambda<T> {

    /**
     * Provides T object
     *
     * @return
     */
    Optional<T> invoke() throws Exception;
}
