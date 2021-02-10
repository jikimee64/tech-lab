package com.websocket.websocketredis.chat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.websocketredis.chat.dto.ChatMessage;
import com.websocket.websocketredis.chat.dto.ChatRoom;
import com.websocket.websocketredis.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ChatService chatService;
    /**
     * 여러 클라이언트가 발송한 메시지를 받아 처리해줄 Handler
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}", payload);
        //JSON형식 String -> 객체 변환
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        ChatRoom room = chatService.findRoomById(chatMessage.getRoomId());
        room.handlerActions(session, chatMessage, chatService);
        //TextMessage textMessage = new TextMessage("Welcome");
        //session.sendMessage(textMessage);
    }

}
