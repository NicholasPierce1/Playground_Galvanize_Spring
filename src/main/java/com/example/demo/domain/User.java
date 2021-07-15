package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.apache.tomcat.jni.Address;

import javax.persistence.*;
import java.util.Arrays;

@Entity(name = "user")
@NamedEntityGraph(
        name = "UserEntityGraph",
        attributeNodes = {
                @NamedAttributeNode( value = "address")
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    //...

    private String name;

    @OneToOne(cascade = CascadeType.ALL, targetEntity = com.example.demo.domain.Address.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    @JsonManagedReference
    private Address address;

    public User(Object user, Object address){
        User user1 = (User) user;
        Address address1 = (Address) address;
        this.address = user1.address;
        this.id = user1.id;
        this.name = user1.name;
    }

    public User(Object... objects){
        Arrays.stream(objects).forEach(System.out::println);
    }

    public User(){}

    // ... getters and setters

    public Long getId() {
        return id;
    }

    public Address getAddress() {
        return address;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(Object obj){
        System.out.println("called");
    }

    @Override
    public String toString(){
        return this.name;
    }
}
