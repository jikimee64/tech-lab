package com.soap.webflux.realwebflux;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

/**
 * https://howtodoinjava.com/spring-webflux/webfluxtest-with-webtestclient/
 */
// 컨트롤러만 메모리에 띄움
//@WebFluxTest //@ExtendsWith 포함
//@Import(EmployeeService.class) //Interface는 빈에 등록 X
@SpringBootTest //customerRepository.findAll을 하기 위해 통합테스트로 변경
@AutoConfigureWebTestClient //통합 테스트시 WebTestClient를 쓰기 위한 어노테이션
class CustomerControllerTest {

    /**
     * WebFluxTest 실행시 OO를 IoC 컨테이너에 임시로 등록
     * 가짜 객체를 띄우는거기 때문에 .findAll 안됨
     */
//    @MockBean
//    CustomerRepository customerRepository;

    @Autowired
    CustomerRepository customerRepository;

    /**
     * @WebFluxTest 안에 @AutoConfigureWebTestCLient가 있기 때문에
     * WebTestClient가 빈에 등록됨
      */
//    @Autowired
//    private WebTestClient webClient; //비동기로 http 요청

    @Test
    public void 한건찾기_테스트(){
        System.out.println("===============================");
        Flux<Customer> fCustomer = customerRepository.findAll();
        fCustomer.subscribe((t) -> {
            System.out.println("데이터");
            System.out.println(t);
        });
    }

}