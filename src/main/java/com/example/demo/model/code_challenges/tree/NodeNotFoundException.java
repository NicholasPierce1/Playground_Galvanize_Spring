package com.example.demo.model.code_challenges.tree;

public class NodeNotFoundException extends RuntimeException{

    public <T extends Comparable<T>> NodeNotFoundException(T valueToFind){
        super(String.format("Could not find node with the value of (%s).", valueToFind.toString()));
    }

}
