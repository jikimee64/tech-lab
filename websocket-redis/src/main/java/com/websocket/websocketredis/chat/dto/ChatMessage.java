package com.websocket.websocketredis.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {

    public ChatMessage(){}

    //메시지 타입 : 입장, 채팅
    public enum MessageType {
        ENTER, QUIT, TALK
    }

    private MessageType type; //메시지 타입
    private String roomId; //방번호
    private String sender; //메시지 보낸사람
    private String message; //메시지
    private long userCount; //채팅방 인원수, 채팅방 내에서 메시지가 전달될때 인원수 갱신시 사용

    @Builder
    public ChatMessage(MessageType type, String roomId, String sender, String messsage, long userCount){
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.message = messsage;
        this.userCount = userCount;
    }




}
