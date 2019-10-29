package com.online.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/***
 * 堆缓冲区：
 * 	最常用的ByteBuf模式，通过分配一个jvm的堆空间，之后的数据都存储在这个堆空间中。
 * 	这种模式被也被称为支撑数组，它能够在没有使用池化的情况下提供快速的分配和释放。
 */
public class HeapBufTest {
    public static void main(String[] args) {
        ByteBuf heapBuf = Unpooled.buffer();//创建一个堆缓冲区
        System.out.println("heapBuf.hasArray()="+heapBuf.hasArray());//如果 ByteBuf 由一个字节数组支撑，则返回 true
        if(heapBuf.isWritable()){//判断是否有字节可被写入
            heapBuf.writeBytes("hello world".getBytes());
        }
        /***
         * 名字以read或者write开头的ByteBuf方法，将会推进区对应的索引，而名称以set或get开头的操作则不会。
         */
        System.out.println("heapBuf.hasArray()="+heapBuf.hasArray());//true
        if(heapBuf.hasArray()){//检查ByteBuf是否有一个支撑数组，对于堆缓冲区，肯定是true
            System.out.println("array="+heapBuf.array().length);//256
            byte[] array = heapBuf.array();//获得支撑数组,也就是开始的容量，默认是个256字节的byte数组
            System.out.println("array.arrayOffset="+heapBuf.arrayOffset());//0
            System.out.println("array.offset="+(heapBuf.arrayOffset()+heapBuf.readerIndex()));//0
            int offset = heapBuf.arrayOffset()+heapBuf.readerIndex();//计算第一个字的偏移量
            System.out.println("========还未开始读========");
            System.out.println("array.readerIndex="+heapBuf.readerIndex());//0
            System.out.println("array.readableBytes="+heapBuf.readableBytes());//11
            System.out.println("array.capacity="+heapBuf.capacity());//256
            int len = heapBuf.readableBytes();//获得可读字节数
            if(heapBuf.isReadable()){//判断是否有字节可被读取
                heapBuf.readByte();//读1一个字节
                heapBuf.markReaderIndex();//标记位置 1
            }

            heapBuf.readByte();
            System.out.println(heapBuf.readerIndex());//2
            heapBuf.resetReaderIndex();//1 resetReaderIndex会将readerIndex重置为上次标记的位置
            System.out.println(heapBuf.readerIndex());//0
            heapBuf.clear();//clear方法会将readerIndex和writerIndex都设置为0
            System.out.println(heapBuf.readerIndex());
            System.out.println("========读完一个字节========");
            System.out.println("array.readerIndex="+heapBuf.readerIndex());//1
            System.out.println("array.readableBytes="+heapBuf.readableBytes());//10 获得可被读取的字节数
            System.out.println("array.writerIndex="+heapBuf.writerIndex());//11
            System.out.println("array.capacity="+heapBuf.capacity());//256 获得ByteBuf可容纳的字节数
            //可以丢弃已经被读过的字节并回收空间。
            //调用discardReadBytes后，可丢弃的空间被变为可写的。readerIndex和writerIndex都会相应的减少，
            //      readerIndex=0，
            //      writerIndex=writerIndex-readerIndex
            //		可读字节 = writerIndex-readerIndex。
            heapBuf.discardReadBytes();
            System.out.println("========清空已读的一个字节========");
            System.out.println("array.readerIndex="+heapBuf.readerIndex());//0
            System.out.println("array.readableBytes="+heapBuf.readableBytes());//10
            System.out.println("array.readableBytes="+heapBuf.writerIndex());//10
            System.out.println("array.capacity="+heapBuf.capacity());//256 获得ByteBuf可容纳的字节数




        }
    }
}
