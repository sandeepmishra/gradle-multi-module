package com.gradle.multimodule.eventproducer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
public class LibraryEventProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryEventProducerApplication.class, args);
    }

    @Bean
    public Docket postsApi(){
        return new Docket(DocumentationType.SWAGGER_2).groupName("library-event-producer").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.gradle.multimodule.eventproducer.controller")).paths(PathSelectors.any()).build();
    }

    @Bean
    UiConfiguration uiConfiguration(){
        return UiConfigurationBuilder.builder().displayRequestDuration(true).validatorUrl(StringUtils.EMPTY).build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Library event producer API").description("Library event producer api used for producing kafk events for library project").version("1.0").build();
    }
}
