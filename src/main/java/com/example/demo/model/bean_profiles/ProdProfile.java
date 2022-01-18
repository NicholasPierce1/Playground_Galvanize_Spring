package com.example.demo.model.bean_profiles;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public final class ProdProfile implements BProfile {

    @Override
    public String setup() {
        return "prod";
    }
}
