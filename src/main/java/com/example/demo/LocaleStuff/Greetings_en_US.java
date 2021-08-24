package com.example.demo.LocaleStuff;

import java.util.ListResourceBundle;

public class Greetings_en_US extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                {"welcome sign", "WELCOME"}
        };
    }

    protected class A{
        static int x = 3;
        static void doSomething(){}
    }
}
