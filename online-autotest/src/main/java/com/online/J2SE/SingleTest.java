package com.online.J2SE;

public class SingleTest {
    private  static volatile SingleTest singleTest = null;
    private SingleTest(){

    }
    static {
        singleTest =  new SingleTest();
    }
    public static SingleTest getSingleTest(){
        //先检查singleTest是否为空
        if(singleTest == null){
            synchronized (SingleTest.class){//这边加锁，避免多个线程同时进行创建这个例子
                if(singleTest==null){//安全起见，在获得创建的权限的时候，最好再做一次判断，避免在你获得锁的时候，对象已经被另一个线程创建成功了
                    singleTest = new SingleTest();
                }
            }
        }
        return singleTest;
    }
}
