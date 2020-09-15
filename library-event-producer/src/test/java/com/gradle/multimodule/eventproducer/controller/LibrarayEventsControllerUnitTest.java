package com.gradle.multimodule.eventproducer.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gradle.multimodule.eventproducer.domain.Book;
import com.gradle.multimodule.eventproducer.domain.LibraryEvent;
import com.gradle.multimodule.eventproducer.producer.LibraryEventProducer;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.isA;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
//@ExtendWith(MockitoExtension.class)
@WebMvcTest(LibrarayEventsController.class)
@AutoConfigureMockMvc
public class LibrarayEventsControllerUnitTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibraryEventProducer libraryEventProducer;

    private ObjectMapper objectMapper;

    @Test
    public void testEventLibraryPost() throws Exception {
        objectMapper = new ObjectMapper();
        Book book = Book.builder()
                .bookId(123)
                .bookName("Learn Kafka Integration")
                .bookAuthor("Sandeep")
                .build();
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(234).book(book).build();

        Mockito.doNothing().when(libraryEventProducer).sendLibraryEvent(isA(LibraryEvent.class));
        mockMvc.perform(MockMvcRequestBuilders.post("/library-event")
        .content(objectMapper.writeValueAsString(libraryEvent)).contentType(MediaType.APPLICATION_JSON))
        .andDo(print()).andExpect(status().isCreated());
    }


    @Test
    public void testEventLibraryPost_4xx() throws Exception {
        objectMapper = new ObjectMapper();
        Book book = Book.builder()
                .bookId(null)
                .bookName("Learn Kafka Integration")
                .bookAuthor("Sandeep")
                .build();
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(234).book(book).build();

        Mockito.doNothing().when(libraryEventProducer).sendLibraryEvent(isA(LibraryEvent.class));
        mockMvc.perform(MockMvcRequestBuilders.post("/library-event")
                .content(objectMapper.writeValueAsString(libraryEvent)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is4xxClientError()).andExpect(content().string("[{\"codes\":[\"NotNull.libraryEvent.book.bookId\",\"NotNull.book.bookId\",\"NotNull.bookId\",\"NotNull.java.lang.Integer\",\"NotNull\"],\"arguments\":[{\"codes\":[\"libraryEvent.book.bookId\",\"book.bookId\"],\"arguments\":null,\"defaultMessage\":\"book.bookId\",\"code\":\"book.bookId\"}],\"defaultMessage\":\"must not be null\",\"objectName\":\"libraryEvent\",\"field\":\"book.bookId\",\"rejectedValue\":null,\"bindingFailure\":false,\"code\":\"NotNull\"}]"));
    }
}