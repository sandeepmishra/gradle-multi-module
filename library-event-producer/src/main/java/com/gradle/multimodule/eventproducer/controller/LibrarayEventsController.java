package com.gradle.multimodule.eventproducer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gradle.multimodule.eventproducer.domain.LibraryEvent;
import com.gradle.multimodule.eventproducer.domain.LibraryEventType;
import com.gradle.multimodule.eventproducer.producer.LibraryEventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/library-event")
public class LibrarayEventsController {

    @Autowired
    LibraryEventProducer libraryEventProducer;
    @PostMapping
    public ResponseEntity<LibraryEvent> addLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) throws JsonProcessingException {
        libraryEvent.setLibraryEventType(LibraryEventType.NEW);
        libraryEventProducer.sendLibraryEventAsynchronous_2(libraryEvent);
        return new ResponseEntity<>(libraryEvent , HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) throws JsonProcessingException {
        if(libraryEvent.getLibraryEventId() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please pass the library id");
        }

        libraryEvent.setLibraryEventType(LibraryEventType.UPDATE);
        libraryEventProducer.sendLibraryEvent(libraryEvent);
        return new ResponseEntity<>(libraryEvent , HttpStatus.OK);
    }

}
