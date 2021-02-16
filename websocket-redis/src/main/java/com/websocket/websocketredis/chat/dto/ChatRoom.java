package com.websocket.websocketredis.chat.dto;

import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 * Redis에 저장되는 객체들은 Serialize가능해야 하므로 Serializable을 참조하도록 선언하고
 * serialVersionUID를 세팅
 */
@Setter
@Getter
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    private String roomId;
    private String name;

    public static ChatRoom create(String name){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.name = name;
        return chatRoom;
    }


}
