package com.lock.distributed;

import com.lock.distributed.domain.Event;
import com.lock.distributed.domain.EventRepository;
import com.lock.distributed.domain.EventTicket;
import com.lock.distributed.domain.EventTicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventTicketRepository eventTicketRepository;
    private final RedisLockRepository redisLockRepository;

    public EventService(final EventRepository eventRepository, final EventTicketRepository eventTicketRepository,
        RedisLockRepository redisLockRepository) {
        this.eventRepository = eventRepository;
        this.eventTicketRepository = eventTicketRepository;
        this.redisLockRepository = redisLockRepository;
    }

    @Transactional
    public EventTicketResponse createEventTicket(final Long eventId) {
        while (!redisLockRepository.lock(eventId)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } // 락을 획득하기 위해 무한 루프를 돌면서 대기 ( Thread.sleep()을 적용하여 레디스 서버 부하 방지 )

        try {
            Event event = eventRepository.findById(eventId).orElseThrow();
            if (event.isClosed()) {
                throw new RuntimeException("마감 되었습니다.");
            }

            EventTicket savedEventTicket = eventTicketRepository.save(new EventTicket(event));
            return new EventTicketResponse(savedEventTicket.getId(), savedEventTicket.getEvent().getId());
        } finally {
            redisLockRepository.unlock(eventId);
        }
    }
}