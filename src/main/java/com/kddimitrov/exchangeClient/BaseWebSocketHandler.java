package com.kddimitrov.exchangeClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

public abstract class BaseWebSocketHandler extends AbstractWebSocketHandler {
    private final Logger logger = LogManager.getLogger(BaseWebSocketHandler.class);

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws JSONException, JsonProcessingException {
        String messagePayload = message.getPayload();
        logger.info("received message - " + messagePayload);
        if (messagePayload.startsWith("{")) {
            handleCommandCallback(messagePayload);
        } else if (messagePayload.startsWith("[")) {
            handleChannelCallback(messagePayload);
        } else {
            logger.error("Got unknown callback: {}", messagePayload);
        }
    }

    protected abstract void handleChannelCallback(String messagePayload) throws JSONException, JsonProcessingException;

    protected void handleCommandCallback(String messagePayload) {
        logger.info("Payload received {}", messagePayload);
    }

    protected abstract void handleUpdateCallback(JSONArray jsonArray) throws JSONException, JsonProcessingException;
}
