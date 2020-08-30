package com.gradle.multimodule.eventproducer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gradle.multimodule.eventproducer.domain.LibraryEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class LibraryEventProducer {

    final String TOPIC = "library-events";

    @Autowired
    KafkaTemplate<Integer, String> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;

    // Asynchronous way of sending message
    public void sendLibraryEvent(LibraryEvent libraryEvent) throws JsonProcessingException {

        Integer key =libraryEvent.getLibraryEventId();
        String value=objectMapper.writeValueAsString(libraryEvent);

        ListenableFuture<SendResult<Integer, String>> sendResultListenableFuture = kafkaTemplate.sendDefault(key, value);
        sendResultListenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailuer(key, value, ex);
            }

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                handleSuccess(key, value, result);
            }
        });
    }


    // Asynchronous way of sending message
    public void sendLibraryEventAsynchronous_2(LibraryEvent libraryEvent) throws JsonProcessingException {

        Integer key =libraryEvent.getLibraryEventId();
        String value=objectMapper.writeValueAsString(libraryEvent);

        ProducerRecord<Integer, String > producerRecord = buildProducerRecord(TOPIC, key, value);
        ListenableFuture<SendResult<Integer, String>> sendResultListenableFuture = kafkaTemplate.send(producerRecord);
        sendResultListenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailuer(key, value, ex);
            }

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                handleSuccess(key, value, result);
            }
        });
    }

    private ProducerRecord<Integer, String > buildProducerRecord(String topic, Integer key, String value){
        // passing header to procedure record -- if we want to pass some extra value
        List<Header> headers = List.of(new RecordHeader("event-source", "scanner".getBytes()));
        return new ProducerRecord<Integer, String>(topic, null, key, value, null);
    }

    public SendResult<Integer, String> sendLibraryEventSynchronous(LibraryEvent libraryEvent) throws JsonProcessingException {
        Integer key =libraryEvent.getLibraryEventId();
        String value=objectMapper.writeValueAsString(libraryEvent);

        SendResult<Integer, String> sendResult = null;

        try {
            sendResult = kafkaTemplate.sendDefault(key, value).get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("InterruptedException/ExecutionException exception while sending message {}", e.getMessage());
        } catch (Exception e) {
            log.error("Exception exception while sending message {}", e.getMessage());
        }

        return sendResult;
    }
    private void handleSuccess(Integer key, String value, SendResult<Integer, String> result){
        log.info("Message sent successfully for the key : {} and the value is {}, partition is {}", key, value, result.getRecordMetadata().partition());
    }

    private void handleFailuer(Integer key, String value, Throwable ex){
        log.error("Error sending message and exception is {}", ex.getMessage());
        log.error("Error on Failure: {}", ex.getMessage());
    }
}
