package com.ratelimit;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 각 메서드에 지정할 사용자 정의 애노테이션
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface RateLimit {
    String key();
}
