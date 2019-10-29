package com.online.J2SE;

import javax.sound.midi.Soundbank;
import java.util.*;

public class ArrayTestAndVectorTest {
    public static void main(String[] args) {

        for (int m=0;m<=1000;m++){
            //ArrayList：它的方法没带synchronized,所以它是非线程安全的不是线程安全
            List<Integer> list = new ArrayList<Integer>();//ArrayList的底层存储结构是一个内存连续的数组Object[] elementData

            long beginTime = System.currentTimeMillis();
            //它是线程安全的，每个方法都带synchronized,所以多线程情况下不会出现并发问题。
            //由于线程的同步必然要影响性能,因此,ArrayList的性能比Vector好（这个其实在1.6之后，如果不存在锁竞争的话，已经不是很明显了，因为1.6之后，对锁锁了很多优化，加入了偏向锁等）
            List<Integer> vector = new Vector <>();
            for(int i=0;i<=100000;i++){
                //在往尾部添加元素的话，只要修改tail指针和pre指针的引用，不需要做任何的内存复制，所以添加元素的时候，可能会比ArrayList快
                vector.add(i);
            }
            System.out.println("耗时:"+(System.currentTimeMillis()-beginTime));

            beginTime = System.currentTimeMillis();
            for(int i=0;i<=100000;i++){
                //因为内部结构是一个数组，数组的内存是连续的，所以，往尾巴添加元素的话，很快，唯一可能不好的话，会不定期的导致底层内部数组扩容。
                list.add(i);
            }
            System.out.println("耗时:"+(System.currentTimeMillis()-beginTime));
            System.out.println("========m="+m);
        }





    }
}
