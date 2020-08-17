package com.gradle.multimodule.adminservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
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
public class AdminServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminServicesApplication.class, args);
    }

    @Bean
    public Docket postsApi(){
        return new Docket(DocumentationType.SWAGGER_2).groupName("admin-services-api").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.gradle.multimodule.adminservices.controller")).paths(PathSelectors.any()).build();
    }

    @Bean
    UiConfiguration uiConfiguration(){
        return UiConfigurationBuilder.builder().displayRequestDuration(true).validatorUrl("").build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Admin services API").description("Admin services api is use of user authentication").version("1.0").build();
    }

}
