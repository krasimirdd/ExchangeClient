package com.kddimitrov.exchangeClient.bitfinex;

import com.kddimitrov.exchangeClient.BaseWebSocketHandler;
import com.kddimitrov.exchangeClient.orderbook.OrderBook;
import com.kddimitrov.exchangeClient.bitfinex.model.BitfinexPayload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;
import org.springframework.web.socket.WebSocketSession;

public class BitfinexHandler extends BaseWebSocketHandler {
    private final Logger logger = LogManager.getLogger(BitfinexHandler.class);
    private final OrderBook book;

    public BitfinexHandler(OrderBook orderBook) {
        this.book = orderBook;
    }

    @Override
    protected void handleChannelCallback(String messagePayload) throws JSONException {
        final JSONArray jsonArray = new JSONArray(new JSONTokener(messagePayload));
        JSONArray payloadAsArray = jsonArray.optJSONArray(1);

        // update contains [changeId, price, count, amount] or [changeId, action]
        // snapshot contains [changeId, [ [price, count, amount], ...]]
        if (payloadAsArray == null) {
            handleUpdateCallback(jsonArray);
        } else {
            BitfinexPayload snapshotPayload = new BitfinexPayload(payloadAsArray);

            OrderBookHandler orderBookHandler = new OrderBookHandler(book);
            orderBookHandler.handleSnapshotData(snapshotPayload);
        }
    }

    @Override
    protected void handleCommandCallback(String payload) {
        logger.info(payload.equals("hb")
                    ? "Received heath beat callback, skipping action"
                    : "Payload received {}", payload);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.info("established connection to Bitfinex - " + session);
    }

    @Override
    protected void handleUpdateCallback(JSONArray jsonArray) throws JSONException {
        Object payload = jsonArray.get(1);

        // response [changeId, action]
        if (payload instanceof String) {
            handleCommandCallback((String) payload);
        } else {
            // response [changeId, price, count, amount]
            BitfinexPayload updatePayload = new BitfinexPayload(jsonArray);

            OrderBookHandler orderBookHandler = new OrderBookHandler(book);
            orderBookHandler.handleUpdateData(updatePayload);
        }
    }
}
