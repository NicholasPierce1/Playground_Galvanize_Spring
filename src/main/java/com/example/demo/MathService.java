package com.example.demo;

import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.List;

public class MathService {
    String operation;
    String x;
    String y;
    MultiValueMap<String, String> n ;

    public MathService(MultiValueMap<String, String> mMap) {
        setN(mMap);
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public MultiValueMap<String, String> getN() {
        return n;
    }

    public void setN(MultiValueMap<String, String> n) {
        this.n = n;
    }

    public String doMath() {
        if (operation != null) {
            if (operation.equals("add")) {
                int add =  Integer.parseInt(x) + Integer.parseInt(y);
                return x + " + " + y + " = " + add;
            }
            if (operation.equals("multiply")) {
                int mul =  Integer.parseInt(x) * Integer.parseInt(y);
                return x + " * " + y + " = " + mul;
            }
            if (operation.equals("subtract")) {
                int sub =  Integer.parseInt(x) - Integer.parseInt(y);
                return x + " - " + y + " = " + sub;
            }
            if (operation.equals("divide")) {
                int div =  Integer.parseInt(x) / Integer.parseInt(y);
                return x + " / " + y + " = " + div;
            }
        }
        int sum =  Integer.parseInt(x) + Integer.parseInt(y);
        return x + " + " + y + " = " + sum;
    }

    public String doSum() {
        String str = "";
        int sum = 0;
        for (List<String> data : n.values())
            for (int i = 0; i < data.size() ; i++) {
                if (i == (data.size() - 1)) {
                    str += data.get(i);
                } else {
                    str += data.get(i) + " + ";
                }
                sum += Integer.parseInt(data.get(i));
            }
        return str + " = " + sum;
    }

}
