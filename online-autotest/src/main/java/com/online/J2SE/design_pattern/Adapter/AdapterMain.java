package com.online.J2SE.design_pattern.Adapter;

import com.online.J2SE.design_pattern.Adapter.dest.Bird;
import com.online.J2SE.design_pattern.Adapter.dest.Huangli;
import com.online.J2SE.design_pattern.Adapter.source.Chicken;
import com.online.J2SE.design_pattern.Adapter.source.Muji;

public class AdapterMain {

    public static void main(String[] args) {
        Bird huangli = new Huangli();
        huangli.fly();
        huangli.sing();
        Chicken muji = new Muji();
        muji.run();
        muji.daming();
        Bird unknowBird = new BirdAdapter(muji);
        unknowBird.fly();
        unknowBird.sing();

        Bird unknowBird2 = new BirdAdapter2();
        unknowBird2.fly();
        unknowBird2.sing();
    }
}
