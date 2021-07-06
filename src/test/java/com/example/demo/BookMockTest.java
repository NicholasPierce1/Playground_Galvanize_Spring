package com.example.demo;

import com.example.demo.controllers.BookController;
import com.example.demo.domain.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class BookMockTest {

    @Mock
    private BookController _bookController;

    @Autowired
    private ObjectMapper _objectMapper;

    @Test
    public void testAddBook() throws Exception{

        final Book bookToSave = new Book(){{
            set_author("Power of Un");
            set_id("60e4b52d695d9555246071f8");
            set_length(150);
            set_price(125.5);
        }};

        when(this._bookController.saveBook(any(Book.class)))
                .thenReturn(
                        new Book(){{
                            set_author("Power of Un");
                            set_id("60e4b52d695d9555246071f8");
                            set_length(150);
                            set_price(125.5);
                        }}
                );

        assertEquals(
                this._objectMapper.writeValueAsString(this._bookController.saveBook(bookToSave)),
                this._objectMapper.writeValueAsString(bookToSave)
                );

        verify(this._bookController, times(1)).saveBook(any(Book.class));
        verifyNoMoreInteractions(this._bookController);

    }

}
