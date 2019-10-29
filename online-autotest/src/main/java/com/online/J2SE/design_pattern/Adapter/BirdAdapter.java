package com.online.J2SE.design_pattern.Adapter;

import com.online.J2SE.design_pattern.Adapter.dest.Bird;
import com.online.J2SE.design_pattern.Adapter.source.Chicken;

/***
 * 对象适配器(比较常用)
 * 一个适配器，负责将chicken适配为bird
 * 因为适配器要转换为目标接口，这个这个适配肯定要实现目标接口，对外表现出来它是目标接口的对象
 */
public class BirdAdapter implements Bird {
    private Chicken chicken;
    public BirdAdapter(Chicken chicken){
        this.chicken = chicken;
    }
    @Override
    public void fly() {
        System.out.print("====i am chicken,我假装我可以飞===");
        chicken.run();
    }

    @Override
    public void sing() {
        System.out.print("====i am chicken,我假装我唱歌很好听===");
        chicken.daming();
    }
}
