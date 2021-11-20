package com.soap.springevent.baeldung.event;

import org.springframework.context.ApplicationEvent;

/**
 * 신청 이벤트 생성 문자열 메시지를 보유
 */
public class CustomSpringEvent extends ApplicationEvent {

    private String message;

    public CustomSpringEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}