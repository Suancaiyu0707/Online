package com.online.J2SE;

public class IntegerTest {
    public static void main(String[] args) {
        //自动封箱
        Integer a = 10;//100会被虚拟机通过Integer.valueOf(10)转换成new Integer(100),因为虚拟机的默认只缓存了-128~127的对象，所以这里会去虚拟机缓存里直接获取new Integer(10)
        Integer b = 10;//100会被虚拟机通过Integer.valueOf(10)转换成new Integer(100),因为虚拟机的默认只缓存了-128~127的对象，所以这里会去虚拟机缓存里直接获取new Integer(10)
        Integer c = 1000;//1000会被虚拟机通过Integer.valueOf(1000)转换成new Integer(1000),因为虚拟机的默认只缓存了-128~127的对象，所以这里会去new一个Integer(1000)
        Integer d = 1000;//1000会被虚拟机通过Integer.valueOf(1000)转换成new Integer(1000),因为虚拟机的默认只缓存了-128~127的对象，所以这里会去new一个Integer(1000)

        System.out.println(a==b);//都是从虚拟机缓存里获取，所以这边获取的是同一个，为true

        System.out.println(c==d);//都是需要在虚拟机堆内存里new一个，所以两个对象是不一样的


    }
}
