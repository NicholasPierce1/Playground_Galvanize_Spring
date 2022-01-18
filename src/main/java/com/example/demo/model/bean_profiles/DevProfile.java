package com.example.demo.model.bean_profiles;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DevProfile implements BProfile {

    @Override
    public final String setup() {
        return "dev";
    }
}
