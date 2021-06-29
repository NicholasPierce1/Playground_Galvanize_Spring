package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MathController.class)
@AutoConfigureMockMvc
public class MathControllerTests {
    @Autowired
    private MockMvc mvc;

    @Test
    public void testMathPi() throws Exception {
        this.mvc.perform(get("/math/pi").accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk()).andExpect(content()
                .string("3.141592653589793"));
    }

    @Test
    public void testForm() throws Exception {
        this.mvc.perform(get("/math/pi")
                .params(new MultiValueMapAdapter<String, String>(
                        new HashMap<String, List<String>>(){{
                            put("first", new ArrayList<>(){{add("Nick");}});
                            put("last", new ArrayList<>(){{add("Pierce");}});
                        }}
                ))
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk()).andExpect(content()
                .string("3.141592653589793"));
    }

}
