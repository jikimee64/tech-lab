package com.soap.webflux.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

@Deprecated
public class MyFilter implements Filter {

    private EventNotify eventNotify;

    public MyFilter(EventNotify eventNotify) {
        this.eventNotify = eventNotify;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        System.out.println("필터 실행");

        HttpServletResponse servletResponse =
            (HttpServletResponse) response;
        //servletResponse.setContentType("text/plain; charset=utf-8");
        servletResponse.setContentType("text/event-stream; charset=utf-8");

        // --------- webflux 기능 시작
        PrintWriter out = servletResponse.getWriter();
        // 1. Reactive Stream 라이브러리 사용시 표준 지켜서 응답 가능
        for (int i = 0; i < 5; i++) {
            /**
             * 5초가 지나면 한번에 출력
             * 왜? text/plain 속성 떄문에
             * event-stream으로 변경하면 응답이 1초마다 생성
             * 스트림을 열어놓고 데이터를 계속 주는구나 라고 생각해서
             */
            out.print("응답 " + i + "\n");
            out.flush(); // 버퍼를 비움
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // --------- webflux 기능 끝

        // SSE 개념을 적용한 Push 기능
        // 2. SSE Emitter 라이브러리 사용시 편하게 구현 가능
        while(true){
            try {
                if(eventNotify.getChange()){
                    int lastIndex = eventNotify.getEvents().size() - 1;
                    out.print("응답 " + eventNotify.getEvents().get(lastIndex) + "\n");
                    out.flush(); // 버퍼를 비움
                    eventNotify.setChange(false);
                }
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 3. WebFlux -> Reactive Streams의 구현체(비동기 단일 스레드)
        // Servlet or Spring MVC에서 사용시 멀티 스레드 방식

    }
}
