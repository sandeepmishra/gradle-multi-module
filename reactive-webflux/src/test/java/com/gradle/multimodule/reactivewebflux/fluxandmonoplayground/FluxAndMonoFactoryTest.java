package com.gradle.multimodule.reactivewebflux.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFactoryTest {

    List<String> names = Arrays.asList("sam", "carry", "jack");


    @Test
    public void testFluxUsingIterable(){
        Flux<String> stringFlux = Flux.fromIterable(names).log();
        // same way like iterable we can use fromArry(),  fromStream()
        StepVerifier.create(stringFlux)
                .expectNext("sam", "carry", "jack")
                .verifyComplete();
    }

    @Test
    public void testMonoUsingSupplier(){
        Mono<String> stringMono = Mono.justOrEmpty(null);// will create with or without supplied object..
        stringMono = Mono.fromSupplier(()->"from supplier");
        StepVerifier.create(stringMono.log()).expectNext("from supplier").verifyComplete();
    }

    @Test
    public void testFluxUsingRange(){
        Flux<Integer> integerFlux = Flux.range(1, 5);
        StepVerifier.create(integerFlux.log())
                .expectNext(1, 2, 3, 4, 5)
                .verifyComplete();
    }
}
