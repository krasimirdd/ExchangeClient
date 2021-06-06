package com.kddimitrov.exchangeClient;

import com.kddimitrov.exchangeClient.SubscribeMessage;
import com.kddimitrov.exchangeClient.util.exception.ExchangeClientConnectionError;
import com.kddimitrov.exchangeClient.util.Lambda;
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

public abstract class AbstractExchangeClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractExchangeClient.class);

    protected final URI url;
    private final AbstractWebSocketHandler websocketHandler;

    protected AbstractExchangeClient(URI url, AbstractWebSocketHandler messageWebsocketHandler) {
        this.url = url;
        this.websocketHandler = messageWebsocketHandler;
    }

    /**
     * Initialize connection with exchange provider.
     * <p></p>
     *
     * @throws ExchangeClientConnectionError in case of connection is unsuccessful within 2 attempts.
     */
    public void connect() throws ExchangeClientConnectionError {
        WebSocketClient webSocketClient = new StandardWebSocketClient();

        subscribe(
                withRetry(() -> Optional.of(webSocketClient.doHandshake(this.websocketHandler,
                                                                        new WebSocketHttpHeaders(),
                                                                        this.url)
                                                    .get()))
        );
    }

    /**
     * Sends subscription message to the exchange provider.
     * <p></p>
     *
     * @param webSocketSession session with exchange provider
     * @throws ExchangeClientConnectionError in case of connection is unsuccessful within 2 attempts.
     */
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

    /**
     * Executes lambda and retry once more in case of {@code InterruptedException} occurs.
     * <p></p>
     *
     * @param method to execute
     * @return Optional of method result or empty.
     */
    private Optional<WebSocketSession> withRetry(Lambda<WebSocketSession> method) {
        try {
            return method.invoke();
        } catch (InterruptedException ignored) {

            LOGGER.error("Exception while accessing websockets, going to retry...");
            try {
                return method.invoke();
            } catch (Exception e) {
                LOGGER.error("Exception while accessing websockets: ", e);
            }
        } catch (Exception ee) {
            LOGGER.error("Exception while accessing websockets: ", ee);
        }

        return Optional.empty();
    }
}
