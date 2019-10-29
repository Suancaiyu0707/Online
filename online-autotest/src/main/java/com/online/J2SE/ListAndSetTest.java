package com.online.J2SE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListAndSetTest {

    public static void main(String[] args) {
        List<String>list =new ArrayList<String>();
        Set<String> s =new HashSet<String>();

        list.add("aa");
        list.add("aa");
        list.add("cc");
        list.add("bb");
        System.out.println(list.size());//4，允许重复
        System.out.println(list);//因为list的元素是有序的，所以就是插入的顺序
        s.add("aa");
        s.add("aa");
        s.add("c");
        s.add("bb");
        System.out.println(s.size());//3，不允许重复，因为它底层是放到一个hashMap里，hashMap的key是set里的元素，所以如果遇到key相同的话，会被覆盖掉。
        System.out.println(s);//因为list的元素是无序的，，所以不一定是插入的顺序，因为它底层是放到一个hashMap里，key在哈希的顺序是在哈希槽里，哈希槽是根据key的hashCode计算出来的
    }
}
