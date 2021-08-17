package com.example.demo.domain.JsonMapperTestPackage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jdk.jfr.DataAmount;
import net.bytebuddy.implementation.bytecode.Throw;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;

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
abstract public class ClassA { // ... wtf

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

    protected void blah() throws Exception{}

    public final static int blah;
    static{
        blah = 3;
    }
    {
        x = 3;
        y = "word";
        short shortV = new Byte((byte)6);
        Short shotO = shortV;
        System.out.println(2 * shotO);
    }
}
