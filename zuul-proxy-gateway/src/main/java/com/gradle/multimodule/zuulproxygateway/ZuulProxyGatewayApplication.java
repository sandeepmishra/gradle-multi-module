package com.gradle.multimodule.zuulproxygateway;

import com.gradle.multimodule.zuulproxygateway.filters.CustomFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
public class ZuulProxyGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulProxyGatewayApplication.class, args);
    }

    @Bean
    public CustomFilter getFilter(){
        return new CustomFilter();
    }
}
