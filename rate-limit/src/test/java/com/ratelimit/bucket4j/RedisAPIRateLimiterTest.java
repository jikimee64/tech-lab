package com.ratelimit.bucket4j;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class RedisAPIRateLimiterTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier("redisAPIRateLimiter")
    private APIRateLimiter apiRateLimiter;

    @Test
    void 토큰_소비_성공시_true_반환() {
        assertThat(apiRateLimiter.tryConsume("test")).isTrue();
        assertThat(apiRateLimiter.tryConsume("test")).isTrue();
        assertThat(apiRateLimiter.tryConsume("test")).isTrue();

        assertThat(apiRateLimiter.tryConsume("test3")).isTrue();
        assertThat(apiRateLimiter.tryConsume("test3")).isTrue();
        assertThat(apiRateLimiter.tryConsume("test3")).isTrue();
    }

    @Test
    void 토큰소비_요청이_4개_이상할_경우_false_반환() {
        assertThat(apiRateLimiter.tryConsume("test2")).isTrue();
        assertThat(apiRateLimiter.tryConsume("test2")).isTrue();
        assertThat(apiRateLimiter.tryConsume("test2")).isTrue();
        assertThat(apiRateLimiter.tryConsume("test2")).isFalse();
    }

}
