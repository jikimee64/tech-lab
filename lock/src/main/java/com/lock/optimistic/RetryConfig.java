package com.lock.optimistic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRetry
public class RetryConfig {

    @Bean
    public TicketRetryListener customRetryListener() {
        return new TicketRetryListener();
    }

//    @Bean
//    public RetryTemplate retryTemplate() {
//        RetryTemplate retryTemplate = new RetryTemplate();
//        retryTemplate.setRetryPolicy(new SimpleRetryPolicy());
//        retryTemplate.registerListener(customRetryListener());
//        return retryTemplate;
//    }
}
