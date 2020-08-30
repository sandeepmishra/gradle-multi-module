package com.gradle.multimodule.eventconsumer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gradle.multimodule.eventconsumer.domain.LibraryEvent;
import com.gradle.multimodule.eventconsumer.domain.LibraryEventType;
import com.gradle.multimodule.eventconsumer.producer.LibraryEventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/library-event")
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
