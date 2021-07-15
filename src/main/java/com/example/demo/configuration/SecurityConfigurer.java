package com.example.demo.configuration;

import com.example.demo.configuration.jwt.JwtRequestFilter;
import com.example.demo.configuration.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService myUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService);
    }

    public @Bean UserDetailsService getUserDetailsService(){
        System.out.println("bean called");
        return new MyUserDetailsService();
    }

    public @Bean JwtRequestFilter getJwtRequestFilter(){
        return new JwtRequestFilter();
    }

    public @Bean JwtUtil getJwtUtil(){
        return new JwtUtil();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    @Bean  public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        System.out.print("is injection working? ");
        System.out.println(this.myUserDetailsService != null);

        httpSecurity
                .csrf().disable()
                .authorizeRequests()
//                .antMatchers("/authenticate").permitAll()
//                .antMatchers("/hello1").permitAll()
//                //.antMatchers("/sensitiveController/**").hasRole("admin")
//                .antMatchers("/secret", "/secret/").hasAuthority("admin")
//                .antMatchers("/getUser", "/getUser/").hasAuthority("user")
//                .anyRequest().authenticated().and()
                .anyRequest().permitAll().and()
                .exceptionHandling().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).maximumSessions(1);

        /*
            hasAuthority(?)
            hasAnyAuthority()
            authenticated()
            permitAll()
         */

        // how authentication is performed via jwt tokens AFTER authentication (token dispense)
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}


