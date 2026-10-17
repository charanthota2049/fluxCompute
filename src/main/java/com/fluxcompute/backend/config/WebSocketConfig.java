package com.fluxcompute.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.fluxcompute.backend.services.AgentWebSocketHandler;


@Configuration 
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{
    
    private final AgentWebSocketHandler agentWebSocketHandler;
    public WebSocketConfig(AgentWebSocketHandler agentWebSocketHandler){
        this.agentWebSocketHandler = agentWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(agentWebSocketHandler, "/ws/agent")
                .setAllowedOriginPatterns("*");
    }
}