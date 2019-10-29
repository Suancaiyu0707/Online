package com.online.J2SE.design_pattern.Adapter.source;

public class Muji implements Chicken{
    @Override
    public void run() {
        System.out.println("i can run,跑起来像飞一样");

    }

    @Override
    public void daming() {
        System.out.println("i can daming,声音听起来像在唱歌");

    }
}
