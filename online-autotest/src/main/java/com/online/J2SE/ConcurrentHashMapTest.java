package com.online.J2SE;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapTest {

    public static void main(String[] args) {
        for(int m =0;m<10;m++){
            ConcurrentHashMap<Integer,Integer> map =new ConcurrentHashMap<>();//是key-value形式存储的。底层是用链表加数组的形式存储。键对象不能重复.
            //ConcurrentHashMap
            ConcurrentHashMap<Integer,Integer> concurrentHashMap =new ConcurrentHashMap<>();
            long beginTime = System.currentTimeMillis();
            for(int i=0;i<100000;i++){
                //HashMap：它支持用null作为一个键，这样的键只有一个。它的方法没带synchronized,所以它是非线程安全的，如果多线程对hashMap操作导致扩容，那么就有可能导致死循环。
                map.put(i,i);
            }
            System.out.println("耗时:"+(System.currentTimeMillis()-beginTime));


            beginTime = System.currentTimeMillis();
            for(int i=0;i<100000;i++){
                //ConcurrentHashMap在新增或获取的时候，会对链表上的头节点加锁，所以性能会比较慢点
                concurrentHashMap.put(i,i);
            }
            System.out.println("耗时:"+(System.currentTimeMillis()-beginTime));
            System.out.println("========m="+m);
        }

    }
}
