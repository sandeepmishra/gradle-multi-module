package com.gradle.multimodule.productservice;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableHystrix
@EnableHystrixDashboard
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

    @Bean
    public Docket postsApi(){
        return new Docket(DocumentationType.SWAGGER_2).groupName("admin-services-api").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.gradle.multimodule.productservice.controller")).paths(PathSelectors.any()).build();
    }

    @Bean
    UiConfiguration uiConfiguration(){
        return UiConfigurationBuilder.builder().displayRequestDuration(true).validatorUrl(StringUtils.EMPTY).build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Coupon services API").description("Admin services api is use of user authentication").version("1.0").build();
    }
}
