package com.gradle.multimodule.reactivewebflux.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
public class FluxAndManoControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testFluxController(){
        Flux<Integer> integerFlux = webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
                .returnResult(Integer.class).getResponseBody();

        StepVerifier.create(integerFlux).expectSubscription().expectNext(1, 2, 3, 4)
                .verifyComplete();
    }

    @Test
    public void testFluxControllerOtherApproach(){
        EntityExchangeResult<List<Integer>> integerFlux = webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
                .expectBodyList(Integer.class).returnResult();

        assertEquals(Arrays.asList(1, 2, 3, 4), integerFlux.getResponseBody());
    }
}
