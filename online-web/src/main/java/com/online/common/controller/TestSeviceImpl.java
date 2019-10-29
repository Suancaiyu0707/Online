package com.online.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource(value="",name="test")
@ComponentScan(value = "com.online.common.api")
@ImportResource
@Import(value = {Service.class})

public class TestSeviceImpl  implements TestService{
    @Autowired
    public TestSeviceImpl(){

    }
    @Lookup
    public void hello(){

    }
}
