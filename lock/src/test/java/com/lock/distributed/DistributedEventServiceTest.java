package com.lock.distributed;

import com.lock.distributed.domain.Event;
import com.lock.distributed.domain.EventRepository;
import com.lock.distributed.domain.EventTicketRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class DistributedEventServiceTest {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventTicketRepository eventTicketRepository;

    @Autowired
    private EventService service;

    @AfterEach
    void tearDown() {
        eventTicketRepository.deleteAll();
        eventRepository.deleteAll();
    }

    @Test
    void 이벤트_티켓_동시성_테스트() throws InterruptedException {
        // given
        int memberCount = 30;
        int ticketAmount = 10;
        Event savedEvent = eventRepository.save(new Event(ticketAmount));

        ExecutorService executorService = Executors.newFixedThreadPool(memberCount);
        CountDownLatch countDownLatch = new CountDownLatch(memberCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // when
        for (int i = 0; i < memberCount; i++) {
            executorService.submit(() -> {
                try {
                    service.createEventTicket(savedEvent.getId());
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    failCount.incrementAndGet();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        System.out.println("successCount = " + successCount);
        System.out.println("failCount = " + failCount);

        // then
        long eventTicketCount = eventTicketRepository.count();
        assertThat(eventTicketCount)
                .isEqualTo(Math.min(memberCount, ticketAmount));
    }

}
