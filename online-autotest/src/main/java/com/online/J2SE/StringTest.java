package com.online.J2SE;

import com.sun.btrace.annotations.BTrace;

@BTrace(unsafe = true)
public class StringTest {
    public static void main(String[] args) {
        String s1="a"+"b"+1;
        String s2= "ab1";
        //==号用来比较内存单元的内容是否相等
        //如果是引用的话，比较的是两个引用的地址值，也就是说两个引用所指向的是同一个对象
        System.out.println(s1==s2);

        String a = "a";//因为它是一个普通变量，没有final修饰，所以有可能通过字节码增强修改它，所以编译期无法确定a是否是不可变的
        final String c ="a";//因为它是一个final变量，不可修改的，所以编译期可以确定它是不可变的
        String b= a+"b";//因为a是可变的，所以无法在编译期进行优化成 ab
        String d =c+"b";//因为c是不可变的，所以可以在编译期进行优化成 ab
        String e ="ab";//这个字符串 ab 是放在常量池里的
        String f = new String (b);//f是一个引用，指向堆里的一个对象new String("ab")
        //f本身是一个指向堆里的一个对象new String("ab")，调用intern后，它会拿出字面量"ab"去常量池里，通过遍历再通过equal比较char[]字符数组，来判断常量池里是否有同样字符串。
        //有的话，返回这个字符串ab在常量池中的地址给g,没有则在常量池新建一个ab，并返回内存地址。所以这里g/h和e都指向了常量池中的同一个内存地址
        //因为调用intern时，它要先遍历一大堆的常量，所以性能比较耗时。而且为了解决并发，通常还要锁的介入。
        //常量池中的字面量会在Full GC的时候回收
        String g = f.intern();
        String h = b.intern();
        System.out.println(b==e);//false
        System.out.println(d==e);//true
        System.out.println(f==e);//false
        System.out.println(f==e);//false
        System.out.println(g==e);//true ==是直接比较的是在常量池中的内存地址，不需要遍历字符
        System.out.println(h==e);//true ==是直接比较的是在常量池中的内存地址，不需要遍历字符
        System.out.println(h==g);//true ==是直接比较的是在常量池中的内存地址，不需要遍历字符


        System.out.println("==============================");

        String m ="";
        String o=null;
        for(int i=0;i<1000;i++){
            o=m;
            m += i;
            System.out.println(m==o);//false,表示m每次都会指向一个新的对象
        }

    }
}
