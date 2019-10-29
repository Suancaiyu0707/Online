package com.online.J2SE.design_pattern.TemplateDesign;

/***
 * 我的一日三餐是交给父类模版，我自己不需要重写.单位我具体要实现三餐吃什么
 * 所以子类主要负责实现步骤模版里的每一个步骤的实现
 */
public class MealMe extends MealTemplate {

    @Override
    void breakfast() {
        System.out.println("我的早餐是：豆浆+茶叶蛋+油饼");
    }

    @Override
    void lunch() {
        System.out.println("我的午餐是：黄焖鸡");
    }

    @Override
    void dinner() {
        System.out.println("我的晚餐是：红烧牛肉拉面");
    }
}
