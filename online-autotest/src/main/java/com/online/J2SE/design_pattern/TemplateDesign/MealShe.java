package com.online.J2SE.design_pattern.TemplateDesign;

/***
 *模板方法使得子类可以不改变算法的结构即可重定义该算法的某些特定步骤
 */
public class MealShe extends MealTemplate{
    @Override
    void breakfast() {
        System.out.println("她的早餐是：豆浆+茶叶蛋");
    }

    @Override
    void lunch() {
        System.out.println("她的午餐是：食堂");
    }

    @Override
    void dinner() {
        System.out.println("她的晚餐是：面条");
    }
}
