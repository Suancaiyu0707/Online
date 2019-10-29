package com.online.springtest;

import com.online.springtest.service.TestService2;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext context=new ClassPathXmlApplicationContext("application.xml");

        TestService2 testService2 = (TestService2) context.getBean("testService2");


        System.out.println(testService2);
        testService2.sayHello();

//        User user=context.getBean(User.class);
//        System.out.println(user.toString());

    }
}
