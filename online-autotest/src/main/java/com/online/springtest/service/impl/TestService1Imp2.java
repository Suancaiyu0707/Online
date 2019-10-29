package com.online.springtest.service.impl;

import com.online.springtest.service.TestService1;
import com.online.springtest.service.TestService2;

public class TestService1Imp2 implements TestService2 {
    private TestService1 testService1;

    public void setTestService1(TestService1 testService1) {
        this.testService1 = testService1;
    }


    @Override
    public void sayHello() {
        System.out.println("testService1="+testService1);
    }
}
