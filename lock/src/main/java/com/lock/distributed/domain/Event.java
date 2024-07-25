package com.lock.distributed.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int ticketLimit;

    @OneToMany(mappedBy = "event")
    private List<EventTicket> eventTickets;

    public Event(int ticketLimit) {
        this.ticketLimit = ticketLimit;
    }

    public boolean isClosed() {
        return eventTickets.size() >= ticketLimit;
    }

}
