package com.example.productServiceSpring.config;


import com.example.productServiceSpring.filter.TrackingIdFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<TrackingIdFilter> trackingIdFilter() {
        FilterRegistrationBean<TrackingIdFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TrackingIdFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1); 
        return registrationBean;
    }
}
