package com.online.J2SE.design_pattern.TemplateDesign;

/***
 * 一日三餐的模版类，meals定义了一日三餐的执行顺序
 *
 * 模板模式的优点
 * 　（1）具体细节步骤实现定义在子类中，子类定义详细处理算法是不会改变算法整体结构。
 * 　（2）代码复用的基本技术，在数据库设计中尤为重要。
 * 　（3）存在一种反向的控制结构，通过一个父类调用其子类的操作，通过子类对父类进行扩展增加新的行为，符合“开闭原则”。
 *
 * 弊端：
 *    需要非常慎重的修改，特别是模版里的执行顺序不能改变。
 */
public abstract  class MealTemplate {
    //用模版的方式定义了一天三餐的模版，三餐的的步骤不能修改，所以定义到父类里。
    //但是具体的三个步骤的实现可以交给子类
    protected   void meals(){
        breakfast();//早餐
        lunch();//午餐
        dinner();//晚餐
    }

    abstract void breakfast();

    abstract void lunch();

    abstract void dinner();
}
