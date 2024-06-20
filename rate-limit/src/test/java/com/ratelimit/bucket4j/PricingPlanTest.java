package com.ratelimit.bucket4j;

import io.github.bucket4j.Bandwidth;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PricingPlanTest {

    @Test
    void testGetLimit() {
        Bandwidth freeLimit = PricingPlan.FREE.getLimit();
        Bandwidth basicLimit = PricingPlan.BASIC.getLimit();
        Bandwidth professionalLimit = PricingPlan.PROFESSIONAL.getLimit();

        assertThat(freeLimit.getCapacity()).isEqualTo(20);
        assertThat(basicLimit.getCapacity()).isEqualTo(40);
        assertThat(professionalLimit.getCapacity()).isEqualTo(100);
    }

}
