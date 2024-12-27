package com.ms.chat.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Prefix for messages sent to the broker
        config.enableSimpleBroker("/public"); // Clients will subscribe to topics like /topic/public
        config.setApplicationDestinationPrefixes("/app"); // Prefix for messages from clients
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket connection endpoint
        registry.addEndpoint("/ws")  // Frontend will connect to /ws
                .setAllowedOrigins("http://localhost:5173")  // Allow all origins for simplicity
                .withSockJS();  // Fallback for clients that don't support WebSocket
    }
}
