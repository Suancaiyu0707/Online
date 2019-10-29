package com.online.J2SE.design_pattern.policyDesign.Food;

public class ManFood implements Food{
    @Override
    public void eat() {
        System.out.println("=======man:我喜欢吃肉========");
    }
}
