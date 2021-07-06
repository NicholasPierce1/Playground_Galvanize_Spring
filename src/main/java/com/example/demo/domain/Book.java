package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class Book {

    @Id
    private String _id;

    @JsonProperty("Title")
    @Field("Title")
    private String _title;

    @JsonProperty("Author")
    @Field("Author")
    private String _author;

    @JsonProperty("Length")
    @Field("Length")
    private double _length;

    @JsonProperty("Price")
    @Field("Price")
    private double _price;

    public static String  BOOK_COLLECTION_NAME = "Book";

    public String get_id() {
        return _id;
    }

    public double get_price() {
        return _price;
    }

    public double get_length() {
        return _length;
    }

    public String get_author() {
        return _author;
    }

    public String get_title() {
        return _title;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void set_author(String _author) {
        this._author = _author;
    }

    public void set_length(double _length) {
        this._length = _length;
    }

    public void set_price(double _price) {
        this._price = _price;
    }

    public void set_title(String _title) {
        this._title = _title;
    }
}
