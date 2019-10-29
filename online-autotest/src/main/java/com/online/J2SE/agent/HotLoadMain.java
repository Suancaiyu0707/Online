package com.online.J2SE.agent;

import com.online.J2SE.classloader.HotLoad;

import java.lang.reflect.InvocationTargetException;

public class HotLoadMain {
    public static void main(String[] args)
            throws ClassNotFoundException, InterruptedException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        while (true) {
            HotLoad account = new HotLoad();
            account.operation();

            account.operation3();
            Thread.sleep(5000);
        }
    }
}
