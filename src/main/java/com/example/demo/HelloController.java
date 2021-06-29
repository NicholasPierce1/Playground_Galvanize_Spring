package com.example.demo;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
public class HelloController {

    @GetMapping("/")
    public String helloWorld() {
        return "Hello from Spring!";
    }

    @RequestMapping( value = "/paramStringBasic", produces = MediaType.TEXT_PLAIN_VALUE, method = RequestMethod.GET)
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

    @RequestMapping( value = "/paramString", produces = MediaType.TEXT_PLAIN_VALUE, method = RequestMethod.GET)
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

    @RequestMapping( value ={ "/pathString/{first}/{last}", "/pathString/{first}/" }, produces = MediaType.TEXT_PLAIN_VALUE, method = RequestMethod.GET)
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

    @RequestMapping( value = "/getN_Items", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
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

}
