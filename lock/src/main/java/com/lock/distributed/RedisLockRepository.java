package com.lock.distributed;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisLockRepository {
    private final RedisTemplate<String, String> redisTemplate;

    // setIfAbsent()를 활용해서 SETNX를 실행
    public Boolean lock(final long key) {
        return redisTemplate
                .opsForValue()
                .setIfAbsent(String.valueOf(key), "lock", Duration.ofMillis(3_000));
    }

    public Boolean unlock(final long key) {
        return redisTemplate.delete(String.valueOf(key));
    }
}
