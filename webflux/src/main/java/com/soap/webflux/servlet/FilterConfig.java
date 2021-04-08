package com.soap.webflux.servlet;

import javax.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Deprecated
@Configuration
public class FilterConfig {

    @Autowired
    private EventNotify eventNotify;

    @Bean
    public FilterRegistrationBean<Filter> addFilter(){
        System.out.println("필터 등록");
        FilterRegistrationBean<Filter> bean =
            new FilterRegistrationBean<Filter>(new MyFilter(eventNotify));
        // bean.addUrlPatterns("/*");
        bean.addUrlPatterns("/sse");
        return bean;
    }

    @Bean
    public FilterRegistrationBean<Filter> addFilter2(){
        System.out.println("필터 등록");
        FilterRegistrationBean<Filter> bean =
            new FilterRegistrationBean<Filter>(new MyFilter2(eventNotify));
        // bean.addUrlPatterns("/*");
        bean.addUrlPatterns("/add");
        return bean;
    }

}
