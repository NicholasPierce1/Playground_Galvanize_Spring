package com.example.demo;

import com.example.demo.controllers.MathController;
import com.example.demo.service.MathService;
import com.mongodb.assertions.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//@SpringBootTest
//@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
//@RunWith(SpringRunner.class) IF Junit4
@ContextConfiguration(classes ={DemoApplication.class} )
@ActiveProfiles(profiles = {"dev"})  // default already given in default profile configuration
public class ProfileTest {

    @Autowired
    DemoApplication.DummyBeanClass dummyBeanClass;

    @Test
    public void testProfileAvailability(){
        Assertions.assertTrue(dummyBeanClass.isActive());
    }


}
