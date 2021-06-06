package com.kddimitrov.exchangeClient.kraken;

import com.kddimitrov.exchangeClient.orderbook.attribute.Asks;
import com.kddimitrov.exchangeClient.BaseWebSocketHandler;
import com.kddimitrov.exchangeClient.orderbook.attribute.Bids;
import com.kddimitrov.exchangeClient.orderbook.OrderBook;
import com.kddimitrov.exchangeClient.kraken.model.KrakenSnapshotPayload;
import com.kddimitrov.exchangeClient.kraken.model.KrakenUpdatePayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.web.socket.WebSocketSession;

/**
 * Handles communication with Kraken trough web sockets.
 */
public class KrakenHandler extends BaseWebSocketHandler {
    private final Logger logger = LogManager.getLogger(KrakenHandler.class);
    private final OrderBook book;

    public KrakenHandler(OrderBook orderBook) {
        this.book = orderBook;
    }

    @Override
    protected void handleChannelCallback(String messagePayload) throws JSONException, JsonProcessingException {
        JSONArray jsonArray = new JSONArray(new JSONTokener(messagePayload));
        JSONObject payload = jsonArray.getJSONObject(1);

        if (messagePayload.contains("\"c\":")) {
            handleUpdateCallback(jsonArray);
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            KrakenSnapshotPayload snapshotPayload = objectMapper.readValue(payload.toString(), KrakenSnapshotPayload.class);

            OrderBookHandler orderBookHandler = new OrderBookHandler(book);
            orderBookHandler.handleSnapshotData(new Bids(snapshotPayload.getBs()));
            orderBookHandler.handleSnapshotData(new Asks(snapshotPayload.getAs()));
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.info("established connection to Kraken - " + session);
    }

    @Override
    protected void handleUpdateCallback(JSONArray jsonArray) throws JSONException, JsonProcessingException {
        JSONObject jsonObject = jsonArray.getJSONObject(1);
        OrderBookHandler orderBookHandler = new OrderBookHandler(book);

        ObjectMapper objectMapper = new ObjectMapper();
        KrakenUpdatePayload updatePayload = objectMapper.readValue(jsonObject.toString(), KrakenUpdatePayload.class);
        orderBookHandler.handleUpdateData(new Bids(updatePayload.getB()));
        orderBookHandler.handleUpdateData(new Asks(updatePayload.getA()));
    }
}
