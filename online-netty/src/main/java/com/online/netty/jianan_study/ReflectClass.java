package com.online.netty.jianan_study;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/***
 * 我们都知道，类是在被编译成.class文件之后才会被虚拟机加载进去。但是我们也要注意：
 *      虚拟机程序在启动时候，并不会一下就把所有的类都家在到内存里。而是在真正调用这个类的时候才会加载这个编译好的ReflectClass.class文件。
 *      .class文件被加载到虚拟机内存(方法区)后，虚拟机会为在内存里为每一个已经加载好的类创建一个Class对象,里面包含这个被加载的类的信息。
 *      但程序要创建一个对象的时候，它其实是通过Class对象通过反射，然后创建一个对象，也就是根据这个Class对象，会在堆内存里分配一块内存用来代表这个新建的对象。
 */
public class ReflectClass {
    public static void main(String args[]) throws NoSuchMethodException, InvocationTargetException {

        //在方法区里的Class对象的名称，它其实是一个路径
        System.out.println(AA.class.getName());//jianan_study.AA
        AA a = new AA();
        //class jianan_study.AA
        System.out.println(a.getClass());//获得代表这个类的Class对象
        // jianan_study.AA
        System.out.println(a.getClass().getName());//获得代表这个类的Class对象的名称


        try {
            //在方法区里的Class对象的名称,找到这个代表类的Class对象
            //获得Class对象的第一种方式
            Class cl = Class.forName("jianan_study.AA");
           Field[] fileds =  cl.getFields();
           for(Field f:fileds){
               if("age".equals(f.getName())){
                   System.out.println("修改之前:"+f.getName()+":"+f.getInt(a));
                   System.out.println("我要开始修改age了");
                   f.setInt(a,30);
                   System.out.println("我结束修改age了");
                   System.out.println("修改之后:"+f.getName()+":"+f.getInt(a));
               }

           }
            Method[] methods = cl.getDeclaredMethods();
            for(Method m:methods){
                if(m.getName().equals("m")){//找到对应的方法，然后加一些日志
                    System.out.println("我是日志，我开始执行方法m");
                    System.out.println(m.getName()+":"+m.invoke(a,null));
                    System.out.println("我是日志，我结束执行方法m");
                }

            }
            cl.newInstance();
            //获得Class对象的第二种方式

            Class c2 =a.getClass();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
