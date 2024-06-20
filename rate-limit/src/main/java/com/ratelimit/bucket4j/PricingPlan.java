package com.ratelimit.bucket4j;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;

import java.time.Duration;


// 사용자 요금제에 따른 API 호출 제한
public enum PricingPlan {
    // 한 시간에 20번 사용 가능
    FREE {
        @Override
        public Bandwidth getLimit() {
            return Bandwidth.classic(20, Refill.intervally(20, Duration.ofHours(1)));
        }
    },
    // 한 시간에 40번 사용 가능
    BASIC {
        @Override
        public Bandwidth getLimit() {
            return Bandwidth.classic(40, Refill.intervally(40, Duration.ofHours(1)));
        }
    },
    // 한 시간에 100번 사용 가능
    PROFESSIONAL {
        @Override
        public Bandwidth getLimit() {
            return Bandwidth.classic(100, Refill.intervally(100, Duration.ofHours(1)));
        }
    };

    PricingPlan() {
    }

    public abstract Bandwidth getLimit();
}