package com.kddimitrov.exchangeClient;

import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ExecutionException;

public interface WebSocketConnectionLambda {
    WebSocketSession doConnect() throws ExecutionException, InterruptedException;
}
