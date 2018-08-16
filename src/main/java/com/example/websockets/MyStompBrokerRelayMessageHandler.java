package com.example.websockets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.broker.AbstractBrokerMessageHandler;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompBrokerRelayMessageHandler;
import org.springframework.messaging.tcp.reactor.Reactor2TcpClient;
import org.springframework.web.socket.config.annotation.DelegatingWebSocketMessageBrokerConfiguration;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
class MyStompBrokerRelayMessageHandler extends DelegatingWebSocketMessageBrokerConfiguration {

    @Bean
    public AbstractBrokerMessageHandler stompBrokerRelayMessageHandler() {

        StompBrokerRelayMessageHandler handler = (StompBrokerRelayMessageHandler) super.stompBrokerRelayMessageHandler();

        handler.setTcpClient(new Reactor2TcpClient<>(
                new StompTcpFactory(STOMP_URL,
                        STOMP_PORT,
                        true)
        ));

        return handler;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableStompBrokerRelay("/queue", "/topic")
                .setRelayHost(WSS_URL)
                .setRelayPort(WSS_PORT)
                .setSystemLogin(ADMIN_LOGIN)
                .setSystemPasscode(ADMIN_PASSWORD)
                .setClientLogin(USER_LOGIN)
                .setClientPasscode(USER_PASSWORD);

        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    protected void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/gs-guide-websocket").withSockJS();
    }
}
