package com.online.J2SE;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ArrayTestAndLinkedListTest {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<Integer>();//ArrayList的底层存储结构是一个内存连续的数组Object[] elementData
        Random random = new Random();
        long beginTime = System.currentTimeMillis();
        for(int i=0;i<=100000;i++){
            //因为内部结构是一个数组，数组的内存是连续的，所以，往尾巴添加元素的话，很快，唯一可能不好的话，会不定期的导致底层内部数组扩容。
            list.add(random.nextInt(1000000));
        }
        System.out.println("耗时:"+(System.currentTimeMillis()-beginTime));
        beginTime = System.currentTimeMillis();
        List<Integer> linkedList = new LinkedList <>();
        for(int i=0;i<=100000;i++){
            //在往尾部添加元素的话，只要修改tail指针和pre指针的引用，不需要做任何的内存复制，所以添加元素的时候，可能会比ArrayList快
            linkedList.add(random.nextInt(1000000));
        }
        System.out.println("耗时:"+(System.currentTimeMillis()-beginTime));


        beginTime = System.currentTimeMillis();
        for(int i=0;i<=100000;i++){
            //因为内部结构是一个数组，数组的内存是连续的，所以，获取指定索引设置元素的话，就跟往头设置一样快。
            list.get(i);
        }
        System.out.println("耗时:"+(System.currentTimeMillis()-beginTime));
        beginTime = System.currentTimeMillis();
        for(int i=0;i<=100000;i++){
            //利用get去设置指定索引的值，因为要一条条的从头或从尾巴遍历，遍历到指定索引i位置后，获取值，所以性能堪忧
            linkedList.get(i);
        }
        System.out.println("耗时:"+(System.currentTimeMillis()-beginTime));
    }
}
