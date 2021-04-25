package com.soap.webflux.realwebflux;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@RestController
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final Sinks.Many<Customer> sink;

    // A요청 -> Flux -> Stream
    // B요청 -> Flux -> Stream
    // -> 두개의 스트림을 합칠 수 있음 -> 합치면 싱크가 맞쳐짐
    // -> Flux.merge- > sink
    // sink : 모든 client들의 Flux 요청을 sink?할수있음
    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        // multicast() : 새로 push된 데이터만 구독자에게 전송하는 방식
        // 유니캐스트 등 다양한 방식 사용 가능
        sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    /**
     * Flux.just : 데이터를 순차적으로 꺼내서 던져줌
     * 다 돌고나서 데이터가 응답이됨
     */
    @GetMapping("/flux")
    public Flux<Integer> flux(){
        return Flux.just(1,2,3,4,5).delayElements(Duration.ofSeconds(1)).log();
    }

    /**
     * 화면에도 순차적으로 나감
     * 버퍼로 플러시하는 개념
     * Response가 유지되면서 계속 하나씩 가는것
     * @return
     */
    @GetMapping(value = "/fluxstream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> fluxstream(){
        /**
         *
         */
        return Flux.just(1,2,3,4,5).delayElements(Duration.ofSeconds(1)).log();
    }

    /**
     *  : onSubscribe(FluxUsingWhen.UsingWhenSubscriber)
     *  => db의 데이터에 subscribe를 함 onsubscribe를 하면 응답을 함
     *  => 해석 : 나너구독할께 / 응 
     *  : request(unbounded)
     *  => unbounded : 니가 가지고 있는 데이터 다줘
     *  : onNext(Customer(id=1, firstName=Jack, lastName=Bauer))
     *  ...
     *  : onComplete()
     *  => 데이터를 다 받아서 onComplete
     *  => onComplete되는 순간 응답이 됨
     * @return
     */

    // APPLICATION_STREAM_JSON_VALUE : 스트림을 달아놔서 응답을 다 던져주면 연결 끝
   //@GetMapping(value = "/customer", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    @GetMapping("customer")
    public Flux<Customer> findAll(){
        //return customerRepository.findAll().delayElements(Duration.ofSeconds(1)).log();
        return customerRepository.findAll().log();
    }

    /**
     * 데이터가 1건이면 return을 Mono
     * 데이터가 여러건이면 Flux
     * onNext() 1번하고 onComplete
     */
    @GetMapping("/customer/{id}")
    public Mono<Customer> findById(@PathVariable Long id){
        return customerRepository.findById(id).log();
    }

    /**
     * sse는 연결이 계속 유지되는 프로토콜
     * TEXT_EVENT_STREAM_VALUE : 표준(이걸 적용하면 SSE프로토콜이 적용되서 응답됨)
     * 특징 : json 데이터 앞에 data라는게 붙음
     * 다 던지면 멈춤
     * 이걸 안멈추게 할려면...
     *
     */
    @GetMapping(value = "/customer/sse")
    //ServerSentEvent로 보내면 produces가 자동으로 TEXT_EVENT_STREAM_VALUE로 세팅됨(생략 가능)
    //public Flux<Customer> findAllSSE(){
    public Flux<ServerSentEvent<Customer>> findAllSSE(){
        //return customerRepository.findAll().delayElements(Duration.ofSeconds(1)).log();
        //싱크에 데이터가 합쳐지면 합쳐진 데이터를 응답해줌
        //합쳐진 데이터가 없으면 계속 삥글삥글돔
        //sink에 데이터를 push해주는 순간 스트림에 새로운 데이터가 나와서 바로 화면에 출력됨
        //단점 : sse를 끊으면 이를 알려줘야함 : sink.asFlux().blockLast();
        return sink.asFlux().map(c-> ServerSentEvent.builder(c).build()).doOnCancel(() -> {
            sink.asFlux().blockLast(); // 취소될때 강제로 마지막 데이터라고 sink에 알려주는것
        });
    }

    @PostMapping(value = "/customer")
    public Mono<Customer> save(){
        return customerRepository.save(new Customer("gildong","Hong"))
            .doOnNext(c -> {
                sink.tryEmitNext(c); //sink에 push하는것
            });
    }

}
