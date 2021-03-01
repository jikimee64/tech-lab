package com.websocket.websocketredis.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable() //기본값이 on인 csrf 취약점 보안을 해제. on으로 설정해도 되나 설정할 경우 웹페이지에서 추가처리가 필요
        .headers()
            .frameOptions().sameOrigin() //SockJS는 기본적으로 HTML iframe 요소를 통한 전송을 허용하지 않도록 설정되는데 해당 내용을 해제
        .and()
            .formLogin() //권한없이 페이지 접근하면 로그인 페이지로 이동
        .and()
            .authorizeRequests()
                .antMatchers("/chat/**").hasRole("USER") //chat으로 시작하는 리소스에 대한 접근권한 설정
                .anyRequest().permitAll(); //나머지 리소스에 대한 접근 설정
    }

    /**
     * 테스트를 위한 In-Memory 계정 임의 생성
     */
    @Override
    protected  void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("happydaddy")
                .password("{noop}1234") //비번 : 1234
                .roles("USER")
            .and()
                .withUser("angrydaddy")
                .password("{noop}1234") //비번 : 1234
                .roles("USER")
            .and()
                .withUser("guest")
                .password("{noop}1234") //비번 : 1234
                .roles("GUEST");

    }

}
