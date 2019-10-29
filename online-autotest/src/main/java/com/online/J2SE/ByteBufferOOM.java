package com.online.J2SE;

import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;

/***
 * jvm三种配置：
 *      a、 -XX:+PrintGCDetails
 *
 *      b、 -XX:+PrintGCDetails -XX:MaxDirectMemorySize=256m
 *
 *          [GC (System.gc()) [PSYoungGen: 3998K->832K(38400K)] 3998K->840K(125952K), 0.0012538 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 *          [Full GC (System.gc()) [PSYoungGen: 832K->0K(38400K)] [ParOldGen: 8K->726K(87552K)] 840K->726K(125952K), [Metaspace: 3321K->3321K(1056768K)], 0.0045776 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
 *
 *      c、-XX:+PrintGCDetails -XX:MaxDirectMemorySize=256m -XX:+DisableExplicitGC
 *
 *
 */
public class ByteBufferOOM {
    public static void main(String[] args) {
        //ByteBuffer.allocateDirect(257*1024*1024);
        ByteBuffer byeteBuffer =ByteBuffer.allocateDirect(256*1024*1024);
        //((DirectBuffer)byeteBuffer).cleaner().clean();//可以通过这个方法来回收直接内存
        ByteBuffer.allocateDirect(1*1024*1024);
    }
}
