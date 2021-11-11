package com.example.demo.model;

import com.fasterxml.jackson.annotation.*;
import org.json.JSONArray;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlightInfo {

    private FlightInfo(){}

//    @JsonProperty(required = true)
    private String landsAt = "Tampa Bay International";

    @JsonProperty(required = false)
    private double capacity = 100;

    @JsonProperty("safe")
    private final boolean isSafe = true;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate myDate = LocalDate.of(2000, 10, 10);

    @JsonIgnore()
    private String secretInfo = "is actually not safeeee";

//    @JsonProperty(value = "myArray", required = true, defaultValue = "[\"hello\", \"world\"]")
    private String[] myArray = {"hello?"};

    public boolean isSafe() {
        return isSafe;
    }

    public double getCapacity() {
        return capacity;
    }

    @JsonFormat(pattern = "yyyy-dd-MM")
    public LocalDate getMyDate() {
        return myDate;
    }

    @JsonGetter(value = "destination")
    public String getLandsAt() {
        return landsAt;
    }

    public String getSecretInfo() {
        return secretInfo;
    }

    @JsonGetter(value = "myArray")
    private String[] getMyArray() {
        return myArray;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    @JsonSetter(value = "DESTINATION")
    public void setLandsAt(String landsAt) {
        this.landsAt = landsAt;
    }

    public void setMyDate(LocalDate myDate) {
        this.myDate = myDate;
    }

    public void setSecretInfo(String secretInfo) {
        this.secretInfo = secretInfo;
    }

    public void setMyArray(String[] myArray) {
        this.myArray = myArray;
    }
}
