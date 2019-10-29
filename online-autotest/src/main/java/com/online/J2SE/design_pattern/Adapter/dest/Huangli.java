package com.online.J2SE.design_pattern.Adapter.dest;

public class Huangli implements Bird{

    @Override
    public void fly() {
        System.out.println("i can fly,而且可以飞的很高");
    }

    @Override
    public void sing() {
        System.out.println("i can sing,而且声音很好听");

    }
}
