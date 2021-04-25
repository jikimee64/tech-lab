package com.soap.hateoas.customer;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@Builder
public class Customer extends RepresentationModel<Customer> {
    private String customerId;
    private String customerName;
    private String companyName;
}
