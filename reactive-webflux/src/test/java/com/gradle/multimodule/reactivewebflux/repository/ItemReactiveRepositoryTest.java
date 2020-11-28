package com.gradle.multimodule.reactivewebflux.repository;

import com.gradle.multimodule.reactivewebflux.document.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
@RunWith(SpringRunner.class)
@DirtiesContext
public class ItemReactiveRepositoryTest {

    @Autowired
    private ItemReactiveRepository itemReactiveRepository;

    List<Item> items = Arrays.asList(new Item(null, "Samsung TV", 400.0),
    new Item(null, "Sony TV", 432.5),
    new Item(null, "Apple Watch", 299.25),
    new Item(null, "Beats Headphones", 149.05),
    new Item("5fb177517a834b29b3535751", "Bose Headphones", 149.05));

    @Before
    public void setUp(){
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(items))
        .flatMap(itemReactiveRepository::save)
        .doOnNext(item -> System.out.println(item)).blockLast();
    }

    @Test
    public void getAllItems(){
        StepVerifier.create(itemReactiveRepository.findAll())
                .expectSubscription()
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    public void getItemsById(){
        StepVerifier.create(itemReactiveRepository.findById("5fb177517a834b29b3535751"))
                .expectSubscription()
                .expectNextMatches(item -> item.getDescription().equals("Bose Headphones"))
                .verifyComplete();
    }

    @Test
    public void findItemByDescription(){
        StepVerifier.create(itemReactiveRepository.findByDescription("Bose Headphones").log("Running find by description test"))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void saveItem(){
        Item item = new Item("DEFID", "Google Home mini", 50.0);
        Mono<Item> savedItem = itemReactiveRepository.save(item);
        StepVerifier.create(savedItem)
                .expectSubscription()
                .expectNextMatches(item1 -> item1.getId() != null)
                .verifyComplete();
    }

    @Test
    public void updateItem(){
        Mono<Item> updateItem = itemReactiveRepository.findByDescription("Samsung TV")
                .map(item -> {
                    item.setPrice(425.0);
                    return item;
                }).flatMap(item -> itemReactiveRepository.save(item));

        StepVerifier.create(updateItem)
                .expectSubscription()
                .expectNextMatches(item1 -> item1.getPrice() == 425.0 && item1.getDescription().equals("Samsung TV"))
                .verifyComplete();
    }

    @Test
    public void deleteById(){
        Mono<Void> deletedItem = itemReactiveRepository.findById("5fb177517a834b29b3535751")
                .map(Item::getId)
                .flatMap(id->itemReactiveRepository.deleteById(id));

        StepVerifier.create(deletedItem)
                .expectSubscription()
                .verifyComplete();

    }

    @Test
    public void deleteItem(){
        Mono<Void> deletedItem = itemReactiveRepository.findById("DEFID")
                //.map(Item::getId)
                .flatMap(item->itemReactiveRepository.delete(item));

        StepVerifier.create(deletedItem)
                .expectSubscription()
                .verifyComplete();

    }
}
