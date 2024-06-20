package com.ratelimit.bucket4j;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.BucketProxy;
import io.github.bucket4j.distributed.jdbc.BucketTableSettings;
import io.github.bucket4j.distributed.jdbc.SQLProxyConfiguration;
import io.github.bucket4j.mysql.MySQLSelectForUpdateBasedProxyManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Primary
@Component
public class MysqlAPIRateLimiter implements APIRateLimiter {

    // 버킷 최대 용량
    private static final int CAPACITY = 3;
    // 지정된 시간동안 버킷에 추가될 토큰 수
    private static final int REFILL_AMOUNT = 3;
    // 토큰 재충전 빈도
    private static final Duration REFILL_DURATION = Duration.ofSeconds(5);

    // 버킷의 생성 및 관리를 담당
    private final MySQLSelectForUpdateBasedProxyManager<Long> proxyManager;
    // 동일한 API 키에 대한 요청을 처리. 버킷을 재사용하기 위해 버킷을 저장하는 맵
    private final ConcurrentMap<String, BucketProxy> buckets = new ConcurrentHashMap<>();

    public MysqlAPIRateLimiter(DataSource dataSource) {
        // 버킷 테이블 설정을 가져옴
        var tableSettings = BucketTableSettings.getDefault();
        // 커스텀한 스키마, 테이블, 칼럼 지정시 방법
        // BucketTableSettings.customSettings("test.bucket", "id", "state");
        // SQL 프록시 설정을 생성
        var sqlProxyConfiguration = SQLProxyConfiguration.builder()
                .withTableSettings(tableSettings)
                .build(dataSource);
        // SQL 프록시 설정을 이용해 MySQLSelectForUpdateBasedProxyManager 객체를 생성
        proxyManager = new MySQLSelectForUpdateBasedProxyManager<>(sqlProxyConfiguration);
    }

    /**
     * API 키에 해당하는 버킷에서 토큰을 소비하려고 시도하는 메서드
     *
     * @param apiKey API 키
     * @return 토큰 소비 성공 여부
     */
    public boolean tryConsume(String apiKey) {
        BucketProxy bucket = getOrCreateBucket(apiKey);
        // 버킷에서 토큰을 소비하려고 시도하고, 그 결과를 반환
        boolean consumed = bucket.tryConsume(1);
        log.info("API Key: {}, Consumed: {}, AvailableToken: {}, Time: {}", apiKey, consumed, bucket.getAvailableTokens(), LocalDateTime.now());
        return consumed;
    }

    /**
     * API 키에 해당하는 버킷을 가져오거나 생성하는 메서드
     *
     * @param apiKey API 키
     * @return 해당 API 키에 대응하는 버킷
     */
    private BucketProxy getOrCreateBucket(String apiKey) {
        return buckets.computeIfAbsent(apiKey, key -> {
            // API 키의 해시 코드를 버킷 ID로 사용
            Long bucketId = (long) key.hashCode();
            // 버킷 설정을 생성
            var bucketConfiguration = createBucketConfiguration();
            // 버킷 ID와 버킷 설정을 이용해 버킷을 생성하고, 이를 반환
            return proxyManager.builder()
                    .build(bucketId, bucketConfiguration);
        });
    }

    /**
     * 버킷 설정을 생성하는 메서드
     *
     * @return 생성된 버킷 설정
     */
    private BucketConfiguration createBucketConfiguration() {
        return BucketConfiguration.builder()
                .addLimit(
                        Bandwidth.builder()
                                .capacity(CAPACITY)
                                .refillIntervally(REFILL_AMOUNT, REFILL_DURATION)
                                .build()
                )
                .build();
    }

}
