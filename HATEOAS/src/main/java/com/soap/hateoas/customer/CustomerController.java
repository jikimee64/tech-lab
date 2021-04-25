package com.soap.hateoas.customer;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.soap.hateoas.order.Order;
import com.soap.hateoas.order.OrderService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;


    @GetMapping("/{customerId}")
    public Customer getCustomerById(@PathVariable String customerId) {
        return customerService.getCustomerDetail(customerId);
    }

    @GetMapping(value = "{customerId}/order/{orderId}")
    public Order getOrderById(@PathVariable final String customerId, String orderId) {
        return Order.builder()
            .orderId(customerId)
            .price(1)
            .quantity(1)
            .build();
    }

    @GetMapping(value = "/{customerId}/orders", produces = { "application/hal+json" })
    public CollectionModel<Order> getOrdersForCustomer(@PathVariable final String customerId) {
        List<Order> orders = orderService.getAllOrdersForCustomer(customerId);
        for (final Order order : orders) {
            Link selfLink = linkTo(methodOn(CustomerController.class)
                .getOrderById(customerId, order.getOrderId())).withSelfRel();
            order.add(selfLink);
        }

        Link link = linkTo(methodOn(CustomerController.class)
            .getOrdersForCustomer(customerId)).withSelfRel();
        CollectionModel<Order> result = CollectionModel.of(orders, link);
        return result;
    }

    @GetMapping(produces = {"application/hal+json"})
    public CollectionModel<Customer> getAllCustomers() {
        List<Customer> allCustomers = customerService.allCustomers();

        for (final Customer customer : allCustomers) {
            String customerId = customer.getCustomerId();
            Link selfLink = linkTo(CustomerController.class).slash(customerId).withSelfRel();
            customer.add(selfLink);
            if (orderService.getAllOrdersForCustomer(customerId).size() > 0) {
                Link ordersLink = linkTo(methodOn(CustomerController.class)
                    .getOrdersForCustomer(customerId)).withRel("allOrders");
                customer.add(ordersLink);
            }
        }
        Link link = linkTo(CustomerController.class).withSelfRel();
        CollectionModel<Customer> result = CollectionModel.of(allCustomers, link);
        return result;
    }

}
