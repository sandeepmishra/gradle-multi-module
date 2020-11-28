package com.gradle.multimodule.reactivewebflux.repository;

import com.gradle.multimodule.reactivewebflux.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String> {

    Mono<Item> findByDescription(String description);
}
