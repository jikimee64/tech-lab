package com.ratelimit;

import org.springframework.stereotype.Service;

@Service
public class RateLimitingService {
    @RateLimit(key = "someUniqueKey")
    public String run() {
        return "요청 성공";
    }
}