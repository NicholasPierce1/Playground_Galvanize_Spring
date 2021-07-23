package com.example.demo.controllers;

import com.example.demo.service.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/kafka/producer/")
public class KafkaProducerController {

    //private final KafkaProducer producer;

//    @Autowired
//    public KafkaProducerController(KafkaProducer producer) {
//        this.producer = producer;
//    }

    @PostMapping("/publish")
    public boolean messageToTopic(
            @RequestParam( value= "message", required = false, defaultValue = "hello from kafka producer") String message){

       // this.producer.sendMessage(message);

        return true;
    }

}
