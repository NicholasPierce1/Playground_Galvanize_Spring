package com.example.demo.controllers;

import com.example.demo.model.FlightInfo;
import com.example.demo.model.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Stream;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class HelloController {

    private static Logger logger = LoggerFactory.getLogger(HelloController.class);

    @GetMapping("/")
    public String helloWorld(HttpSession httpSession) {

        //Date date = Date.from(Instant.from(LocalDate.now())).

        logger.info("hello world from {} logger!", "my");

        System.out.println(httpSession);

        return "Hello from Spring!";
    }

    @RequestMapping( value = "/paramStringBasic", produces = MediaType.TEXT_PLAIN_VALUE, method = GET)
    public ResponseEntity<String> getParamStringBasic(
    ){

        // @RequestHeader( value = "testHeader", required = false, defaultValue = "middle") String middleName

        // if required is false, then prepare to handle null if no default value
        final HttpHeaders headers = new HttpHeaders();

        return new ResponseEntity<String>(
                "Hello from home controller",
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
    public Task postNewTaskWithForm(@RequestBody @ModelAttribute("Task") Task task){
        return task;
    }

    @PostMapping(value = "/map-example", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String getMapParams(@RequestParam Map<String, String> formData) {
        return formData.toString();
    }

    @PostMapping(value = "/URL-encoded-indv", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String getIndvParams(@RequestParam String name) {
        return name;
    }

    @RequestMapping(value = "/webRequest", method = GET)
    public List<?> getAll(WebRequest webRequest){
        Map<String, String[]> params = webRequest.getParameterMap();
        // can get params & headers here

        final ArrayList<String> list = new ArrayList<>();

        params.forEach( (x,y) -> {list.add(String.format("%s-%s", x, Arrays.stream(y).reduce((a,c) -> a+c)));} );

        return null;
    }

    @RequestMapping(
            value = "/getFlightInfo",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET
    )
    public @NotNull FlightInfo getFlightInfo(){
        return null; //new FlightInfo();
    }

    @RequestMapping(
            value = "/postFlightInfo",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST
    )
    public FlightInfo createFlightInfo(@RequestBody FlightInfo flightInfo){

        System.out.println("Did it set the ignore value? " + flightInfo.getSecretInfo().equals("scary flight"));

        return flightInfo;
    }

    @RequestMapping(
            value = "/postFlightInfos",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST
    )
    public FlightInfo createFlightInfos(@RequestBody FlightInfo[] flightInfo){

        // System.out.println("Did it set the ignore value? " + flightInfo.getSecretInfo().equals("scary flight"));

        return flightInfo[flightInfo.length -1];
    }

    @RequestMapping(
            value = "/postFlightInfo/custom",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST
    )
    public FlightInfo createFlightInfoFromObjectMapper(@RequestBody String jsonString) throws JsonProcessingException {

        final ObjectMapper objectMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
                //new ObjectMapper();

        //System.out.println("Did it set the ignore value? " + flightInfo.getSecretInfo().equals("scary flight"));

        return objectMapper.readValue(jsonString, FlightInfo.class);
    }

    @Autowired
    ObjectMapper objectMapper;

    @RequestMapping(
            value = "/testServlet",
            method = RequestMethod.GET
    )
    public boolean UseServletRequest(final HttpServletRequest httpServletRequest,
                                     final HttpServletResponse httpServletResponse,
                                     @RequestBody ArrayList<String> strings) throws Exception{

        //final Map<String, Object> servletMap = (Map<String, Object>)objectMapper.convertValue(httpServletRequest, Map.class);
        System.out.println("URI: " + httpServletRequest.getRequestURI());
        System.out.println("Path: " + httpServletRequest.getPathInfo());
        System.out.println("request URL: " + httpServletRequest.getRequestURL());
        System.out.println("context path: " + httpServletRequest.getContextPath());

        if(httpServletRequest.getCookies() != null) {
            System.out.println("\nprinting cookies:");
            Arrays.<Cookie>stream(httpServletRequest.getCookies()).forEach((cookie) -> {
                System.out.print("key: " + cookie.getName());
                System.out.print("\tvalue: " + URLDecoder.decode(cookie.getValue() +"\n", StandardCharsets.UTF_8));

            });
        }

        //Cookie cookieToAdd = new Cookie("name", URLEncoder.encode("some long\svalue", StandardCharsets.UTF_8));
        // cookies with tomcat >= 8 don't support spaces. Must encode to UT8 between cookie calls
        // %20 or + = space in URL encoded && %2B = plus sign
        System.out.println("\nencode: " + URLEncoder.encode("some long\svalue + nothing = long value", StandardCharsets.UTF_8));
        System.out.println("decode: " +
                URLDecoder.decode(URLEncoder.encode("some long\svalue + nothing = long value", StandardCharsets.UTF_8), StandardCharsets.UTF_8)
        );
        //httpServletResponse.addCookie(cookieToAdd);

        System.out.println(strings);

        return true;
    }

}
