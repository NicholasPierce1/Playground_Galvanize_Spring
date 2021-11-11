package com.example.demo.domain;

import javax.persistence.*;

@Entity
@Table(name = "address_custom")
public class AddressCustom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;
    //...

    private String addressPath;

    @Transient
    private User user;

    //... getters and setters

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAddressPath() {
        return addressPath;
    }

    public void setAddressPath(String addressPath) {
        this.addressPath = addressPath;
    }
}

