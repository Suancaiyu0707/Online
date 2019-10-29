package com.online.J2SE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListToArray {
    public static void main(String[] args) {
        List<String> list =new ArrayList<String>();
        list.equals(null);
        list.add("one");
        list.add("two");
        list.add("three");
        //直接调用toArray()会出现泛型丢失
        Object [] array1 = list.toArray();
        System.out.println(Arrays.asList(array1));
        //目标数组的长度小于集合大小,则括号里的入参数组array2会被弃用，并重新分配一个空间，分配完成后返回一个新的数组的引用
        //所以array2里的元素并未改变
        String [] array2 = new String[2];
        list.toArray(array2);
        System.out.println(Arrays.asList(array2));
        //目标数组的长度大于等于集合大小,所以集合会将元素复制到目标数组array3即可
        String [] array3 = new String[3];
        list.toArray(array3);
        System.out.println(Arrays.asList(array3));
    }
}
