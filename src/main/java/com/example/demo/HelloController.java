package com.example.demo;

import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class HelloController {

    @GetMapping("/")
    public String helloWorld() {
        return "Hello from Spring!";
    }

    @RequestMapping( value = "/paramStringBasic", produces = MediaType.TEXT_PLAIN_VALUE, method = GET)
    public ResponseEntity<String> getParamStringBasic(
    ){

        // @RequestHeader( value = "testHeader", required = false, defaultValue = "middle") String middleName

        // if required is false, then prepare to handle null if no default value
        final HttpHeaders headers = new HttpHeaders();

        return new ResponseEntity<String>(
                "Hello",
                headers,
                HttpStatus.OK
        );

    }

    @RequestMapping( value = "/paramString", produces = MediaType.TEXT_PLAIN_VALUE, method = GET)
    public ResponseEntity<String> getParamString(
            @RequestParam(value = "first") String firstName,
            @RequestParam(value = "last", defaultValue = "Doe", required = false) String lastName,
            @RequestHeader( value = "testHeader", required = false, defaultValue = "middle") String middleName
            ){

        // @RequestHeader( value = "testHeader", required = false, defaultValue = "middle") String middleName

        // if required is false, then prepare to handle null if no default value
        final HttpHeaders headers = new HttpHeaders();

        return new ResponseEntity<String>(
                "Hello, " + firstName + " " + middleName + " " + lastName,
                headers,
                HttpStatus.OK
        );

    }

    @RequestMapping( value ={ "/pathString/{first}/{last}", "/pathString/{first}", "/pathString/{first}/" }, produces = MediaType.TEXT_PLAIN_VALUE, method = GET)
    public ResponseEntity<String> getPathString(
            @PathVariable( value = "first" ) String firstName,
            @PathVariable( value = "last", required = false ) String last){

        final HttpHeaders headers = new HttpHeaders();

        final String lastName = last == null ? "Doe" : last;

        return new ResponseEntity<String>(
                "Hello, " + firstName + " " + lastName,
                headers,
                HttpStatus.OK
        );
    }

    @RequestMapping( value = "/getN_Items", method = GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getN_Items(
            @RequestParam(value = "n") int... firstName
    ){
        // System.out.println(firstName[0]);
        System.out.println(Arrays.stream(firstName).reduce(0, Integer::sum));

        // if required is false, then prepare to handle null if no default value
        final HttpHeaders headers = new HttpHeaders();

        return new ResponseEntity<String>(
                String.valueOf(Arrays.stream(firstName).reduce(0, Integer::sum)),
                headers,
                HttpStatus.OK
        );

    }

    @RequestMapping(
            value = {"/task/", "/task"},
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    public Task postNewTask(@ModelAttribute("Task") Task task){
        return task;
    }

    @RequestMapping(
            value = {"/task/form", "/task/form/"},
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            method = RequestMethod.POST)
    public Task postNewTaskWithForm(@ModelAttribute("Task") Task task){
        return task;
    }

    @PostMapping(value = "/map-example", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String getMapParams(@RequestParam Map<String, String> formData) {
        return formData.toString();
    }

    @RequestMapping(value = "/webRequest", method = GET)
    public List<?> getAll(WebRequest webRequest){
        Map<String, String[]> params = webRequest.getParameterMap();
        // can get params & headers here

        final ArrayList<String> list = new ArrayList<>();

        //params.forEach( (x,y) -> {list.add(String.format("%s-%s"));} );

        return null;
    }

}
