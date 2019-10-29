package com.online.J2SE;

import com.sun.btrace.annotations.BTrace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@BTrace(unsafe = true)
public class ListAndMapTest {
    public static void main(String[] args) {
        List<String> list =new ArrayList<String>();
        list.add("aa");//它是一个列表，底层实现是一个链接或数组，是有序的，集合的元素是可以重复的。可以插入多个null元素
        list.add("aa");
        list.add(null);
        list.add(null);
        System.out.println(list.size());
        Map<String,String> map =new HashMap<>();//是key-value形式存储的。底层是用链表加数组的形式存储。键对象不能重复.
        map.put("aa","aa");
        map.put("aa","aa");
        map.put("bb","bb");
        map.put(null,"aa");
        map.put(null,null);
        System.out.println(map.size());

    }
}
