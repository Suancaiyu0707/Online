package com.online.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ByteProcessor;

public class LookForByteTest {
    public static void main(String[] args) {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeBytes("hello world, netty  \r!".getBytes());
       // ByteProcessor.IndexOfProcessor processor = new ByteProcessor.IndexOfProcessor();
        int index = buffer.forEachByte(ByteProcessor.FIND_CR);//检查buffer中是否有回车符
        System.out.println(index);
    }
}
