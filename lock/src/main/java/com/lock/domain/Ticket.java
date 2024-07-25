package com.lock.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int totalAmount;

    private int reservedAmount = 0;

    @Version
    private Integer version;

    public Ticket(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void increaseReservedAmount() {
        if (reservedAmount >= totalAmount) {
            throw new IllegalArgumentException("Sold out.");
        }
        reservedAmount++;
    }
}