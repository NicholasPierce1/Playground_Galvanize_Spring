package com.example.demo.domain.JsonMapperTestPackage;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(value = {"test_3"}, ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class JsonMapperTest {

    @JsonProperty(value = "test_1")
    private String test1;

    @JsonProperty(value = "my_test_2")
    private double myTest2;

    @JsonProperty(value = "test_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonProperty(value = "sub_class")
    private SubClass subClass;

    @JsonProperty(value = "sub_class_list")
    private List<SubClass> subClassList;

    @JsonProperty(value = "class_a")
    private ClassA classA;

    @JsonProperty(value = "class_a_list")
    private List<ClassA> classAList;

    public JsonMapperTest(){}

    public double getMyTest2() {
        return myTest2;
    }

    public String getTest1() {
        return test1;
    }

    public LocalDate getDate() {
        return date;
    }

    public SubClass getSubClass() {
        return subClass;
    }

    public List<SubClass> getSubClassList() {
        return subClassList;
    }

    public ClassA getClassA() {
        return classA;
    }

    public List<ClassA> getClassAList() {
        return classAList;
    }

    public void setClassAList(List<ClassA> classAList) {
        this.classAList = classAList;
    }

    public void setClassA(ClassA classA) {
        this.classA = classA;
    }

    public void setSubClassList(List<SubClass> subClassList) {
        this.subClassList = subClassList;
    }

    public void setMyTest2(double myTest2) {
        this.myTest2 = myTest2;
    }

    public void setTest1(String test1) {
        this.test1 = test1;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setSubClass(SubClass subClass) {
        this.subClass = subClass;
    }

    public static class SubClass{

        @JsonProperty(value = "my_string")
        public String myString;

        @JsonProperty(value = "my_string_list")
        public List<String> myStringList;

        public SubClass(){
        }

        public String getMyString() {
            return myString;
        }

        public List<String> getMyStringList() {
            return myStringList;
        }

        public void setMyStringList(List<String> myStringList) {
            this.myStringList = myStringList;
        }

        public void setMyString(String myString) {
            this.myString = myString;
        }
    }
}
