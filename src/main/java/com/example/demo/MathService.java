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

    public String doVolume(final Map<String, String> map){
        int volume = 1;
        String placeHolder = "";

        for (String data : map.values()) {
            placeHolder += data + "x";

            volume *= Integer.parseInt(data);
        }
        placeHolder = placeHolder.substring(0, placeHolder.length() -1);
        return "The volume of a " + placeHolder + " rectangle is " + volume;
    }

    public String doArea(final Map<String, String> content){
        final String typeKey = "type";
        final String radiusKey = "radius";
        final String heightKey = "height";
        final String widthKey = "width";

        if (content.get(typeKey).equals("circle")) {
            return String.format(
                    "Area of a %s with a radius of %d is %.5f",
                    content.get(typeKey),
                    Integer.parseInt(content.get(radiusKey)),
                    Math.pow(Integer.parseInt(content.get(radiusKey)),2) * Math.PI
            );
        } else {
            return String.format("Area of a %sx%s %s is %d",
                    content.get(widthKey),
                    content.get(heightKey),
                    content.get(typeKey),
                    Integer.parseInt(content.get(heightKey)) * Integer.parseInt(content.get(widthKey))
            );
        }
    }
}
