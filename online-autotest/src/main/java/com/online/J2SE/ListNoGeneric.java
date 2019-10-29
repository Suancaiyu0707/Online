package com.online.J2SE;

import java.util.ArrayList;
import java.util.List;

/***
 * List<T>最大的问题是只能放置一中类型
 */
public class ListNoGeneric {
    public static void main(String[] args) {
        //泛型出现之前的集合定义
        //在遍历过程中，遍历没有问题，但是不小心就可能出现类型转换失败
        //非范型的可以副职给任意范型的对象引用
        List a1 = new ArrayList();
        List<Object>a11 = new ArrayList();
        a1.add(new Object());
        a1.add(new Integer(10));
        a1.add(new String("hello world"));
        System.out.println(a1.size());
        //把a1赋值给a2,注意a2和a1的 区别是增加了泛型限制Object
        List<Object>a2 = a1;
        a2.add(new Object());
        a2.add(new Integer(100));
        a2.add(new String("hello world2"));
        System.out.println(a2.size());
        //把a1赋值给a3,注意a3和a1的 区别是增加了泛型限制Integer
        List<Integer>a3 = a1;
        //这段是会报错的，不能把List<Object>赋给List<Integer>
       // List<Integer>a33 = a11;
//        for(Object i:a3){
//            System.out.println(i);
//        }
        a3.add(new Integer(100));
        System.out.println(a3.size());
        //？是通配符，它可以匹配任何字符，它可以接受任何数据类型的集合引用赋值，但是不能修改，因为在编译期无法明确它的类型。但是可以remove和clear
        List<?> a4 =a1;
        System.out.println(a4.size());
        a1.remove(0);
        a4.clear();
        //编译期出现，不允许增加任何元素
        //a4.add(new Object());

    }
}
