package com.gradle.multimodule.eventproducer.controller;


import com.gradle.multimodule.eventproducer.domain.Book;
import com.gradle.multimodule.eventproducer.domain.LibraryEvent;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

//@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = {"library-events"}, partitions = 1)
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}"})
public class LibrarayEventsControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    Consumer<Integer, String> consumer;

    @LocalServerPort
    private int port;

    @Before
    public void setup(){
//        KafkaEmbedded kafkaEmbedded = new KafkaEmbedded(1, false, 1, "test_topic");
//        kafkaEmbedded.setKafkaPorts(9092);
        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.consumerProps("group", "true", embeddedKafkaBroker));
        consumer = new DefaultKafkaConsumerFactory<>(configs, new IntegerDeserializer(), new StringDeserializer()).createConsumer();
        embeddedKafkaBroker.consumeFromAllEmbeddedTopics(consumer);
    }

    @AfterEach
    public void tearDown(){
        consumer.close();
    }

    @Test
    @Timeout(10) // to test async call
    public void postLibraryEvent() throws URISyntaxException, InterruptedException {
        //given
        Book book = Book.builder()
                .bookId(123)
                .bookName("Learn Kafka Integration")
                .bookAuthor("Sandeep")
                .build();
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(234).book(book).build();
        HttpHeaders httpHeadders = new HttpHeaders();
        httpHeadders.set("content-type", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<LibraryEvent> request = new HttpEntity<>(libraryEvent, httpHeadders);
        //when
        //URI uri = new URI("/v1/library-event");
        ResponseEntity<LibraryEvent> responseEntity = restTemplate.exchange(createURLWithPort("/v1/library-event"), HttpMethod.POST, request, LibraryEvent.class);
        //then
        //Thread.sleep(10000);
        ConsumerRecord<Integer, String> consumerRecord= KafkaTestUtils.getSingleRecord(consumer, "library-events");
        String value = consumerRecord.value();
        String expectedValue ="{\"libraryEventId\":234,\"book\":{\"bookId\":123,\"bookName\":\"Learn Kafka Integration\",\"bookAuthor\":\"Sandeep\"},\"libraryEventType\":\"NEW\"}";
        Assertions.assertEquals(value, expectedValue);
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    private String createURLWithPort(String uri) {

        System.out.println("http://localhost:" + port + uri);
        return "http://localhost:" + port + uri;
    }
}
