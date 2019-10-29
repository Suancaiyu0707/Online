package com.online.J2SE.design_pattern.policyDesign.Human;

import com.online.J2SE.design_pattern.policyDesign.Food.ManFood;
import com.online.J2SE.design_pattern.policyDesign.clothes.ManClothes;

public class FemaleBehaviour extends AbstractHumanBehaviour{
    public FemaleBehaviour(){
        this.clothes=new ManClothes();
        this.food = new ManFood();
    }


}
