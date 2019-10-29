package com.online.J2SE;

/***
 * 置换变量
 */
public class SwitchVariableTest {

    public static void main(String[] args) {
        int a =10;int b =2;
        switch1(a,b);

        System.out.println( Integer.toBinaryString(10));

    }
    private static void switch1(int a,int b){
        int c= a;
        a=b;
        b=c;
        System.out.println("a="+a+";b="+b);
    }

    private static void switch2(int a,int b){
       a = a+b;//缺点是在这一步的时候可能会越界
       b = a-b;//b=a+b-b
       a=a-b;//b=a+b-b
        System.out.println("a="+a+";b="+b);
    }

    private static void switch3(int a,int b){
        a = a^b;//异或 效率高且不会越界
        b = a^b;//b=a+b-b
        a = a^b;//b=a+b-b
        System.out.println("a="+a+";b="+b);
    }
}
