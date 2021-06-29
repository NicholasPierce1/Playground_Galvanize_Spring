package com.example.demo;

import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.List;
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
            if (i == (n.length - 1)) {
                str += n[i];
            } else {
                str += n[i] + " + ";
            }
            sum += n[i];

        }

        return str + " = " + sum;

    }
}
