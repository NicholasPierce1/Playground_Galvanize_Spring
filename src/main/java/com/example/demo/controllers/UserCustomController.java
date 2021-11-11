package com.example.demo.controllers;

import com.example.demo.Repository.UserCustomRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.domain.User;
import com.example.demo.domain.UserCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/userCustom/")
public class UserCustomController {

    @Autowired
    private UserCustomRepository _userCustomRepository;

    @RequestMapping(
            value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public boolean getUsers(){
        this._userCustomRepository.getAllUsers();
        return true;
    }

    @RequestMapping(
            value= {"/getAll", "/getAll/"},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Iterable<UserCustom> getAllUsers(){
        return this._userCustomRepository.findAll();
    }

    @RequestMapping(
            value = {"/test", "/test/"},
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET
    )
    public boolean testUsers() throws Exception {
        this._userCustomRepository.testJDBC();
        return true;
    }

}
