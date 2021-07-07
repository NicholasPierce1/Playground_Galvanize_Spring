package com.example.demo;

import com.example.demo.controllers.BookController;
import com.example.demo.domain.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.in;
import static org.assertj.core.api.InstanceOfAssertFactories.MAP;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Test
    public void testGetAllBooks() throws Exception{

        final Book bookInventory = new Book(){{
            set_author("Power of Un");
            set_id("60e4b52d695d9555246071f8");
            set_length(150);
            set_price(125.5);
        }};

        final List<Book> expected = List.of(bookInventory);

        when(this._bookController.getBooks()).thenReturn(List.of(bookInventory));

        assertEquals(
                this._objectMapper.writeValueAsString(expected),
                this._objectMapper.writeValueAsString(this._bookController.getBooks())
        );

        verify(this._bookController, times(1)).getBooks();
        verifyNoMoreInteractions(this._bookController);

    }

    @Test
    public void testGetBooksByCriteria() throws Exception{

        final List<Book> expectedBookList = List.of(
                new Book(){{
                    set_id("60e4c2b333e8d442aa182828");
                    set_price(9915.5);
                    set_author("Author1");
                    set_title("Book1");
                    set_length(10.0);
                }},
                new Book(){{
                    set_id("60e4c2f333e8d442aa182829");
                    set_price(9915.5);
                    set_author("Author4");
                    set_title("Book4");
                    set_length(10.0);
                }}
        );

        final HashMap<String, ?> inputs = new HashMap<String, Object>(){{
            put("Length", 10.0);
            put("Price", 9915.5);
        }};

        when(this._bookController.getBookByCriteria(inputs)).thenReturn(expectedBookList);

        assertEquals(
                this._objectMapper.writeValueAsString(expectedBookList),
                this._objectMapper.writeValueAsString(this._bookController.getBookByCriteria(inputs))
        );

        verify(this._bookController, times(1)).getBookByCriteria(inputs);
        verifyNoMoreInteractions(this._bookController);
    }

    @Test
    public void testDeleteById() throws Exception {

        final String idToDelete = "60e4c2b333e8d442aa182828";

        final Book expectedBookToDelete = new Book(){{
            set_id("60e4c2b333e8d442aa182828");
            set_price(9915.5);
            set_author("Author1");
            set_title("Book1");
            set_length(10.0);
        }};

        when(this._bookController.deleteBookById(idToDelete))
                .thenReturn(expectedBookToDelete);

        assertEquals(
                this._objectMapper.writeValueAsString(expectedBookToDelete),
                this._objectMapper.writeValueAsString(this._bookController.deleteBookById(idToDelete))
        );

        verify(this._bookController, times(1)).deleteBookById(idToDelete);
        verifyNoMoreInteractions(this._bookController);

    }

    @Test
    public void testDeleteByCriteria() throws Exception{

        final List<Book> expectedBookList = List.of(
                new Book(){{
                    set_id("60e4c2b333e8d442aa182828");
                    set_price(9915.5);
                    set_author("Author1");
                    set_title("Book1");
                    set_length(10.0);
                }},
                new Book(){{
                    set_id("60e4c2f333e8d442aa182829");
                    set_price(9915.5);
                    set_author("Author4");
                    set_title("Book4");
                    set_length(10.0);
                }}
        );

        final HashMap<String, ?> inputs = new HashMap<String, Object>(){{
            put("Length", 10.0);
            put("Price", 9915.5);
        }};

        when(this._bookController.deleteBookByCriteria(inputs)).thenReturn(expectedBookList);

        assertEquals(
                this._objectMapper.writeValueAsString(expectedBookList),
                this._objectMapper.writeValueAsString(this._bookController.deleteBookByCriteria(inputs))
        );

        verify(this._bookController, times(1)).deleteBookByCriteria(inputs);
        verifyNoMoreInteractions(this._bookController);
    }

    @Test
    public void testUpdateById() throws Exception{

        final String id = "60e4c2b333e8d442aa182828";

        final Map<String, ?> updateState = new HashMap<>(){{
            put("Length", 150);
        }};

        when(this._bookController.updateBookById(id, updateState)).thenReturn(1L);

        assertEquals(1, this._bookController.updateBookById(id, updateState));

        verify(this._bookController, times(1)).updateBookById(id, updateState);
        verifyNoMoreInteractions(this._bookController);

    }

    @Test
    public void testUpdateByCriteria() throws Exception{

        final Map<String, HashMap<String, Object>> inputState = new HashMap<String, HashMap<String, Object>>(){{
            put(
                    "criteria",
                    new HashMap<String, Object>(){{
                        put("Length", 150);
                    }}
            );
            put(
                    "updateState",
                    new HashMap<String, Object>(){{
                        put("Length", 500);
                    }}
            );
        }};

        when(this._bookController.updateBookByCriteria(inputState)).thenReturn(2L);

        assertEquals(2, this._bookController.updateBookByCriteria(inputState));

        verify(this._bookController, times(1)).updateBookByCriteria(inputState);
        verifyNoMoreInteractions(this._bookController);

    }

}
