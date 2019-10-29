package com.online.J2SE;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SoftReferenceHouse {
    public static void main(String[] args) {
        List<SoftReference> houses = new ArrayList<SoftReference>();
        /***

        int i = 0;
        ReferenceQueue phantomReferenceQueue = new ReferenceQueue();
        ReferenceQueue softReferenceQueue = new ReferenceQueue();
        ReferenceQueue weakReferenceceQueue = new ReferenceQueue();
        // houses.add(new House());
        //创建软引用，软引用并非必须的，在系统将要发生内存溢出异常之前，将会把这些对象列入回收范围之中，并进行第二次回收
        SoftReference<House> softReference = new SoftReference<>(new House());
        SoftReference<House> softReference2 = new SoftReference<>(new House(),softReferenceQueue);
        System.out.println("软引用垃圾回收前可以get到="+softReference.get());

        House h = softReference.get();
        if(h==null){
            //在实际场景里，这边可能要访问数据库来重新查询一个对象，所以说这个功能就有点像缓存：如果软引用里能拿到(类似于缓存里能拿到)则直接返回，如果软引用里拿不到(类似于缓存里没有，这个时候就要查数据库重新查询)，则重新获取
            h=new House();
        }
        //创建弱引用,也是用来描述，比软引用还弱，只能生存到下一次垃圾回收发生之前，即使回收后内存会足够，都会回收弱引用关联的对象
        WeakReference<House> weakReference = new  WeakReference<House>(new House());
        WeakReference<House> weakReference2 = new  WeakReference<House>(new House(),weakReferenceceQueue);
        System.out.println("弱引用垃圾回收前可以get到="+weakReference.get());
        //创建虚引用，虚引用，也叫幻引用，你拿不到它的值，但是可以在垃圾回收时收到一个系统通知
        PhantomReference<House> phantomReference = new  PhantomReference<House>(new House(),phantomReferenceQueue);
        System.out.println("虚引用垃圾回收前也不可以get到="+phantomReference.get());
        System.gc();//主动触发垃圾回收
        System.out.println("软引用在垃圾回收时，如果jvm内存足够的话，并不会被回收，所以还是可以get到="+softReference.get());
        System.out.println("弱引用在垃圾回收后一定会被回收，所以已经不可以get到="+weakReference.get());
        System.out.println("虚引用在垃圾回收后一定会被回收，所以已经也不可以get到="+weakReference.get());

        System.out.println("softReferenceQueue="+softReferenceQueue.poll());//软引用，只有在内存不足的时候才会被回收，所以，这里因为还没被回收，所以队列里为空
        System.out.println("weakReferenceceQueue="+weakReferenceceQueue.poll());
        System.out.println("phantomReferenceQueue="+phantomReferenceQueue.poll());
         *
         */
        ReferenceQueue queue = new ReferenceQueue();
        int i =0;
        while (true){
           // houses.add(new House());
            //创建软引用，软引用并非必须的，在系统将要发生内存溢出异常之前，将会把这些对象列入回收范围之中，并进行第二次回收
            SoftReference<House> buyer2 = new SoftReference<>(new House());
//            //创建弱引用
//            WeakReference<House> buyer3 = new  WeakReference<House>(new House());
//            //创建虚引用
//            PhantomReference<House> buyer4 = new  PhantomReference<House>(new House(),queue);

            houses.add(buyer2);
            System.out.println("i="+(++i));

        }
    }
}
/***
 * PSYoungGen:用于回收Young空间
 * ParOldGen：用于回收old空间
 * Metaspace：用于回收元空间
 */
