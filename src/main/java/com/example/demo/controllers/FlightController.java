package com.example.demo.controllers;

import com.example.demo.model.FlightTickets;
import com.example.demo.model.Ticket;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping(value = "/flights/tickets/")
public class FlightController {

    @RequestMapping(
            value = {"/total", "/total/"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST
    )
    public ResponseEntity<String> postCalculateTotalTicketCost(@RequestBody FlightTickets flightTickets) throws Exception{

        JSONObject jsonObject = new JSONObject();

        double price = 0;

        for(final Ticket ticket : flightTickets.getTickets())
            price += ticket.getPrice();

        jsonObject.put("result", price);

        return new ResponseEntity<String>(jsonObject.toString(), HttpStatus.OK);

    }

}
