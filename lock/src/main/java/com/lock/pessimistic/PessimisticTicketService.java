package com.lock.pessimistic;

import com.lock.Reservation;
import com.lock.ReservationRepository;
import com.lock.Ticket;
import com.lock.TicketRepository;
import com.lock.optimistic.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PessimisticTicketService {

    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;

    @Retry
    @Transactional
    public void ticketing(long ticketId) {
        Ticket ticket = ticketRepository.findByIdForUpdate(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket Not Found."));
        ticket.increaseReservedAmount();
        int sequence = ticket.getReservedAmount();
        reservationRepository.save(new Reservation(ticket, sequence));
    }

}
