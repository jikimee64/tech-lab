package com.ratelimit;

import com.ratelimit.bucket4j.APIRateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * AOP 사용도 가능하나 로직이 숨겨지기 때문에 개인적으론 사용하지 않음
 */
//@Aspect
//@Component
//public class RateLimitingAspect {
//
//    private final APIRateLimiter apiRateLimiter;
//
//    @Autowired
//    public RateLimitingAspect(APIRateLimiter apiRateLimiter) {
//        this.apiRateLimiter = apiRateLimiter;
//    }
//
//    @Around("@annotation(rateLimit)")
//    public Object rateLimitAround(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
//        if( apiRateLimiter.tryConsume(rateLimit.key())) {
//            return joinPoint.proceed();
//        }else {
//            throw new RuntimeException("API rate limit exceeded for key: " + rateLimit.key());
//        }
//    }
//}
