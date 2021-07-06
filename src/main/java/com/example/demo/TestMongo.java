package com.example.demo;

import com.example.demo.model.TestDatabaseCollection;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.mongodb.ServerAddress;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import java.util.Arrays;
import com.mongodb.Block;

import com.mongodb.client.MongoCursor;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TestMongo implements CommandLineRunner {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(mongoTemplate != null);
        System.out.println("Collection Exists? " + mongoTemplate.collectionExists("testDatabaseCollection"));

        final ObjectMapper objectMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();

        final MongoCollection<Document> collection =  mongoTemplate.getCollection("testDatabaseCollection");
        final Query query = new Query(Criteria.where("name").is("Bob"));
        for(final TestDatabaseCollection doc : mongoTemplate.findAll(TestDatabaseCollection.class)) {
            System.out.println(doc.get_id());

            if(doc.getName().equals("Bob")){

                doc.setName("Bobby");

                final Update update = new Update();

                Map<String,Object> map = (Map<String, Object>)objectMapper.convertValue(doc, Map.class);
                // System.out.println(map);

                map.forEach(update::set);

                System.out.println(update);

                mongoTemplate.updateFirst(query, update, TestDatabaseCollection.class);

            }

        }

        //System.out.println(this.mongoTemplate.getCollection("testDatabaseCollection").getNamespace().getCollectionName());

//        final MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, "testDatabase");
//        System.out.println("Collection Exists? " + mongoTemplate.collectionExists("testDatabaseCollection"));
    }

}
