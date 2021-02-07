package com.gradle.multimodule.couponservice;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableEurekaClient
@EnableJpaRepositories(basePackages="com.gradle.multimodule.couponservice.repository")
@EnableTransactionManagement
@EntityScan(basePackages="com.gradle.multimodule.couponservice.entities")
public class CouponServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CouponServiceApplication.class, args);
    }

    @Bean
    public Docket postsApi(){
        return new Docket(DocumentationType.SWAGGER_2).groupName("admin-services-api").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.gradle.multimodule.couponservice.controller")).paths(PathSelectors.any()).build();
    }

    @Bean
    UiConfiguration uiConfiguration(){
        return UiConfigurationBuilder.builder().displayRequestDuration(true).validatorUrl(StringUtils.EMPTY).build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Coupon services API").description("Admin services api is use of user authentication").version("1.0").build();
    }
}
