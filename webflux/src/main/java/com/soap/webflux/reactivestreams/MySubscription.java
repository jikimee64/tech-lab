package com.soap.webflux.reactivestreams;

import java.util.Iterator;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * 구독 정보
 * 정보 : 구독자, 어떤 데이터를 구독할지
 */
public class MySubscription implements Subscription {

    private Subscriber s;
    private Iterator<Integer> it;

    public MySubscription(Subscriber s, Iterable<Integer> its) {
        this.s = s;
        this.it = its.iterator();
    }

    // n의 개수만큼 응답
    @Override
    public void request(long n) {
        while(n > 0){
            if(it.hasNext()){
                s.onNext(it.next());
            }else{
                // it는 10개 가지고있다. 근데 n이 20일때 줄 데이터가 없을때 else발생
                s.onComplete(); // 데이터를 다 받았을때
                break;
            }
            n--;
        }
    }

    @Override
    public void cancel() {

    }
}
