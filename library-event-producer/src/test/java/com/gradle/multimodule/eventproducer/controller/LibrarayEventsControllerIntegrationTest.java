//package com.gradle.multimodule.eventproducer.controller;
//
//
//import com.gradle.multimodule.eventproducer.domain.Book;
//import com.gradle.multimodule.eventproducer.domain.LibraryEvent;
//import org.junit.Test;
//import org.junit.jupiter.api.Assertions;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.web.server.LocalServerPort;
//import org.springframework.http.*;
//import org.springframework.kafka.test.context.EmbeddedKafka;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.net.URISyntaxException;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@EmbeddedKafka(topics = {"library-event"}, partitions = 3)
//@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
//        "spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}"})
//public class LibrarayEventsControllerIntegrationTest {
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//    @LocalServerPort
//    private int port;
//
//    @Test
//    public void postLibraryEvent() throws URISyntaxException {
//        //given
//        Book book = Book.builder()
//                .bookId(123)
//                .bookName("Learn Kafka Integration")
//                .bookAuthor("Sandeep")
//                .build();
//        LibraryEvent libraryEvent = LibraryEvent.builder()
//                .libraryEventId(234).book(book).build();
//        HttpHeaders httpHeadders = new HttpHeaders();
//        httpHeadders.set("content-type", MediaType.APPLICATION_JSON_VALUE);
//
//        HttpEntity<LibraryEvent> request = new HttpEntity<>(libraryEvent, httpHeadders);
//        //when
//        //URI uri = new URI("/v1/library-event");
//        ResponseEntity<LibraryEvent> responseEntity = restTemplate.exchange(createURLWithPort("/v1/library-event"), HttpMethod.POST, request, LibraryEvent.class);
//        //then
//        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
//    }
//
//    private String createURLWithPort(String uri) {
//
//        System.out.println("http://localhost:" + port + uri);
//        return "http://localhost:" + port + uri;
//    }
//}
