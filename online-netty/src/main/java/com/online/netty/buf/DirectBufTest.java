package com.online.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class DirectBufTest {
    public static void main(String[] args) {
        ByteBuf directBuf = Unpooled.directBuffer();
        directBuf.writeBytes("hello world".getBytes());
        if(!directBuf.hasArray()){//检查内存是否由数组支撑，不是的话说明是直接内存
            System.out.println("directBuf.readableBytes()="+directBuf.readableBytes());//11
            int len = directBuf.readableBytes();
            byte[] array = new byte[len];//分配一个新的数组来保存具有该长度的字节数据
            System.out.println("directBuf.readerIndex()="+directBuf.readerIndex());//0
            directBuf.getBytes(directBuf.readerIndex(),array);//将数据复制到该数组
           // System.out.println("array.arrayOffset="+directBuf.arrayOffset()); 直接内存不支持arrayOffset
          //  System.out.println("array.offset="+(directBuf.arrayOffset()+directBuf.readerIndex()));
        }
    }
}
