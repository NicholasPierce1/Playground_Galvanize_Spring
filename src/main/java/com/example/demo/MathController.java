package com.example.demo;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping( value = "/math/")
public class MathController {

    @GetMapping("/pi")
    public String getPi(){
        return "3.141592653589793";
    }

    @GetMapping("/calculate")
    public String calculate(MathService mathService) {
        return mathService.doMath();
    }

    @PostMapping("/sum")
    public String sum(@RequestParam MultiValueMap<String, String> n){
        MathService mathService = new MathService(n);
        return mathService.doSum();
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


}
