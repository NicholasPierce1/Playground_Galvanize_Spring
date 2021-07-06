package com.example.demo;

import com.example.demo.Repository.LessonRepository;
import com.example.demo.domain.Lesson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
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

//    @Autowired
//    private LessonRepository _lessonRepository;

    @Autowired
    private MockMvc _mvc;

    private ObjectMapper _objectMapper;

    private DateTimeFormatter _dateTimeFormatter;

    @BeforeAll
    public void setInstanceState(){
        this._objectMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();

        this._dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
                                        this._dateTimeFormatter
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

    @Test
    @Transactional
    @Rollback
    public void testFindByName() throws Exception{

        final long id = 1;

        final Lesson expected = new Lesson(){{
            setName("Spring");
            setDeliveredOn(LocalDate.parse("2021-06-30", _dateTimeFormatter));
            setId(id);
        }};

        final MockHttpServletRequestBuilder getByName = get("/lesson/find/" + expected.getName());

        this._mvc.perform(getByName)
                .andExpect(status().isOk())
                .andExpect(content().json(this._objectMapper.writeValueAsString(expected)));

    }

    @Test
    @Transactional
    @Rollback
    public void testGetDateBetween() throws Exception{

        final long idOne = 1;
        final long idTwo = 2;

        final Lesson expectedOne = new Lesson(){{
            setName("Spring");
            setDeliveredOn(LocalDate.parse("2021-06-30", _dateTimeFormatter));
            setId(idOne);
        }};

        final Lesson expectedTwo = new Lesson(){{
            setName("Angular");
            setDeliveredOn(LocalDate.parse("2021-07-07", _dateTimeFormatter));
            setId(idTwo);
        }};

        final JSONArray expectedJsonArray = new JSONArray();
        expectedJsonArray.put(this._objectMapper.writeValueAsString(expectedOne));
        expectedJsonArray.put(this._objectMapper.writeValueAsString(expectedTwo));

        final MockHttpServletRequestBuilder getBetweenDates = get(
                "http://localhost:8080/lesson/between?startDate=2000-01-01&endDate=2022-12-20"
        )
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        System.out.println(expectedJsonArray.toString());

        final MockHttpServletResponse response = this._mvc.perform(getBetweenDates)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse();

        final JSONArray actualJsonArray = new JSONArray(response.getContentAsString());

        for(int i = 0; i < actualJsonArray.length(); i++){

            final JSONObject expectedJsonObject = new JSONObject(expectedJsonArray.getString(i));
            final JSONObject actualJsonObject = new JSONObject(actualJsonArray.getString(i));

            assertEquals(actualJsonObject.toString(), expectedJsonObject.toString());
        }

    }

}
