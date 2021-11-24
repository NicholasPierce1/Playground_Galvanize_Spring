package com.example.demo.model;

public class TestBeanModel {

    private String successString;

    public void printSuccess(){
        System.out.println(this.successString);
    }

    public String getSuccessString() {
        return successString;
    }

    public void setSuccessString(String successString) {
        this.successString = successString;
    }
}
