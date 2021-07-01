package com.example.demo;

import com.example.demo.controllers.FlightController;
import com.example.demo.model.FlightTickets;
import com.example.demo.model.Passenger;
import com.example.demo.model.Ticket;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

// import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is; // don't use
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.is;


@WebMvcTest(FlightController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FlightJsonTest {

    @Autowired
    private MockMvc _mvc;

    private FlightTickets _flightTickets;

    private FlightTickets _flightTicketsCompatibleWithGson;

    private ObjectMapper _objectMapper;

    private Gson _gson;

    @BeforeAll
    public void setConstantState(){

        this._gson = new GsonBuilder().setPrettyPrinting().create();

        this._objectMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();

    }

    @BeforeEach
    public void setMemberState(){
        this._flightTickets = new FlightTickets(){
            {
                setTickets(
                        new Ticket[]{
                                new Ticket() {{
                                    setPassenger(
                                            new Passenger() {{
                                                setFirstName("Some name");
                                                setLastName("Some other name");
                                            }}
                                    );

                                    setPrice(200);
                                }},
                                new Ticket() {{
                                    setPassenger(
                                            new Passenger() {{
                                                setFirstName("Name B");
                                                setLastName("Name C");
                                            }}
                                    );

                                    setPrice(150);
                                }}
                        }
                );
            }};

        this._flightTicketsCompatibleWithGson = new FlightTickets();

        final Passenger passengerOne = new Passenger();
        passengerOne.setFirstName("Some name");
        passengerOne.setLastName("Some other name");

        final Passenger passengerTwo = new Passenger();
        passengerTwo.setFirstName("Name B");
        passengerTwo.setLastName("Name C");

        final Ticket ticketOne = new Ticket();
        ticketOne.setPrice(200);
        ticketOne.setPassenger(passengerOne);

        final Ticket ticketTwo = new Ticket();
        ticketTwo.setPrice(150);
        ticketTwo.setPassenger(passengerTwo);

        this._flightTicketsCompatibleWithGson.setTickets(new Ticket[]{ticketOne, ticketTwo});


    }

    @Test
    public void testGetTicketPriceSumSerialize() throws Exception{

        // step 1: create JSON data for input and output/expected (String)

        // create expected
        final JSONObject expected = new JSONObject();
        expected.put("result", 350);

        final String jsonString = this._gson.toJson(this._flightTicketsCompatibleWithGson, FlightTickets.class);
        assertTrue(true);
        System.out.println(jsonString);
        // System.out.println(this._objectMapper.writeValueAsString(this._flightTickets));

        // step 2: create post request & tailor the json data to it

        MockHttpServletRequestBuilder postRequest = post("/flights/tickets/total")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        this._mvc.perform(postRequest)
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.result").value(expected.getDouble("result")))
                .andExpect(jsonPath("$.result", is(350)))
                //.andExpect(jsonPath("$").value(expected.toString()));
                .andExpect(content().json(expected.toString()));
        //                .andExpect((ResultMatcher) jsonPath("$.result", is(expected.get("result"))));

        // step 3: create, and run, mvc test and assert json object is expected

    }

    @Test
    public void testGetTicketPriceSumStringLiteral() throws Exception{

        // step 1: create JSON data for input and output/expected (String)

        // create expected
        final JSONObject expected = new JSONObject();
        expected.put("result", 350);

        final String jsonString =
                "{\"tickets\":[{\"passenger\":{\"firstName\":\"Some name\"," +
                        "\"lastName\":\"Some other name\"},\"price\":200.0},{" +
                        "\"passenger\":{\"firstName\":\"Name B\"," +
                        "\"lastName\":\"Name C\"},\"price\":150.0}]}";
        assertTrue(true);
        // System.out.println(this._objectMapper.writeValueAsString(this._flightTickets));

        // step 2: create post request & tailor the json data to it

        MockHttpServletRequestBuilder postRequest = post("/flights/tickets/total")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        this._mvc.perform(postRequest)
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.result").value(expected.getDouble("result")))
                .andExpect(jsonPath("$.result", is(350)));
        //.andExpect(jsonPath("$").value(expected.toString()));
        //.andExpect(content().json(expected.toString()));
        //                .andExpect((ResultMatcher) jsonPath("$.result", is(expected.get("result"))));

        // step 3: create, and run, mvc test and assert json object is expected

    }

    @Test
    public void testGetTicketPriceSumJsonResourceFile() throws Exception{

        // step 1: create JSON data for input and output/expected (String)

        // create expected
        final JSONObject expected = new JSONObject();
        expected.put("result", 350);

        final URL jsonResourceLocation = this.getClass().getResource("/flightTickets.json");

        assertNotNull(jsonResourceLocation, "json path resource location is null.");

        File f = new File(jsonResourceLocation.getPath());

        final String jsonString = Files.readString(f.toPath());

        // System.out.println(this._objectMapper.writeValueAsString(this._flightTickets));

        // step 2: create post request & tailor the json data to it

        MockHttpServletRequestBuilder postRequest = post("/flights/tickets/total")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        this._mvc.perform(postRequest)
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.result").value(expected.getDouble("result")))
                .andExpect(jsonPath("$.result", is(350)));
        //.andExpect(jsonPath("$").value(expected.toString()));
        //.andExpect(content().json(expected.toString()));
        //                .andExpect((ResultMatcher) jsonPath("$.result", is(expected.get("result"))));

        // step 3: create, and run, mvc test and assert json object is expected

    }


}