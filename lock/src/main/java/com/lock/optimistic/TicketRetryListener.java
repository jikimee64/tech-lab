package com.lock.optimistic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class TicketRetryListener implements RetryListener {

    private ConcurrentHashMap<String, AtomicInteger> retryCounts = new ConcurrentHashMap<>();

    // retry 전체 프로세스 시작 전
    @Override
    public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
        String key = context.getAttribute("method.name") + "-" + Thread.currentThread().getId();
        retryCounts.putIfAbsent(key, new AtomicInteger(0));
        return true; // Always proceed with retry
    }

    // retry 전체 프로세스(recover 포함)를 모두 마무리 한 직후
    @Override
    public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        String key = context.getAttribute("method.name") + "-" + Thread.currentThread().getId();
        int count = retryCounts.get(key).get();
        System.out.println("Thread " + Thread.currentThread().getId() + " 리트라이 시도 횟수: " + count);
    }

    // retry 중 발생한 예외상황
    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        String key = context.getAttribute("method.name") + "-" + Thread.currentThread().getId();
        retryCounts.get(key).incrementAndGet();
    }
}