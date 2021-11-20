package com.soap.springevent.baeldung.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class GenericSpringEventListener {


    //Spel, String의 GenericSpringEvent 가 성공한 경우에만 호출
    //@EventListener(condition = "#event.success")
    /**
     * AFTER_COMMIT (기본값)는 트랜잭션이 성공적으로 완료된 경우 이벤트를 발생시키는 데 사용됩니다 .
     * AFTER_ROLLBACK – 트랜잭션이 롤백된 경우
     * AFTER_COMPLETION – 트랜잭션이 완료된 경우 ( AFTER_COMMIT 및 AFTER_ROLLBACK에 대한 별칭 )
     * BEFORE_COMMIT 는 트랜잭션 커밋 직전에 이벤트를 발생시키는 데 사용됩니다 .
     *
     * 이 리스너는 이벤트 생성자가 실행 중이고 커밋하려고 하는 트랜잭션이 있는 경우에만 호출됩니다
     * 실행 중인 트랜잭션이 없으면 fallbackExecution 속성을 true 로 설정하여 이를 재정의하지 않는 한 이벤트가 전혀 전송되지 않습니다.
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleSuccessful(GenericSpringEvent<String> event){
      log.info("Handling generic event (conditional). ");
    }

}