package com.online.common.controller;

import Test.A;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationController {
    @Bean
    public A getO(){
        return new A();
    }
}
