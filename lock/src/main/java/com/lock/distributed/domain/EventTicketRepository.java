package com.lock.distributed.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventTicketRepository extends JpaRepository<EventTicket, Long> {
}