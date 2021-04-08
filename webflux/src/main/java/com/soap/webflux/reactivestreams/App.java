package com.soap.webflux.reactivestreams;

/**
 * WebFlux = 단일스레드,비동기 + Strema을 통해 백프레셔가 적용된 데이터만큼 간헐적 응답이 가능하다.
 * + 데이터가 소비가 끝나면 응답이 종료
 * SSE 적용(Servlet,WebFlux 둘다 가능, but 단일스레드방식이 좋음) = 데이터 소비가 끝나도 Stream 계속 유지
 */
public class App {
    public static void main(String[] args) {
        MyPub pub = new MyPub(); // 신문사 생성
        MySub sub = new MySub(); // 구독자 생성

        // 구독자 시작, 구독자 정보를 넘겨줌
        pub.subscribe(sub); // 구독 시작
    }
}
