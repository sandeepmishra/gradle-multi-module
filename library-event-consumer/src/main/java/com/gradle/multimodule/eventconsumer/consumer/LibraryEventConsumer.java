package com.gradle.multimodule.eventconsumer.consumer;


import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class LibraryEventConsumer {

    @KafkaListener(topics = "library-events")
    public void onMessage(ConsumerRecord<Integer, String> consumerRecord){
        log.info("Consumer record: {} ", consumerRecord);
    }

    // implements AcknowledgingMessageListener<Integer, String>
//    @Override
//    @KafkaListener(topics = "library-events")
//    public void onMessage(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment) {
//        log.info("Consumer record: {} ", consumerRecord);
//        acknowledgment.acknowledge();
//
//    }


}
