package com.example.demo.controllers;

import com.example.demo.Repository.UserRepository;
import com.example.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/user/")
public class UserController {

    @Autowired
    private UserRepository _userRepository;

    @RequestMapping(
            value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<User> getUsers(){
        final List<User> users = new ArrayList<User>();
        this._userRepository.findAllCustom().forEach(users::add);
        return users;
    }

}
