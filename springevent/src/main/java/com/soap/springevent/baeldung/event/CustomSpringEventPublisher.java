package com.soap.springevent.baeldung.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 이벤트 게시가 역할
 */
@Component
@Slf4j
public class CustomSpringEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishCustomEvent(final String message) {
        log.info("Publishing custom event.");
        CustomSpringEvent customSpringEvent = new CustomSpringEvent(this, message);
        applicationEventPublisher.publishEvent(customSpringEvent);
    }

    public void publishCustomEvent2(final String message) {
        log.info("Publishing generic event");
        GenericSpringEvent<String> genericSpringEvent = new GenericStringSpringEvent(message, true);
        applicationEventPublisher.publishEvent(genericSpringEvent);
    }

}