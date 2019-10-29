package com.online.netty.jianan_study;


import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;

/***
 * TreeSet支持自然顺序访问，但是添加、删除、包含等操作要相对低效（log(n)时间)
 *
 *HashSet则是利用哈希算法，理想情况下，如果哈希散列正常，可以常数时间的添加、删除、包含等操作，但是它不保证有序
 *
 * LinkedHashSet内部创建了一个记录插入顺序的双向脸憋，因此提供了按照插入顺序遍历的能力，与此同时，也保证了常数时间的添加、删除、包含等操作，这些操作性能略低于HashSet，因为内部维护了链表的开销
 *
 * 在遍历元素时，HashSet性能受资深容量影响，所以初始化时，除非有必要，不然不要将背后的HashMap容量设置过大。而对于LinkedHashSet,由于其内部链表提供的方便，遍历性能和元素多少有关系
 */
public class TreeMapTes {
    public static void main(String args[]){
        TreeSet<String> set = new TreeSet <String>();

        set.add("b");

        set.add("c");

        set.add("a");

        set.add("d");

        System.out.println(set);


        HashSet <String>hset = new HashSet<String>();
        hset.add("b");

        hset.add("c");

        hset.add("a");

        hset.add("d");

        System.out.println(hset);

        LinkedHashSet<String> lhset = new LinkedHashSet<String>();
        lhset.add("b");

        lhset.add("c");

        lhset.add("a");

        lhset.add("d");

        System.out.println(lhset);

        //Lists.of("a","b","c","d");


        System.out.println("1"+1);
        System.out.println(1+"1");
    }
}
