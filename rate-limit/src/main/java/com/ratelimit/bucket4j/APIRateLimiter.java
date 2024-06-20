package com.ratelimit.bucket4j;

public interface APIRateLimiter {
    boolean tryConsume(String apiKey);
}
