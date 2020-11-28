package com.gradle.multimodule.reactivewebflux.handler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
//@SpringBootTest
@AutoConfigureWebTestClient
//@DirtiesContext
//@WebFluxTest
public class SampleHandlerFunctionTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testFluxController(){
        Flux<Integer> integerFlux = webTestClient.get().uri("/functional/flux")
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
                .returnResult(Integer.class).getResponseBody();

        StepVerifier.create(integerFlux).expectSubscription().expectNext(1, 2, 3, 4)
                .verifyComplete();
    }
}
