package com.example.demo.Repository;


import com.example.demo.domain.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Repository
public class BookRepository {

    @Autowired
    private ObjectMapper _objectMapper;

    @Autowired
    private MongoTemplate _mongoTemplate;

    public Book saveBook(final Book toSave){
        return this._mongoTemplate.insert(toSave, Book.BOOK_COLLECTION_NAME);
    }

    public List<Book> findAllBooks(){
        return this._mongoTemplate.findAll(Book.class, Book.BOOK_COLLECTION_NAME);
    }

    public List<Book> findByCriteria(final Map<String, ?> criteria){

        final Query query = new Query();

//        criteria.forEach(
//                (key, value) ->
//                        query.addCriteria(
//                                Criteria.where(key).is(value)
//                        )
//        );
        System.out.println(criteria);
        for(String key : criteria.keySet())
            query.addCriteria(
                    Criteria.where(key).is(criteria.get(key))
            );

        //query.addCriteria(Criteria.where("Author").is("Author2"));

        System.out.println(query);

        final List<Book> returnType = this._mongoTemplate.find(query, Book.class, Book.BOOK_COLLECTION_NAME);

        System.out.println(returnType);

        return returnType;
    }

}
