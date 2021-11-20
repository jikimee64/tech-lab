//package com.soap.springevent.baeldung.async;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.event.ApplicationEventMulticaster;
//import org.springframework.context.event.SimpleApplicationEventMulticaster;
//import org.springframework.core.task.SimpleAsyncTaskExecutor;
//
///**
// * 이벤트리스너는 별도의 스레드에서 이벤트를 비동기식으로 처리
// */
//@Configuration
//public class AsynchronousSpringEventsConfig {
//
//    @Bean(name = "applicationEventMulticaster")
//    public ApplicationEventMulticaster simpleApplicationEventMulticaster(){
//        SimpleApplicationEventMulticaster eventMulticaster =
//            new SimpleApplicationEventMulticaster();
//
//        eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
//        return eventMulticaster;
//    }
//
//}
