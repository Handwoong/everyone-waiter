package com.handwoong.everyonewaiter.config.socket

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.handwoong.everyonewaiter.dto.socket.OrderSocketMessageDto
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

@Component
class WebSocketHandler(
    private val sessionStorage: ConcurrentHashMap<String, MutableList<WebSocketSession>> = ConcurrentHashMap()
) : TextWebSocketHandler() {

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val keyString = "store"
        val requestSessionMessage = message.payload
        val mapper = jacksonObjectMapper()
        val socketMessageDto = mapper.readValue(requestSessionMessage, OrderSocketMessageDto::class.java)

        val id = socketMessageDto.storeId
        val msg = mapper.writeValueAsString(socketMessageDto.message)

        if (!sessionStorage.containsKey(keyString + id)) {
            return
        }

        for (sess in sessionStorage[keyString + id]!!) {
            if (sess.isOpen) {
                sess.sendMessage(TextMessage(msg))
            }
        }
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val url = session.uri.toString()
        val keyString = "store"
        val keyId = url.split("/stores/")[1]

        if (!sessionStorage.containsKey(keyString + keyId)) {
            sessionStorage[keyString + keyId] = CopyOnWriteArrayList()
        }

        sessionStorage[keyString + keyId]?.add(session)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        for (sessionMutableList in sessionStorage.values) {
            if (!sessionMutableList.contains(session)) {
                return
            }
            sessionMutableList.remove(session)
        }
    }

}
