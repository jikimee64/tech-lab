package com.lock.none;

import com.lock.ReservationRepository;
import com.lock.Ticket;
import com.lock.TicketRepository;
import com.lock.nonlock.NonLockTicketService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class NonLockTicketServiceTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private NonLockTicketService service;

    @AfterEach
    void tearDown() {
        reservationRepository.deleteAll();
        ticketRepository.deleteAll();
    }

    @Test
    void 티켓_순차적_예매_테스트() {
        // given
        int memberCount = 30;
        int ticketAmount = 10;
        Ticket ticket = ticketRepository.save(new Ticket(ticketAmount));

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // when
        for (int i = 0; i < memberCount; i++) {
            try {
                service.ticketing(ticket.getId());
                successCount.incrementAndGet();
            } catch (Exception e) {
                failCount.incrementAndGet();
            }
        }

        System.out.println("successCount = " + successCount);
        System.out.println("failCount = " + failCount);

        // then
        long reservationCount = reservationRepository.count();
        assertThat(reservationCount)
                .isEqualTo(Math.min(memberCount, ticketAmount));
    }

    /**
     * 테스트 실패를 의도
     */
    @Test
    @Disabled
    void 티켓_동시_예매_테스트() throws InterruptedException {
        // given
        int memberCount = 30;
        int ticketAmount = 10;
        Ticket ticket = ticketRepository.save(new Ticket(ticketAmount));

        ExecutorService executorService = Executors.newFixedThreadPool(memberCount);
        CountDownLatch countDownLatch = new CountDownLatch(memberCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // when
        for (int i = 0; i < memberCount; i++) {
            executorService.submit(() -> {
                try {
                    service.ticketing(ticket.getId());
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    failCount.incrementAndGet();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();;

        System.out.println("successCount = " + successCount);
        System.out.println("failCount = " + failCount);

        // then
        long reservationCount = reservationRepository.count();
        assertThat(reservationCount)
                .isEqualTo(Math.min(memberCount, ticketAmount));
    }

}
