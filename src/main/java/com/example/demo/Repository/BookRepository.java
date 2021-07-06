package com.example.demo.Repository;


import com.example.demo.domain.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepository {

    @Autowired
    private ObjectMapper _objectMapper;

    @Autowired
    private MongoTemplate _mongoTemplate;

    public Book saveBook(final Book toSave){
        return this._mongoTemplate.insert(toSave, Book.BOOK_COLLECTION_NAME);
    }

}
