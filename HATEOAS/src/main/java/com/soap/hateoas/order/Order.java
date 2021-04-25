package com.soap.hateoas.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@Builder
public class Order extends RepresentationModel<Order> {
    private String orderId;
    private double price;
    private int quantity;

}
