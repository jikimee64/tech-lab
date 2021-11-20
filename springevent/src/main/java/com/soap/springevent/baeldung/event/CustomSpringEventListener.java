package com.soap.springevent.baeldung.syn;

import com.soap.springevent.baeldung.event.CustomSpringEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomSpringEventListener implements
    ApplicationListener<CustomSpringEvent> {

    @Override
    public void onApplicationEvent(CustomSpringEvent event){
        log.info("Received spring custom event - " + event.getMessage());
    }

}
