package com.online.J2SE.design_pattern.policyDesign.Food;

public class WomanFood implements Food{
    @Override
    public void eat() {
        System.out.println("=======woman:我喜欢吃水果========");
    }
}
