package com.async;

import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApiService {

    public String hello() {
        for(int i = 1; i < 10; i++){
            try {
                Thread.sleep(2000);
                log.info("thread sleep ...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "async hello";
    }

    //보통사용하는 예시
    //여러 api에 동시에 전송한다음 거기에 대한 결과를 join해서 받을때
    //보통 사용하는게 좋은 형태
    // 다른 스레드에서 실행
    //사실 @Async를 쓰는것보다 메소드 내에서
    // CompletableFuture를 여러개 만들어서 전송시키고 받아서 조인하는게 좋음
    //@Async는 AOP이기 때문에 프록시 패턴을 탐
    //그래서 public 메소드에만 async를 넣을 수 있음
    //같은 클래스내에서 같은메소드를 호출할땐 async를 타지 않음
    //NOSQL을 쓰면 @Async보다 webflux추천, RDB면 @Async가 의미가 없음 왜?
    //RDB는 동기방식으로 하기 때문에..
    //그냥 이러한 방법이 있다정도만 알려줌
    @Async("async-thread")
    public CompletableFuture run(){
        return new AsyncResult(hello()).completable();
    }
}
