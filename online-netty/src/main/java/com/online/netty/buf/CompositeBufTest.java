package com.online.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

public class CompositeBufTest {
    public static void main(String[] args) {
        //CompositeByteBuf可以同时包含堆内存缓冲和直接内存缓冲
        CompositeByteBuf compositeBuf = Unpooled.compositeBuffer();//创建一个复合缓冲区
        compositeBuf.writeBytes("hello compositeBuf".getBytes());
        ByteBuf headerBuf = Unpooled.directBuffer();
        headerBuf.writeBytes("hello world".getBytes());
        ByteBuf bodyBuf = Unpooled.buffer();
        ByteBuf bodyBuf2 = Unpooled.buffer();
        compositeBuf.addComponent(bodyBuf);
        //true  因为此时只有一个ByteBuf且是jvm堆内存空间的。如果是有多个ByteBuf,那么即使都是jvm内存缓冲区，也是false
        System.out.println("compositeBuf.hasArray()="+compositeBuf.hasArray());
        compositeBuf.addComponent(headerBuf);
        System.out.println("compositeBuf.hasArray()="+compositeBuf.hasArray());//false
        compositeBuf.removeComponent(0);
        System.out.println("compositeBuf.hasArray()="+compositeBuf.hasArray());//false
        //true  因为此时只有一个ByteBuf且是直接内存空间的。如果是有多个ByteBuf,那么即使都是直接内存缓冲区，也是false
        System.out.println("compositeBuf.isDirect()="+compositeBuf.isDirect());//true
        for(ByteBuf buf: compositeBuf
                ){
            System.out.println(buf.toString());
            System.out.println("buf.readerIndex()="+buf.readerIndex());//0
            System.out.println("buf.readableBytes()="+buf.readableBytes());//11
            compositeBuf.getBytes(compositeBuf.readerIndex(),new byte[buf.readableBytes()]);//从缓冲区开始可读的位置开始读
        }

        //访问CompositeByteBuf
        int length = compositeBuf.readableBytes();//获得缓冲区中可读的字节大小
        System.out.println("compositeBuf.readableBytes()="+compositeBuf.readableBytes());//0
        byte[] array2 = new byte[length];
        System.out.println("compositeBuf.readerIndex()="+compositeBuf.readerIndex());//0
     //   compositeBuf.getBytes(compositeBuf.readerIndex(),new byte[11]);//从缓冲区开始可读的位置开始读

    }
}
