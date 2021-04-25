package com.soap.hateoas.customer;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.soap.hateoas.order.Order;
import java.util.ArrayList;
import java.util.List;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {



    public List<Customer> allCustomers(){
        List<Customer> customers = new ArrayList<>();

        int i = 0;
        while (i < 5) {
            customers.add(Customer.builder()
                .customerId("customerId" + i)
                .customerName("customerName" + i)
                .companyName("companyName" + i)
                .build());
            i++;
        }
        return customers;
    }

    public Customer getCustomerDetail(String customerId) {
        Customer customer = Customer.builder()
            .customerId("10A")
            .customerName("Jane")
            .companyName("ABC Company")
            .build();

        Link link = new Link("http://localhost:8080/spring-security-rest/api/customers/10A");
        customer.add(link);
        //customer.add(linkTo(CustomerController.class).slash(customer.getCustomerId()).withSelfRel());

        return customer;

    }


}
