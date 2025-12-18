package com.example.chat_service;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

// WebSocketConfig.java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 연결할 엔드포인트: /ws
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); // 구독 경로
        registry.setApplicationDestinationPrefixes("/app"); // 발신 경로
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        // 메시지 최대 크기를 50MB로 설정 (기본값은 약 64KB)
        registry.setMessageSizeLimit(50 * 1024 * 1024);

        // 전송 버퍼 크기 제한도 50MB로 설정
        registry.setSendBufferSizeLimit(50 * 1024 * 1024);

        // 전송 시간 제한 늘리기 (큰 파일은 보내는 데 시간이 걸리므로)
        registry.setSendTimeLimit(20 * 10000);
    }
}