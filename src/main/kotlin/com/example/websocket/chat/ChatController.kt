package com.example.websocket.chat

import com.example.websocket.chat.domain.ChatMessage
import com.example.websocket.chat.domain.MessageType
import com.example.websocket.chat.listener.WebSocketEventListener
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.function.RequestPredicates.POST


@RestController
class ChatController {
    private val logger: Logger = LoggerFactory.getLogger(WebSocketEventListener::class.java)

    private var template: SimpMessagingTemplate? = null

    @Autowired
    fun ChatController(template: SimpMessagingTemplate?) {
        this.template = template
    }

    @GetMapping("/start")
    fun greet() {
        logger.info("test")
        template!!.convertAndSend(
            "/topic/public",
            ChatMessage(
                type = MessageType.START,
                content = "가위 바위 보를 시작합니다. '네'라고 응답해 주세요.",
                sender = null
            )
        )
    }

//    @MessageMapping("/chat.addUser")
//    @SendTo("/topic/public")
//    fun addUser(
//        @Payload chatMessage: ChatMessage,
//        headerAccessor: SimpMessageHeaderAccessor,
//    ): ChatMessage? {
//        headerAccessor.sessionAttributes!!["username"] = chatMessage.sender
//        var content = "가위 바위 보를 시작합니다."
//        return chatMessage.copy(content = content, type = MessageType.JOIN)
//    }

//    @MessageMapping("/chat.gameStartConfirm")
//    @SendTo("/topic/public")
//    fun addUser(
//        @Payload chatMessage: ChatMessage,
//        headerAccessor: SimpMessageHeaderAccessor,
//    ): ChatMessage? {
//        headerAccessor.sessionAttributes!!["username"] = chatMessage.sender
//        return if (chatMessage.type == MessageType.CONFIRMED && chatMessage.content == "네") {
//            chatMessage.copy(content = "가위 바위 보를 시작을 승인합니다", type = MessageType.CONFIRMED)
//        } else {
//            chatMessage.copy(
//                content = "가위 바위 보를 시작을 보류합니다",
//                type = MessageType.NOT_CONFIRMED
//            )
//        }
//    }

    @MessageMapping("/chat.rsp")
    @SendTo("/topic/public")
    fun rsp(
        @Payload chatMessage: ChatMessage,
        headerAccessor: SimpMessageHeaderAccessor,
    ): ChatMessage? {
        headerAccessor.sessionAttributes!!["username"] = chatMessage.sender
        //headerAccessor.sessionAttributes?.put("username", chatMessage.sender)
        //0=R, 1=S, 2=P
        // 컴퓨터의 선택을 0, 1, 2 중에서 무작위로 생성
        val sRsp = (Math.random() % 3).toInt()
        val cRsp = chatMessage.content!!.toInt()

        // 사용자와 컴퓨터의 선택에 따른 결과 계산
        val content = when {
            cRsp == sRsp -> "컴퓨터:" + sRsp.toString() + " 무승부"
            (cRsp == 0 && sRsp == 2) ||
                    (cRsp == 1 && sRsp == 0) ||
                    (cRsp == 2 && sRsp == 1) -> "컴퓨터:" + sRsp.toString() + " 사용자 승리"

            else -> "컴퓨터:" + sRsp.toString() + " 컴퓨터 승리"
        }

        return chatMessage.copy(content = content, type = MessageType.RSP)
    }
}