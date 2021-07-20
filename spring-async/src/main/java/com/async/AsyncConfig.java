package com.async;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {
    @Bean("async-thread")
    public Executor asyncThread(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();

        //몇개를 세팅하는게 좋은지 알고 설정하기
        //poolsize10개를 다쓰게되면 queue에 들어감 queue까지도 다 차면
        //poolsize만큼 늘어남 20개개가 또 꽉차면 10개가 늘어남 이를 반복
        //환경이나 request개수에 따라 다르기 때문에 적절히 ㄱㄱ
         threadPoolTaskExecutor.setMaxPoolSize(100); //알고 설정
        threadPoolTaskExecutor.setCorePoolSize(10); //알고 설정
        threadPoolTaskExecutor.setQueueCapacity(10); //알고 설정
        threadPoolTaskExecutor.setThreadNamePrefix("Async-");
        return threadPoolTaskExecutor;
    }

}
