package com.example.demo;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MathService {

    public MathService() {
    }

    public String doMath(final Map<String, String> map) {

        final int x = Integer.parseInt(map.get("x"));
        final int y = Integer.parseInt(map.get("y"));


        if (map.containsKey("operation")) {
            final String operation = map.get("operation");
            if (operation.equals("add")) {
                int add = x + y;
                return x + " + " + y + " = " + add;
            }
            if (operation.equals("multiply")) {
                int mul = x * y;
                return x + " * " + y + " = " + mul;
            }
            if (operation.equals("subtract")) {
                int sub = x - y;
                return x + " - " + y + " = " + sub;
            }
            if (operation.equals("divide")) {
                int div = x / y;
                return x + " / " + y + " = " + div;
            }
        }
        int sum = x + y;
        return x + " + " + y + " = " + sum;
    }

    public String doSum(int... n) {
        String str = "";
        int sum = 0;

        for (int i = 0; i < n.length; i++) {
            str += n[i] + " + ";
            sum += n[i];
        }

        str = str.substring(0, str.length() - 3);
        return str + " = " + sum;
    }
}
