package com.async;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

@Slf4j
public class WebClientTest {

    private static String[] URL = {
        "http://localhost:8082",
        "http://localhost:8083",
    };

//    @Test
//    void test2(){
//        WebClient webClient = WebClient.create(URL[0]);
//
//        TokenRes dncjf64 = webClient.post()
//            .uri("/getToken")
//            .contentType(MediaType.APPLICATION_JSON)
//            .bodyValue(new TokenReq("dncjf64"))
//            .retrieve() //response body 가져오기
//            .onStatus(HttpStatus::is5xxServerError,
//                __ -> Mono.error(new IllegalArgumentException("5xx")))
//            .bodyToMono(TokenRes.class)
//            .block();
//        log.info("이아파요 {}", dncjf64.getToken());
//    }

    @Test
    void test1(){
        try {
            Mono<TokenRes> tokenResMono = localhost_8082()
                .subscribeOn(Schedulers.elastic())
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(1)));
            log.info("8082끝");

            Mono<TokenRes> tokenResMono2 = localhost_8083()
                .subscribeOn(Schedulers.elastic())
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(1)));
            log.info("8083끝");

            Map<String, TokenRes> block = Mono
                .zip(tokenResMono, tokenResMono2, (tokenRes3, tokenRes4) -> {
                    Map<String, TokenRes> map = new LinkedHashMap<>();
                    map.put("tokenRes3", tokenRes3);
                    map.put("tokenRes4", tokenRes4);
                    return map;
                }).block();

            block.forEach((key, value) -> {
                System.out.println(key);
                System.out.println(value);
            });
        }catch(Exception e){
            log.info("멈추지마라");
            e.printStackTrace();
        }

    }

    private Mono<TokenRes> localhost_8082() {
        log.info("localhost_8082 호출");
        WebClient webClient = WebClient.create(URL[0]);
        Mono<TokenRes> hello = webClient.post()
            .uri("/getToken")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new TokenReq("dncjf64"))
            .retrieve() //response body 가져오기
            .onStatus(HttpStatus::is5xxServerError,
                __ -> Mono.error(new IllegalArgumentException("5xx")))
            .bodyToMono(TokenRes.class);
        log.info("localhost_8082 반환 {}", hello);

        return hello;
    }

    private Mono<TokenRes> localhost_8083() {
        log.info("localhost_8083 호출");
        WebClient webClient = WebClient.create(URL[1]);
        Mono<TokenRes> hello = webClient.post()
            .uri("/getToken")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new TokenReq("dncjf64"))
            .retrieve() //response body 가져오기
            .onStatus(HttpStatus::is5xxServerError,
                __ -> Mono.error(new IllegalArgumentException("5xx")))
            .bodyToMono(TokenRes.class);
        log.info("localhost_8083 반환 {}", hello);
        return hello;
    }


    //https://stackoverflow.com/questions/62329617/webflux-webclient-re-try-with-different-url
//    private Mono<String> fromUrl(String url) {
//
//    }



}
