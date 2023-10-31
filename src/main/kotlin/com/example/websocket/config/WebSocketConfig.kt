package com.example.websocket.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer


@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws").withSockJS()
        // [1] "/ws" WebSocket 또는 SockJS Client가 웹소켓 핸드셰이크 커넥션을 생성할 경로이다.
    }

    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        // [2] "/app" 경로로 시작하는 STOMP 메세지의 "destination" 헤더는 @Controller 객체의 @MessageMapping 메서드로 라우팅된다.
        config.setApplicationDestinationPrefixes("/app")
        // [2] 내장된 메세지 브로커를 사용해 Client 에게 Subscriptions, Broadcasting 기능을 제공 한다.
        // 또한 /topic, /queue 로 시작 하는 "destination" 헤더를 가진 메세지를 브로커로 라우팅한다.
        config.enableSimpleBroker("/topic")
    }
}