package com.soap.webflux.reactivestreams;

import java.util.Arrays;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

/**
 * 신문사
 */
@Deprecated
public class MyPub implements Publisher<Integer> {

    Iterable<Integer> its = Arrays.asList(1,2,3,4,5,6,7,8,9,10);

    @Override
    public void subscribe(Subscriber<? super Integer> s){
        System.out.println("구독자 : 신문사야 나 너의신문 볼께");
        System.out.println("신문사 : 알겠어 --구독정보를 만들어서 줄테니 기다려!!");

        // 구독자 정보, 구독할 데이터 정보
        MySubscription subscription = new MySubscription(s, its);
        System.out.println("신문사 : 구독 정보 생성 완료 했어 잘받아!!");
        s.onSubscribe(subscription); // 구독정보를 돌려줌,
    }

}
