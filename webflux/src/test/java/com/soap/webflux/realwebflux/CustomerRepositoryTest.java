package com.soap.webflux.realwebflux;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Repostiory만 올림
 */
@DataR2dbcTest
@Import(DBinit.class)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void 한건찾기_테스트(){
//        customerRepository.findById(1L).subscribe((c) -> {
//            System.out.println(c);
//        });

        StepVerifier
            .create(customerRepository.findById(2L))
            .expectNextMatches((c) -> {
                return c.getFirstName().equals("Chloe");
            })
            .expectComplete()
            .verify();

    }

}