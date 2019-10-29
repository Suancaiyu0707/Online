package com.online.J2SE;

import java.util.Arrays;
import java.util.List;

public class ArrayTest {
    public static void main(String[] args) {
        String [] arr = new String[5];
        //Arrays.asList返回的ArrayList是不可变的list，它属于Arrays的内部类
        List<String> list = Arrays.asList(arr);
    }
}
