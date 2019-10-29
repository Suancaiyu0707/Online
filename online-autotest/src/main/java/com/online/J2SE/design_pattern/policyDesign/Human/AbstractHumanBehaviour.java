package com.online.J2SE.design_pattern.policyDesign.Human;

import com.online.J2SE.design_pattern.policyDesign.Food.Food;
import com.online.J2SE.design_pattern.policyDesign.clothes.Clothes;

/***
 * 策略模式
 *      分别封装行为接口(clothes和food)，实现算法族。超类（AbstractHumanBehaviour）里放行为接口对象，在子类（ChildBehaviour）里具体设定行为对象。
 *      原则就是：分离变化部分(行为)，封装接口，基于接口编程各种功能。此模式行为算法的变化独立于算法的使用者。
 *
 *  clothes 和 food是两种行为接口，这样我们要想再加一个行为组，这样行为的展示的形式是互相组合
 *
 *
 */
public class AbstractHumanBehaviour {
    public Clothes clothes;

    public Food food;

    public AbstractHumanBehaviour(){

    }
    public void clothes(){
        clothes.clothes();
    }

    public void food(){
        food.eat();
    }
}
