package com.websocket.websocketredis.chat.controller;

import com.websocket.websocketredis.chat.dto.ChatMessage;
import com.websocket.websocketredis.chat.dto.ChatRoom;
import com.websocket.websocketredis.chat.pubsub.RedisPublisher;
import com.websocket.websocketredis.chat.repository.ChatRoomRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChatController {

    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징 처리
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessage message){
        if(ChatMessage.MessageType.ENTER.equals(message.getType())){
            chatRoomRepository.enterChatRoom(message.getRoomId()); //채팅 가능하도록 리스너 연동
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }
        // WebSocket에 발행된 메시지를 redis로 발행(publish)
        // 다른 서버에 공유하기 위해 redis의 Topic으로 발행행
        redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
    }


}
