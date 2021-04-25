package com.soap.hateoas.order;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    public List<Order> getAllOrdersForCustomer(String customerId) {

        List<Order> orders = new ArrayList<>();

        int i = 0;
        while (i < 5) {
            orders.add(Order.builder()
                .orderId("orderId" + i)
                .price(i)
                .quantity(i)
                .build());
            i++;
        }

        return orders;
    }

}
