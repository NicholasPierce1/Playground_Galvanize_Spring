package com.example.demo.controllers;

import com.example.demo.configuration.MyUserDetailsService;
import com.example.demo.configuration.jwt.JwtUtil;
import com.example.demo.domain.AuthenticationRequest;
import com.example.demo.domain.AuthenticationResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/security")
public class SecurityController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private HttpSecurity httpSecurity;

    @RequestMapping({ "/hello" })
    public String firstPage() {
        return "Hello World";
    }

    @RequestMapping({ "/hello1" })
    public String secondPage() {
        return "Hello World Again";
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {

//            final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());
//            System.out.println("called");
//            authenticationManager.authenticate(token);

            UserDetails details = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(details, authenticationRequest.getPassword(),
                    details.getAuthorities());
            token.setAuthenticated(false);
            System.out.println("called");
            authenticationManager.authenticate(token); // throws authentication exception if bad credentials (v 2.5.2)

            token.setAuthenticated(true);
            /*
             indirectly calls UserDetailsService that was tethered to the Authentication Manager Builder
              - uses the username to create the user detail service
              - uses the password to assert equality amongst the given user and the user stored *somewhere*
             */
        }
        catch (BadCredentialsException e) {
            System.out.println(e.getMessage());
            throw new Exception("Incorrect username or password", e);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        // repeating same operation to generate JWT token
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        // notice: no security context was set after successful authentication
        // 2nd interaction sets authentication context bearing the header w/ Authorization : 'Bearer <JWT>'

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @RequestMapping(
            value = {"/secret", "/secret/"},
            method = RequestMethod.GET
    )
    public String getSecretStuff(){
        return "You must be an admin!";
    }

    @RequestMapping(
            value = {"/logUserOut", "/logUserOut/"},
            method = RequestMethod.DELETE
    )
    public UserDetails logout(HttpServletRequest request, HttpServletResponse response) throws Exception {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }

        assert(username != null);

        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

//        CookieClearingLogoutHandler cookieClearingLogoutHandler = new CookieClearingLogoutHandler(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY);
//        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
//        cookieClearingLogoutHandler.logout(request, response, null);
//        securityContextLogoutHandler.logout(request, response, null);
//        request.logout();

        //this.httpSecurity.logout().deleteCookies("auth_code", "JSESSIONID").invalidateHttpSession(true).clearAuthentication(true);

        // SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        // SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        // securityContextLogoutHandler.logout(request, response, null);


        ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).eraseCredentials();

        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        System.out.println(sessionRegistry.getAllPrincipals());

        return this.userDetailsService.loadUserByUsername(username);
    }

    @RequestMapping(
            value = {"/getUser", "/getUser/"},
            method = RequestMethod.GET
    )
    public UserDetails getUser(HttpServletRequest request, HttpServletResponse response) throws Exception {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }

        assert(username != null);

        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

//        CookieClearingLogoutHandler cookieClearingLogoutHandler = new CookieClearingLogoutHandler(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY);
//        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
//        cookieClearingLogoutHandler.logout(request, response, null);
//        securityContextLogoutHandler.logout(request, response, null);

        // SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        return this.userDetailsService.loadUserByUsername(username);
    }
}
