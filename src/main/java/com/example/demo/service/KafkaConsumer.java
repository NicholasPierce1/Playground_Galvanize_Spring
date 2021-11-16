package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

//    @KafkaListener(topics = "test_topic",groupId = "group_id")
//    public void consumeMessage(String message){
//
//        System.out.println(message);
//    }

    /*
    // can also autowire KafkaConsumer<K,V> --> see kafka template for K,V types
    @KafkaListener(topics = "any-topic")
void listener(ConsumerRecord<String, String> record) {
    System.out.println(record.key());
    System.out.println(record.value());
    System.out.println(record.partition());
    System.out.println(record.topic());
    System.out.println(record.offset());
}
     */
}
