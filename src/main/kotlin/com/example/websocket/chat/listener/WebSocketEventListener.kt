package com.example.websocket.chat.listener

import com.example.websocket.chat.domain.ChatMessage
import com.example.websocket.chat.domain.MessageType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent

@Component
class WebSocketEventListener(
    private val messagingTemplate: SimpMessageSendingOperations?,
) {
    private val logger: Logger = LoggerFactory.getLogger(WebSocketEventListener::class.java)

    @EventListener
    fun handleWebSocketConnectListener(event: SessionConnectedEvent?) {
        logger.info("Received a new web socket connection")
        val chatMessage = ChatMessage(MessageType.RSP, "RSP 시작함",null)
        messagingTemplate!!.convertAndSend("/topic/public", chatMessage)
    }

    @EventListener
    fun handleWebSocketDisconnectListener(event: SessionDisconnectEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        val username = headerAccessor.sessionAttributes!!["username"] as String?
        if (username != null) {
            logger.info("Disconnected")
            logger.info("${headerAccessor}")
            logger.info("${headerAccessor.sessionId}")
            logger.info("${headerAccessor.sessionAttributes}")
            logger.info("${headerAccessor.sessionAttributes!!["username"]}")
            logger.info("Disconnected end")

            val chatMessage = ChatMessage(MessageType.LEAVE, "", username)
            messagingTemplate!!.convertAndSend("/topic/public", chatMessage)
        }
    }
}