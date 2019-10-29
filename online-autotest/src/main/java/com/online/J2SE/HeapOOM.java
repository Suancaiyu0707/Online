package com.online.J2SE;

import java.util.ArrayList;
import java.util.List;

public class HeapOOM {
    public static void main(String[] args) {
        List<String> list =new ArrayList<>();
        while (true){
            list.add("每次通过add添加的字符串对象，由于存在list这个对象引用它们而无法被回收，所以最终会导致堆内存溢出 java heap space");

        }
    }
}
