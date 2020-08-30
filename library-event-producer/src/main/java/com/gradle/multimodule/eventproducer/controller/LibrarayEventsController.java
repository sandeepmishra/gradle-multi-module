package com.gradle.multimodule.eventproducer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gradle.multimodule.eventproducer.domain.LibraryEvent;
import com.gradle.multimodule.eventproducer.domain.LibraryEventType;
import com.gradle.multimodule.eventproducer.producer.LibraryEventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/library-event")
public class LibrarayEventsController {

    @Autowired
    LibraryEventProducer libraryEventProducer;
    @PostMapping
    public ResponseEntity<LibraryEvent> addLibraryEvent(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException {
        libraryEvent.setLibraryEventType(LibraryEventType.NEW);
        libraryEventProducer.sendLibraryEvent(libraryEvent);
        return new ResponseEntity<>(libraryEvent , HttpStatus.CREATED);
    }

}
