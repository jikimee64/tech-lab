package com.lock.optimistic;

import jakarta.persistence.OptimisticLockException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.StaleObjectStateException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

/**
 * @Transactional의 기본 Order 값은 LOWEST_PRECEDENCE (Integer.MAX_VALUE)
 * Order 값이 낮을수록 우선순위가 높다.
 * @Retry는 @Transactional 직전에 실행되도록 하기 위해 Order 값을 Ordered.LOWEST_PRECEDENCE - 1로 설정한다.
 */
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Aspect
@Component
public class OptimisticLockRetryAspect {

    @Around("@annotation(retry)")
    public Object retryOptimisticLock(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
        Exception exceptionHolder = null;
        for (int attempt = 0; attempt < retry.maxRetries(); attempt++) {
            try {
                return joinPoint.proceed();
            } catch (OptimisticLockException | ObjectOptimisticLockingFailureException | StaleObjectStateException e) {
                exceptionHolder = e;
                Thread.sleep(retry.retryDelay());
            }
        }
        throw exceptionHolder; // 재시도 초과시 마지막으로 발생한 예외를 던진다.
    }
}