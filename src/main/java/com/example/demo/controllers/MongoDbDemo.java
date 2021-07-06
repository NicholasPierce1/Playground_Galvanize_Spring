package com.example.demo.controllers;

import com.example.demo.model.TestDatabaseCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mongoDb/")
public class MongoDbDemo {

    @Autowired
    private MongoTemplate _mongoTemplate;

    @GetMapping(
            value = {"/getAll", "/getAll/"},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Iterable<TestDatabaseCollection> getAllStuff(){
        return this._mongoTemplate.findAll(TestDatabaseCollection.class);
    }

}
