package com.online.netty.jianan_study;

public class ObjectTest {
    public static void main(String args[]){
        A a = new A();
        System.out.println(a);//实际上调用了a.toString()方法
        System.out.println(a.toString());
        System.out.println(a+"1");//实际上先调用了a.toString()方法，然后和"1"拼接
        System.out.println("1"+a);//实际上先调用了a.toString()方法，然后和"1"拼接
    }
}

class A {
    public String name="张迦南";
    public int age = 23;
    @Override
    public String toString(){
        return "zhangjianan 23! ";
    }

}
