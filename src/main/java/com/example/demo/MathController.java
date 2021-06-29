package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping( value = "/math/")
public class MathController {

    @Autowired
    MathService mathService;

    @GetMapping("/pi")
    public String getPi(){
        return "3.141592653589793";
    }

    @GetMapping("/calculate")
    public String calculate(@RequestParam Map<String, String> map) {
        return mathService.doMath(map);
    }

    @PostMapping("/sum")
    public String sum(@RequestParam( value = "n" ) int... n){
        // MathService mathService = new MathService(n);
        return mathService.doSum(n);
    }

    @GetMapping("/volume/{length}/{width}/{height}")
    public String volume(@PathVariable Map<String, String> map){
        int volume = 1;
        String placeHolder = "";

        for (String data : map.values()) {
            placeHolder += data + "x";

            volume *= Integer.parseInt(data);
        }
        placeHolder = placeHolder.substring(0, placeHolder.length() -1);
        return "The volume of a " + placeHolder + " rectangle is " + volume;
    }

    @PostMapping("/area")
    public String area(@RequestParam Map<String, String> content){

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
