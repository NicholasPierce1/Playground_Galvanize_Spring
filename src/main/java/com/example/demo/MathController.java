package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MathController {

    @GetMapping("/math/pi")
//    @ResponseStatus(HttpStatus.OK)
    public String getPi(){
        return "3.141592653589793";
    }

}
