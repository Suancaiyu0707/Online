package com.online.J2SE.classloader;

public class HotLoad {
    public void operation() {
        System.out.println("operation...");
        try {
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void operation3() {
        System.out.println("operation3...");
        try {
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
