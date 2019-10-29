package com.online.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

@Component

public class HelloController {
    @Autowired
    private TestService testService;
}
