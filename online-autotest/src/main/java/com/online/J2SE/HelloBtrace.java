package com.online.J2SE;

import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.OnMethod;

@BTrace(unsafe = true)
public class HelloBtrace {

    @OnMethod(
            clazz="com.online.J2SE.RemoteClass",
            method="f1"
    )
    public static void onF1(String a,int b) {
        System.out.println("Hello BTrace");
    }
}
