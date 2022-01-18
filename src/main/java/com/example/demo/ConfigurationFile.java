package com.example.demo;

import com.example.demo.configuration.MyUserDetailsService;
import com.example.demo.service.MathService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.swing.text.DateFormatter;
import java.time.format.DateTimeFormatter;

@Configuration
public class ConfigurationFile { //implements WebApplicationInitializer {

//    @Override
//    public void onStartup(ServletContext servletContext) throws ServletException {
//        final String profile = "dev";
//        System.out.println("bean profile being setup with " + profile);
//        servletContext.setInitParameter(
//                "spring.profiles.active", profile);
//    }

    public @Bean ObjectMapper getObjectMapper(){
        return JsonMapper.builder()
                .findAndAddModules()
                .build();
    }

    public @Bean DateTimeFormatter getDateFormatter(){
        return DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    public @Bean
    MathService getMathService(){
        return new MathService();
    }

    public @Bean
    ApplicationContext getXmlApplicationContext(){
        ApplicationContext ctx = new
                ClassPathXmlApplicationContext( "applicationContext.xml" );

        return ctx;
    }
}
