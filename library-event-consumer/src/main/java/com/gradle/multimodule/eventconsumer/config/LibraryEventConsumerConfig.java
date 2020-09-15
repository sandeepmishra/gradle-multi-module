package com.gradle.multimodule.eventconsumer.config;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@Slf4j
public class LibraryEventConsumerConfig {


    // when controlling acknowledgement mechanism on our own need to override
    // kafka listener factory bean and then implement implements AcknowledgingMessageListener<Integer, String> into Event consumer class
    // override method which takes Acknowledgement as parameter

    //    @Override
//    @KafkaListener(topics = "library-events")
//    public void onMessage(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment) {
//        log.info("Consumer record: {} ", consumerRecord);
//        acknowledgment.acknowledge();
//
//    }



    @Bean
    KafkaProperties buildConsumerProperties(){
        return new KafkaProperties();
    }
//
    @Bean
    //@ConditionalOnMissingBean(name = "kafkaListenerContainerFactory")
    ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
            ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
            ObjectProvider<ConsumerFactory<Object, Object>> kafkaConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        configurer.configure(factory, kafkaConsumerFactory
                .getIfAvailable(() -> new DefaultKafkaConsumerFactory<>(this.buildConsumerProperties().buildConsumerProperties())));
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.setConcurrency(3); // to set concurrency for kafka listener instances...
        // for implementing error handiling correctly can use ErrorHandler kafa class
        // this way of error handling can be handy instead of using existing in scenarios like when we need to
        // perform some extra operation on failure such that persisting failed records
        factory.setErrorHandler(((thrownException, data) -> {
            log.info("Exception in consumerConfig is {} and the record is {}", thrownException.getMessage(), data);
        }));


        // retry options
        factory.setRetryTemplate(retryTemplate());


        // recoverable logic
        factory.setRecoveryCallback(context -> {
            if(context.getLastThrowable().getCause() instanceof RecoverableDataAccessException){
                //do recovery
                Arrays.asList(context.attributeNames()).forEach(attributeName ->{
                    log.info("Attribute Name is {} ",attributeName);
                    log.info("Attribute Value is {} ",context.getAttribute(attributeName));
                });

                ConsumerRecord<Integer, String> consumerRecord = (ConsumerRecord<Integer, String>)context.getAttribute("record");
            }else{
                throw new RuntimeException(context.getLastThrowable().getMessage());
            }
            return  null;
        });
        return factory;
    }

    private RetryTemplate retryTemplate() {
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(1000);// 1second
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(simpleRetryPolicy());
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
        return retryTemplate;
    }

    private RetryPolicy simpleRetryPolicy() {
        Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<>();
        // put those Exceptions which dont want to retry as false and true for which want to retry
        retryableExceptions.put(IllegalArgumentException.class, false);
        retryableExceptions.put(RecoverableDataAccessException.class, true);
        SimpleRetryPolicy simpleRetryPolicy= new SimpleRetryPolicy(3, retryableExceptions);
        //simpleRetryPolicy.setMaxAttempts(3);
        return simpleRetryPolicy;
    }


}
