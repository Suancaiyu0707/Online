package com.online.J2SE;

public class SingleTest2 {
    //因为是单例，所以构造方法必须私有化
    private SingleTest2 (){}
    //提供给外部调用的方法，用来访问这个唯一的单例
    public static SingleTest2 getSingleTest(){
        return A.a;
    }
    //类A只会被加载一次，所以a在类A被加载的时候，会被初始化一次
    private static class A {
        //虽然SingleTest2是private的构造方法，但是A作为SingleTest2的内部类，它是可以访问到它的外部类的私有的方法，所以可以new SingleTest2()
        public static final SingleTest2 a = new SingleTest2();

    }
}

