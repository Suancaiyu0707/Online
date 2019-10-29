package com.online.J2SE;

public class EqualTest {
    public static void main(String[] args) {
        Integer c = 1000;//1000会被虚拟机转换成new Integer(1000),因为虚拟机的默认只缓存了-128~127的对象，所以这里会去new一个Integer(1000)
        Integer d = 1000;//1000会被虚拟机转换成new Integer(1000),因为虚拟机的默认只缓存了-128~127的对象，所以这里会去new一个Integer(1000)

        System.out.println(c==d);//false，因为==号比较的是在堆内存中地址的值，所以这里是不等的
        System.out.println(c.equals(d));//true，equals其实是调用里Integer的equals方法，所以这边会返回true
    }
}
