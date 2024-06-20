package com.ratelimit.bucket4j;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Component
public class RedisAPIRateLimiter implements APIRateLimiter{
    // 버킷 최대 용량
    private static final int CAPACITY = 3;
    // 지정된 시간동안 버킷에 추가될 토큰 수
    private static final int REFILL_AMOUNT = 3;
    // 토큰 재충전 빈도
    private static final Duration REFILL_DURATION = Duration.ofSeconds(5);

    // 버킷의 생성 및 관리를 담당
    private final LettuceBasedProxyManager<String> proxyManager;
    // 동일한 API 키에 대한 요청을 처리. 버킷을 재사용하기 위해 버킷을 저장하는 맵
    private final ConcurrentMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    /**
     * RedisAPIRateLimiter 생성자.
     *
     * @param redisClient Redis 클라이언트
     */
    public RedisAPIRateLimiter(RedisClient redisClient) {
        // Redis 연결을 생성
        StatefulRedisConnection<String, byte[]> connection = redisClient.connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));
        // Redis 연결을 이용해 LettuceBasedProxyManager 객체를 생성
        this.proxyManager = LettuceBasedProxyManager.builderFor(connection)
                .withExpirationStrategy(ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofSeconds(100)))
                .build();
    }

    /**
     * API 키에 해당하는 버킷을 가져오거나, 없을 경우 새로 생성하는 메서드.
     *
     * @param apiKey API 키
     * @return 해당 API 키에 대응하는 버킷
     */
    private Bucket getOrCreateBucket(String apiKey) {
        return buckets.computeIfAbsent(apiKey, key -> {
            // 버킷 설정을 생성
            BucketConfiguration configuration = createBucketConfiguration();
            // 버킷 ID와 버킷 설정을 이용해 버킷을 생성하고, 이를 반환
            return proxyManager.builder().build(key, configuration);
        });
    }

    /**
     * API 키에 해당하는 버킷에서 토큰을 소비하려고 시도하는 메서드.
     *
     * @param apiKey API 키
     * @return 토큰 소비 성공 여부
     */
    public boolean tryConsume(String apiKey) {
        // API 키에 해당하는 버킷을 가져옴
        Bucket bucket = getOrCreateBucket(apiKey);
        // 버킷에서 토큰을 소비하려고 시도하고, 그 결과를 반환
        boolean consumed = bucket.tryConsume(1);
        log.info("API Key: {}, Consumed: {}, Time: {}", apiKey, consumed, LocalDateTime.now());
        return consumed;
    }

    /**
     * 버킷 설정을 생성하는 메서드.
     *
     * @return 생성된 버킷 설정
     */
    private BucketConfiguration createBucketConfiguration() {
        return BucketConfiguration.builder()
                // 버킷에 대한 제한(용량과 재충전 속도)을 설정
                .addLimit(Bandwidth.simple(CAPACITY, REFILL_DURATION).withInitialTokens(REFILL_AMOUNT))
                .build();
    }

}