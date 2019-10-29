package com.online.J2SE;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class HashMapAndTableTest {

    public static void main(String[] args) {
        for(int m =0;m<10;m++){
            Map<Integer,Integer> map =new HashMap<>();//是key-value形式存储的。底层是用链表加数组的形式存储。键对象不能重复.
            Map<Integer,Integer> table =new Hashtable <>();//是key-value形式存储的。底层是用链表加数组的形式存储。值不能为空
            //map.put(1,null);//HashMap值能为null
            //table.put(1,null);//Hashtable的值不能为null
            long beginTime = System.currentTimeMillis();
            for(int i=0;i<100000;i++){
                //HashMap：它支持用null作为一个键，这样的键只有一个。它的方法没带synchronized,所以它是非线程安全的，如果多线程对hashMap操作导致扩容，那么就有可能导致死循环。
                map.put(i,i);
            }
            System.out.println("耗时:"+(System.currentTimeMillis()-beginTime));


            beginTime = System.currentTimeMillis();
            for(int i=0;i<100000;i++){
                //Hashtable：值不能为null。它是线程安全的，因为它的方法都带有synchronize，这样可以保证一次只有一个线程操作。因为加了synchronized，所以性能会差一点。
                table.put(i,i);
            }
            System.out.println("耗时:"+(System.currentTimeMillis()-beginTime));
            System.out.println("========m="+m);
        }

    }
}
