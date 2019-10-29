package com.online.netty.jianan_study;

public class MethodTest {
    void invoke(Object obj,Object...objs){
        System.out.println("i am two params");
    }

    String invoke(String obj,Object obj2,Object...objs){
        System.out.println("i am three params");
        return "";
    }

    public static void main(String args[]){
        MethodTest test = new MethodTest();
        test.invoke(null,1 );
        test.invoke(null,1 ,2);
        test.invoke(null,1 ,2,3);
        test.invoke(null,new Object[]{1,2});
    }
}
