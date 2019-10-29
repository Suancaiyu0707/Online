package com.online.J2SE.design_pattern.policyDesign.Human;

import com.online.J2SE.design_pattern.policyDesign.Food.ChildFood;
import com.online.J2SE.design_pattern.policyDesign.clothes.ChildClothes;

public class ChildBehaviour extends AbstractHumanBehaviour{

    public ChildBehaviour(){
        this.clothes = new ChildClothes();
        this.food = new ChildFood();
    }
}
