package com.gradle.multimodule.eventconsumer.consumer;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Timeout;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = {"library-events"}, partitions = 1)
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
"spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}"})
public class LibraryEventConsumerIntegrationTest {

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    KafkaTemplate<Integer, String> kafkaTemplate;

    @Autowired
    KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @SpyBean
    LibraryEventConsumer libraryEventConsumer;

    @BeforeEach
    void setUp() {
        for (MessageListenerContainer listenerContainer: kafkaListenerEndpointRegistry.getAllListenerContainers()){
            ContainerTestUtils.waitForAssignment(listenerContainer, embeddedKafkaBroker.getPartitionsPerTopic());
        }
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void publishNewLibraryEvent() throws ExecutionException, InterruptedException {

        //given
        String json  = "{\"libraryEventId\":null,\"book\":{\"bookId\":123,\"bookName\":\"Learn Kafka Integration\",\"bookAuthor\":\"Sandeep\"},\"libraryEventType\":\"NEW\"}";
        kafkaTemplate.sendDefault(json).get();
        //when
        CountDownLatch countDownLatch= new CountDownLatch(1);
        countDownLatch.await(3, TimeUnit.SECONDS);
        //then

        verify(libraryEventConsumer, times(1)).onMessage(isA(ConsumerRecord.class));
    }
}
