package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return this.mathService.doVolume(map);
    }

    @PostMapping("/area")
    public ResponseEntity<String> area(@RequestParam Map<String, String> content){
        return this.mathService.doArea(content);
    }

}
