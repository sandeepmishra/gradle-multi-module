package com.gradle.multimodule.reactivewebflux.fluxandmonoplayground;


import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoTest {

    @Test
    public void testFlux(){
        Flux<String> stringFlux = Flux.just("spring", "spring boot", "reactive spring")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                // once error is emitted it will not send any other data (.concatWith("somedata) after error will not get prcessed)
                // even oncomplete() method will also be not be processed once error occurred
                .log(); // log will display complete processing of element from subscription till completion


        stringFlux.subscribe(System.out::println, (e) -> System.err.println(e),
                () -> System.out.println("completed..")); // if flux ends with error this part will not execute
    }

    @Test
    public void testFluxElements_WithoutError(){
        Flux<String> stringFlux = Flux.just("spring", "spring boot", "reactive spring").log();
        StepVerifier.create(stringFlux)
        .expectNext("spring")
        .expectNext("spring boot")
                .expectNext("reactive spring").verifyComplete();
        //expectNext("spring", "spring boot", "reactive spring") can work in above same way expectNext working with single string element
    }

    @Test
    public void testFluxElements_WithError(){
        Flux<String> stringFlux = Flux.just("spring", "spring boot", "reactive spring")
                .concatWith(Flux.error(new RuntimeException("Exception occurred"))).log();
        StepVerifier.create(stringFlux)
                .expectNext("spring")
                .expectNext("spring boot")
                .expectNext("reactive spring")
                //.expectError(RuntimeException.class).
                .expectErrorMessage("Exception occurred"). // can not use both error and error message method at same time
                verify();
    }

    @Test
    public void testFluxElementsCount(){
        Flux<String> stringFlux = Flux.just("spring", "spring boot", "reactive spring").log();
        StepVerifier.create(stringFlux)
                .expectNextCount(3).verifyComplete();
    }

    @Test
    public void testMono(){
        Mono<String> stringMono = Mono.just("spring");
        StepVerifier.create(stringMono.log())
                .expectNext("spring")
                .verifyComplete();
    }
}
