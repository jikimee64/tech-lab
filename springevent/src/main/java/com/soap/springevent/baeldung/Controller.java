package com.soap.springevent.baeldung;

import com.soap.springevent.baeldung.event.CustomSpringEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    private CustomSpringEventPublisher publisher;

    @GetMapping("test")
    public void test(){
        publisher.publishCustomEvent("gd");
    }

    @GetMapping("test2")
    public void test2(){
        publisher.publishCustomEvent2("gd");
    }

}