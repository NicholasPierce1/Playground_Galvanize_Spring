package com.example.demo;

import com.example.demo.Repository.LessonRepository;
import com.example.demo.domain.Lesson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;


import static org.hamcrest.Matchers.instanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.is;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LessonTests {

    @Autowired
    private LessonRepository _lessonRepository;

    @Autowired
    private MockMvc _mvc;

    private ObjectMapper _objectMapper;

    @BeforeAll
    public void setInstanceState(){
        this._objectMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
    }

    @Test
    @Transactional
    @Rollback
    public void testAdd() throws Exception{

        final Lesson lesson = new Lesson(){{
            setName("H2");
            setDeliveredOn(LocalDate.of(2020, 10, 10));
        }};

        final MockHttpServletRequestBuilder addLesson = post("/lesson")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this._objectMapper.writeValueAsString(lesson));

        this._mvc.perform(addLesson)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(lesson.getName())))
                .andExpect(
                        jsonPath(
                                "$.deliveredOn",
                                is(lesson.getDeliveredOn().format(
                                        DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                ))
                        )
                )
                .andExpect(jsonPath("$.id", instanceOf(Number.class)));

    }

    @Test
    @Transactional
    @Rollback
    public void testPatch() throws Exception{

        Lesson lesson = new Lesson(){{
            setName("H2");
            setDeliveredOn(LocalDate.of(2020, 10, 10));
        }};

        final Lesson lessonUpdatedState = new Lesson(){{
            setName("H2-Updated");
            setDeliveredOn(LocalDate.of(2002, 7, 1));
        }};


        final MockHttpServletRequestBuilder addLesson = post("/lesson")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this._objectMapper.writeValueAsString(lesson));

        final String result = this._mvc.perform(addLesson).andReturn().getResponse().getContentAsString();

        lesson = this._objectMapper.readValue(result, Lesson.class);

        // patch operation test

        final MockHttpServletRequestBuilder patchLesson = patch("/lesson/" + lesson.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this._objectMapper.writeValueAsString(lessonUpdatedState));

        // set to update lesson id to the lesson that is being modified
        lessonUpdatedState.setId(lesson.getId());

        this._mvc.perform(patchLesson)
                .andExpect(status().isOk())
                .andExpect(content().json(this._objectMapper.writeValueAsString(lessonUpdatedState)));

    }

}
