package com.example.demo.configuration;

import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@Component
public class MyUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        final User user = s.equals("admin") ? new User("admin", "password",
                new ArrayList<GrantedAuthority>(){{
                    add(new SimpleGrantedAuthority("admin"));
                    add(new SimpleGrantedAuthority("user"));
                }})
                :
                new User(
                        "generic",
                        "password",
                        new ArrayList<GrantedAuthority>(){{
                            add(new SimpleGrantedAuthority("user"));
                        }}
                );

        return user; //userMapCache.get(s);
    }
}

