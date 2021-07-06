package com.example.demo.controllers;

import com.example.demo.Repository.BookRepository;
import com.example.demo.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public Book saveBook(final @RequestBody Book toSave){
        return this._bookRepository.saveBook(toSave);
    }

}
