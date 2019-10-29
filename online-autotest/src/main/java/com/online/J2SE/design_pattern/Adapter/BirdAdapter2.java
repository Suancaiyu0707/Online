package com.online.J2SE.design_pattern.Adapter;

import com.online.J2SE.design_pattern.Adapter.dest.Bird;
import com.online.J2SE.design_pattern.Adapter.source.Chicken;
import com.online.J2SE.design_pattern.Adapter.source.Muji;

/**
 * 类适配器：适配器也结成了被适配的类
  */
public class BirdAdapter2 extends Muji implements Bird {

    @Override
    public void fly() {
        System.out.print("====i am chicken,我假装我可以飞===");
        super.run();
    }

    @Override
    public void sing() {
        System.out.print("====i am chicken,我假装我唱歌很好听===");
        super.daming();
    }
}

