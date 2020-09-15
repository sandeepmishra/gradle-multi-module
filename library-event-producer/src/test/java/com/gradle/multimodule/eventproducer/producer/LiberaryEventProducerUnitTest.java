package com.gradle.multimodule.eventproducer.producer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gradle.multimodule.eventproducer.domain.Book;
import com.gradle.multimodule.eventproducer.domain.LibraryEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SettableListenableFuture;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class LiberaryEventProducerUnitTest {


    @InjectMocks
    private LibraryEventProducer libraryEventProducer;

    @Mock
    private KafkaTemplate<Integer, String> kafkaTemplate;;

    @Spy
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testSendLiberaryEvent_OnFailuer() throws JsonProcessingException, ExecutionException, InterruptedException {
        Book book = Book.builder()
                .bookId(null)
                .bookName("Learn Kafka Integration")
                .bookAuthor("Sandeep")
                .build();
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(234).book(book).build();
        SettableListenableFuture future = new SettableListenableFuture();
        future.setException(new RuntimeException("Exception occured while sending message"));
        when(kafkaTemplate.send(isA(ProducerRecord.class))).thenReturn(future);
        assertThrows(Exception.class, ()-> libraryEventProducer.sendLibraryEventAsynchronous_2(libraryEvent).get());
    }


    @Test
    public void testSendLiberaryEvent_OnSuccess() throws JsonProcessingException, ExecutionException, InterruptedException {
        Book book = Book.builder()
                .bookId(null)
                .bookName("Learn Kafka Integration")
                .bookAuthor("Sandeep")
                .build();
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(234).book(book).build();
        SettableListenableFuture future = new SettableListenableFuture();
        ObjectMapper mapper = new ObjectMapper();
        ProducerRecord<Integer, String> producerRecord = new ProducerRecord<>("library-events", libraryEvent.getLibraryEventId(), mapper.writeValueAsString(libraryEvent));
        RecordMetadata recordMetadata = new RecordMetadata(new TopicPartition("library-events",1), 1, 1 ,434, System.currentTimeMillis(),1,2);

        SendResult<Integer, String> sendResult = new SendResult<>(producerRecord,recordMetadata );
        future.set(sendResult);
        when(kafkaTemplate.send(isA(ProducerRecord.class))).thenReturn(future);
        SendResult<Integer, String> sendResult1 = libraryEventProducer.sendLibraryEventAsynchronous_2(libraryEvent).get();
        assertTrue(sendResult1.getRecordMetadata().partition()==1);
    }
}
