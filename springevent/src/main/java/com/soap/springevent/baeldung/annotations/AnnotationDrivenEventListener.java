package com.soap.springevent.baeldung.annotations;

import com.soap.springevent.baeldung.event.CustomSpringEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AnnotationDrivenEventListener  {

    /**
     * @EnableAsync 설정과 @Async로 간단하게 비동기 가능
     * @param event
     */
    @Async
    @EventListener
    public void onApplicationEvent(CustomSpringEvent event){
        log.info("Received spring custom event - " + event.getMessage());
    }

}