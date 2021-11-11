package com.example.demo.domain;
import org.apache.tomcat.jni.Address;

import javax.persistence.*;

@Entity(name = "user_custom")
public class UserCustom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    //...

    private String name;

    private Long address;

    // ... getters and setters

    public Long getId() {
        return id;
    }

    public Long getAddress() {
        return address;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAddress(Long address_id) {
        this.address = address_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

