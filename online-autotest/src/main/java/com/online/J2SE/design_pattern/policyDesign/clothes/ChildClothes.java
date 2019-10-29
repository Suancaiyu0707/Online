package com.online.J2SE.design_pattern.policyDesign.clothes;

public class ChildClothes implements Clothes{
    @Override
    public void clothes() {
        System.out.println("=======child:我就不穿裤子======");
    }
}
