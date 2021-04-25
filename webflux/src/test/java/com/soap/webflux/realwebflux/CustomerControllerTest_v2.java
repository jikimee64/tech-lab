package com.soap.webflux.realwebflux;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

/**
 * https://howtodoinjava.com/spring-webflux/webfluxtest-with-webtestclient/
 */
// 컨트롤러만 메모리에 띄움, 어떤걸 메모리에 띄어주는지 확인하는게 중요
@WebFluxTest
    //@ExtendsWith 포함
//@Import(EmployeeService.class) //Interface는 빈에 등록 X
class CustomerControllerTest_v2 {

    /**
     * WebFluxTest 실행시 OO를 IoC 컨테이너에 임시로 등록 가짜 객체를 띄우는거기 때문에 .findAll 안됨
     */
    @MockBean
    CustomerRepository customerRepository;

    /**
     * @WebFluxTest 안에 @AutoConfigureWebTestCLient가 있기 때문에 WebTestClient가 빈에 등록됨
     */
    @Autowired
    private WebTestClient webClient; //비동기로 http 요청

    /**
     * controller의 응답이 잘됬는지 테스트
     */
    @Test
    public void 한건찾기_테스트() {

        // stub -> 행동 지시
        when(customerRepository.findById(1L))
            .thenReturn(Mono.just(new Customer("Jack", "Bauer")));

        webClient.get()
            .uri("/customer/{id}", 1L)
            .exchange()
            .expectBody()
            .jsonPath("$.firstName").isEqualTo("Jack")
            .jsonPath("$.lastName").isEqualTo("Bauer");

    }

}