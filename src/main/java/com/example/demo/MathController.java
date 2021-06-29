package com.example.demo;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

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

}
