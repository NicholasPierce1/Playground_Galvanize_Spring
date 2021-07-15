package com.example.demo.Repository;

import com.example.demo.domain.User;

import java.util.List;

public interface UserGraphRepository {

    public List<User> findAllCustom();

}
