package com.kddimitrov.exchangeClient.util;

@FunctionalInterface
public interface WithLock {
    <T> T invoke();
}
