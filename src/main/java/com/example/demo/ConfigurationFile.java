package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.swing.text.DateFormatter;
import java.time.format.DateTimeFormatter;

@Configuration
public class ConfigurationFile {

    public @Bean ObjectMapper getObjectMapper(){
        return JsonMapper.builder()
                .findAndAddModules()
                .build();
    }

    public @Bean DateTimeFormatter getDateFormatter(){
        return DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

}
