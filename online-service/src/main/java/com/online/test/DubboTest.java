package com.online.test;

import com.ctrip.framework.apollo.core.ConfigConsts;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class DubboTest {
    public static void main(String[] args) throws Exception {
        // set apollo meta server address, adjust to actual address if necessary
        System.setProperty(ConfigConsts.APOLLO_META_KEY, "http://localhost:8080");

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"META-INF/spring/dubbo-provider.xml"});
        context.start();
        System.in.read();//按任意键退出

        Thread.sleep(5000000);
    }
}
