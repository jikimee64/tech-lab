package com.websocket.websocketredis.chat.controller;

import com.websocket.websocketredis.chat.dto.ChatMessage;
import com.websocket.websocketredis.chat.repository.ChatRoomRepository;
import com.websocket.websocketredis.chat.service.ChatService;
import com.websocket.websocketredis.chat.service.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatRoomRepository chatRoomRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ChatService chatService;

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징 처리
     * (5) : 입장 시 메시지 처리 로직이 없어진 간소화
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessage message, @Header("token") String token) {
        String nickname = jwtTokenProvider.getUserNameFromJwt(token);
        // 로그인 회원 정보로 대화명 설정
        message.setSender(nickname);
        // 채팅방 인원수 세팅
        message.setUserCount(chatRoomRepository.getUserCount(message.getRoomId()));
        // WebSocket에 발행된 메시지를 redis로 발행(publish)
        // 다른 서버에 공유하기 위해 redis의 Topic으로 발행(ChatService에 존재)
        chatService.sendChatMessage(message);
    }
}
