package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "testDatabaseCollection")
public class TestDatabaseCollection {

    @Id
    @JsonProperty(value = "_id")
    private String _id;

    @JsonProperty(value = "name")
    private String name;

    public String getName() {
        return name;
    }

    public String get_id() {
        return _id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
