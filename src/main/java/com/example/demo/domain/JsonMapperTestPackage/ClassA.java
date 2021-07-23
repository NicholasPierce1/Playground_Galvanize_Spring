package com.example.demo.domain.JsonMapperTestPackage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jdk.jfr.DataAmount;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClassB.class, name = "ClassB"),
        @JsonSubTypes.Type(value = ClassC.class, name = "ClassC")
})
public abstract class ClassA {

    public int x;

    public String y;

    public ClassA(){}

    public int getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(String y) {
        this.y = y;
    }
}
