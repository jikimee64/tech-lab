package com.lock.nonlock;

import com.lock.domain.Reservation;
import com.lock.domain.ReservationRepository;
import com.lock.domain.Ticket;
import com.lock.domain.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NonLockTicketService {

    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public void ticketing(long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket Not Found."));
        ticket.increaseReservedAmount();
        int sequence = ticket.getReservedAmount();
        reservationRepository.save(new Reservation(ticket, sequence));
    }
}
