package com.example.websocket.chat.domain

enum class MessageType {
    START,
    CONFIRMED,
    NOT_CONFIRMED,
    CHAT,
    JOIN,
    LEAVE,
    RSP
}