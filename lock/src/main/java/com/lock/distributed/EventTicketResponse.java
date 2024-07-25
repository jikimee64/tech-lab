package com.lock.distributed;

public record EventTicketResponse(
        Long eventTicketId,
        Long eventId
) {
}
