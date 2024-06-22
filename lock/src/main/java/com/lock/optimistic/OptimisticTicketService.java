package com.lock.optimistic;

import com.lock.Reservation;
import com.lock.ReservationRepository;
import com.lock.Ticket;
import com.lock.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OptimisticTicketService {

    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;

    /**
     * 낙관적 락은 재시도 요청이 들어온 순서대로 처리되는 것이 아니라, 재시도의 타이밍에 따라 다음 처리될 요청이 결정된다.
     * 요청의 순서에 따라 처리되는 선착순 상황에선 적절하지 않다.
     *
     * 낙관적 락의 버전 충돌 시 발생하는 예외는 아래와 같이 세 가지
     *  - (JPA) javax.persistence.OptimisticLockException
     *  - (Hibernate) org.hibernate.StaleObjectStateException in Hibernate
     *  - (Spring) org.springframework.orm.ObjectOptimisticLockingFailureException
     * Spring Data JPA에서 낙관적락을 사용하게 되면 버전 충돌 시 Hibernate는 StaleStateException을 발생시킨다.
     * Spring은 이를 OptimisticLockingFailureException으로 래핑하여 처리한다.
     *
     */
//    @Retry
    @Retryable(
            retryFor = {ObjectOptimisticLockingFailureException.class},
            maxAttempts = 1000,
            backoff = @Backoff(delay = 100),
            listeners = "ticketRetryListener"
    )
    @Transactional
    public void ticketing(long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket Not Found."));
        ticket.increaseReservedAmount();
        int sequence = ticket.getReservedAmount();
        reservationRepository.save(new Reservation(ticket, sequence));
    }
}
