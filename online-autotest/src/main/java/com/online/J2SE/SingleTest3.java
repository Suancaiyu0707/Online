package com.online.J2SE;

public class SingleTest3 {
    private  static  SingleTest3 singleTest = new SingleTest3();
    private SingleTest3(){

    }

    public static SingleTest3 getSingleTest(){

        return singleTest;
    }
}
