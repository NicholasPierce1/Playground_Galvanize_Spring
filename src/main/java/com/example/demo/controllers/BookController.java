package com.example.demo.controllers;

import com.example.demo.Repository.BookRepository;
import com.example.demo.domain.Book;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/book/")
public class BookController {

    @Autowired
    private BookRepository _bookRepository;

    @RequestMapping(
            value = {"/save", "/save/"},
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Book saveBook(@NotNull final @RequestBody Book toSave){
        return this._bookRepository.saveBook(toSave);
    }

    @RequestMapping(
            value = "/",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Book> getBooks(){
        return this._bookRepository.findAllBooks();
    }

    @RequestMapping(
            value = {"/findBy", "/findBy/"},
            method = RequestMethod.GET,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Book> getBookByCriteria(@NotNull final @RequestBody Map<String, ?> criteria){
        return this._bookRepository.findByCriteria(criteria);
    }

    @RequestMapping(
    value = "/delete/{id}",
    method = RequestMethod.DELETE,
    produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @Nullable Book deleteBookById(@NotNull final @PathVariable String id){
        return this._bookRepository.deleteBookById(id);
    }

    @RequestMapping(
            value = {"/deleteBy", "/deleteBy/"},
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Book> deleteBookByCriteria(@NotNull final @RequestBody Map<String, ?> criteria){
        return this._bookRepository.deleteBookByCriteria(criteria);
    }

    @RequestMapping(
            value = "/update/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public long updateBookById(
            @NotNull final @PathVariable String id,
            @NotNull final @RequestBody Map<String, ?> updateState){
        return this._bookRepository.updateBookById(id, updateState);
    }

    @RequestMapping(
            value = {"/updateBy/", "/updateBy"},
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public long updateBookByCriteria(
            @NotNull final @RequestBody Map<String, ? extends Map<String, ?>> inputState){
        return this._bookRepository.updateBooksByCriteria(
                inputState.get("criteria"),
                inputState.get("updateState")
        );
    }
}


