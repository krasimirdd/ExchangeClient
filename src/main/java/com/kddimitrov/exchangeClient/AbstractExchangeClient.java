package com.kddimitrov.exchangeClient;

import com.kddimitrov.exchangeClient.exception.ExchangeClientConnectionError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.net.URI;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public abstract class AbstractExchangeClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractExchangeClient.class);

    protected final URI url;
    private final AbstractWebSocketHandler websocketHandler;

    protected AbstractExchangeClient(URI url, AbstractWebSocketHandler messageWebsocketHandler) {
        this.url = url;
        this.websocketHandler = messageWebsocketHandler;
    }

    public void connect() throws ExchangeClientConnectionError {
        WebSocketClient webSocketClient = new StandardWebSocketClient();

        subscribe(
                withRetry(() -> webSocketClient.doHandshake(this.websocketHandler,
                                                            new WebSocketHttpHeaders(),
                                                            this.url)
                        .get())
        );
    }

    protected void subscribe(Optional<WebSocketSession> webSocketSession) throws ExchangeClientConnectionError {
        WebSocketSession session = webSocketSession.orElseThrow(ExchangeClientConnectionError::new);

        try {
            TextMessage message = new TextMessage(SubscribeMessage.getSubscribeMessageString(this));
            session.sendMessage(message);
            LOGGER.info("Sent message - " + message.getPayload());
        } catch (Exception e) {
            LOGGER.error("Exception while sending a message", e);
        }
    }

    private Optional<WebSocketSession> withRetry(WebSocketConnectionLambda method) {
        try {
            return Optional.of(method.doConnect());
        } catch (ExecutionException ee) {
            LOGGER.error("Exception while accessing websockets: ", ee);
        } catch (InterruptedException ignored) {

            LOGGER.error("Exception while accessing websockets, going to retry...");
            try {
                return Optional.of(method.doConnect());
            } catch (ExecutionException | InterruptedException e) {
                LOGGER.error("Exception while accessing websockets: ", e);
            }
        }
        return Optional.empty();
    }
}
