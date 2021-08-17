package com.example.demo.domain.JsonMapperTestPackage;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ClassC extends ClassA{

    public double d;

    public ClassC(){
        super();
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    @Override
    public void blah() {
        try {
            super.blah();
            Object obj = null;
            obj.equals(null);
        }
        catch(NullPointerException e){
            System.out.println("oops");
            throw e;
        }
        catch (Exception e){ //LocalDate date = LocalDate.of(10,1);
        }
       // doSomething();
        doSomethingAgain();

    }

    public void doSomething() throws FileNotFoundException {}

    public void doSomethingAgain() throws RuntimeException{}

}
