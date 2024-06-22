package com.lock.optimistic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 0.1초 간격으로 최대 1000번 재시도하는 어노테이션
 */
@Retention(RUNTIME)
@Target(ElementType.METHOD)
public @interface Retry {
    int maxRetries() default 1000;
    int retryDelay() default 100;
}
