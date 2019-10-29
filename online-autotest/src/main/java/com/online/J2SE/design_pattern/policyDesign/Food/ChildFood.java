package com.online.J2SE.design_pattern.policyDesign.Food;

public class ChildFood implements Food{

    @Override
    public void eat() {
        System.out.println("=======child:我喜欢吃零食========");
    }
}
