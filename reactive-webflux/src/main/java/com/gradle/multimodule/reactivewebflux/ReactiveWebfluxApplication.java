package com.gradle.multimodule.reactivewebflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


// https://projectreactor.io/docs/core/release/reference/


@SpringBootApplication
//@EnableEurekaClient
public class ReactiveWebfluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveWebfluxApplication.class, args);
	}

}
