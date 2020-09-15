package com.gradle.multimodule.eventproducer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class LibraryEventControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validateRequestBody(MethodArgumentNotValidException obj){
        List<FieldError> fieldErrors = obj.getBindingResult().getFieldErrors();
        fieldErrors.stream().map(field->field.getField()+" - "+ field.getDefaultMessage())
                .sorted().collect(Collectors.joining(", "));
        log.info("errorMessage: {}", fieldErrors);
        return new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
    }
}
