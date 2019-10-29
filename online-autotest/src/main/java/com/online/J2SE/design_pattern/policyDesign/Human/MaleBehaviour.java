package com.online.J2SE.design_pattern.policyDesign.Human;

import com.online.J2SE.design_pattern.policyDesign.Food.ManFood;
import com.online.J2SE.design_pattern.policyDesign.Food.WomanFood;
import com.online.J2SE.design_pattern.policyDesign.clothes.ManClothes;
import com.online.J2SE.design_pattern.policyDesign.clothes.WomanClothes;

public class MaleBehaviour extends AbstractHumanBehaviour{
    public MaleBehaviour(){
        this.clothes=new WomanClothes();
        this.food = new WomanFood();
    }


}
