package com.soap.webflux.reactivestreams;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Deprecated
public class MySub implements Subscriber<Integer> {

    private Subscription s;
    private int bufferSize = 2;

    // ★
    @Override
    public void onSubscribe(Subscription s) {
        System.out.println("구독자 : 구독 정보 잘 받았어");
        this.s = s;
        System.out.println("구독자 : 나 이제 신문 한개씩 줘");
        // 구독 정보에 있는 Request 호출
        // 신문 한개씩 매일 매일 줘!!
        // ★ (백프레셔) 소비가자 한번에 처리할 수 있는 개수를 요청
        s.request(2);
    }

    // ★
    // Flux : onNext()를 호출하면서 응답을 주는것
    // 2번 실행 -> request 2번실행 -> 데이터 꼬임
    @Override
    public void onNext(Integer t) {
        System.out.println("onNext() : " + t);
        // System.out.println("하루 지남");
        // ---------- 꼬임 방지 로직 시작
        bufferSize--;
        if(bufferSize == 0){
            System.out.println("하루 지남");
            bufferSize = 2;
            s.request(2); // 다음날에 또 줘야될때
        }
        // ---------- 꼬임 방지 로직 끝
        //s.request(2);
    }

    @Override
    public void onError(Throwable t) {
        System.out.println("구독중 에러");
    }

    @Override
    public void onComplete() {
        System.out.println("구독 완료");
    }
}
