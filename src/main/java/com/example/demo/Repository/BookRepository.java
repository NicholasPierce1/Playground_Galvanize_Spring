package com.example.demo.Repository;


import com.example.demo.domain.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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

    public @NotNull Book saveBook(@NotNull final Book toSave){
        return this._mongoTemplate.insert(toSave, Book.BOOK_COLLECTION_NAME);
    }

    public @NotNull List<Book> findAllBooks(){
        return this._mongoTemplate.findAll(Book.class, Book.BOOK_COLLECTION_NAME);
    }

    public @NotNull List<Book> findByCriteria(@NotNull final Map<String, ?> criteria){

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

    public @Nullable Book deleteBookById(@NotNull final String id){

        final Query idQuery = new Query()
                .addCriteria(Criteria.where("_id").is(id));

        return this._mongoTemplate.findAndRemove(idQuery, Book.class, Book.BOOK_COLLECTION_NAME);

    }

    public @Nullable List<Book> deleteBookByCriteria(@NotNull final Map<String, ?> criteria){

        final Query query = new Query();

        for(String key : criteria.keySet())
            query.addCriteria(
                    Criteria.where(key).is(criteria.get(key))
            );

        return this._mongoTemplate.findAllAndRemove(query, Book.class, Book.BOOK_COLLECTION_NAME);

    }

    public long updateBookById(@NotNull final String id, @NotNull final Map<String, ?> updateState){

        final Update update = new Update();

        updateState.forEach(update::set);

        final Query idQuery = new Query()
                .addCriteria(Criteria.where("_id").is(id));

        return this._mongoTemplate.upsert(idQuery, update, Book.class, Book.BOOK_COLLECTION_NAME).getMatchedCount();

    }

    public long updateBooksByCriteria(@NotNull final Map<String, ?> criteria, @NotNull final Map<String, ?> updateState){
        final Update update = new Update();

        updateState.forEach(update::set);

        final Query query = new Query();

        for(String key : criteria.keySet())
            query.addCriteria(
                    Criteria.where(key).is(criteria.get(key))
            );

        return this._mongoTemplate.updateMulti(query, update, Book.class, Book.BOOK_COLLECTION_NAME).getMatchedCount();
    }

}
