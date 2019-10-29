package com.online.netty.jianan_study;

import com.google.gson.Gson;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ReflectClass2 {
    public static void main(String[] args) {
        AA aa = new AA();
        try {
            Method m = aa.getClass().getMethod("aa",String.class,Integer.class);
            Gson gson = new Gson();
             Parameter[] prams = m.getParameters();

            System.out.println(prams.length);
            System.out.println(prams[1].getName());

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
