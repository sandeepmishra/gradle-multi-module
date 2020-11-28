package com.gradle.multimodule.reactivewebflux.controller;

import com.gradle.multimodule.reactivewebflux.document.Item;
import com.gradle.multimodule.reactivewebflux.repository.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class ItemController {

    @Autowired
    private ItemReactiveRepository itemReactiveRepository;

    @GetMapping("/items")
    public Flux<Item> getAllItems(){
        return itemReactiveRepository.findAll();
    }

    @GetMapping("/items/{id}")
    public Mono<ResponseEntity<Item>> getAllItems(@PathVariable String id){
        return itemReactiveRepository.findById(id)
                .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/items/")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> getAllItems(@RequestBody Item item){
        return itemReactiveRepository.save(item);
    }


    @DeleteMapping("/items/{id}")
    public Mono<Void> deleteItem(@PathVariable String id){
        return itemReactiveRepository.deleteById(id);
    }

    @PutMapping("/items/{id}")
    public Mono<ResponseEntity<Item>> updateItem(@PathVariable String id, @RequestBody Item item){
        return itemReactiveRepository.save(item)
                .map(updateItem -> new ResponseEntity<>(updateItem, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
