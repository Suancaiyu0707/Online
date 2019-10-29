package com.online.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

public class DuplicateBufTest {
    public static void main(String[] args) {
        ByteBuf buf = Unpooled.copiedBuffer("netty in ation rocks!",Charset.forName("UTF-8"));
        ByteBuf sliced = buf.slice(0,15);//slice可以避免内存复制，具有自己的readerIndex writerIndex和标记索引，但是内部存储是共享的
        ByteBuf copy = buf.copy(0,15);//内部存储不是共享
        System.out.println(sliced.toString(Charset.forName("UTF-8")));

        buf.setByte(0, (byte)'J');
        System.out.println(buf.getByte(0)==sliced.getByte(0));
        System.out.println(buf.getByte(0)==copy.getByte(0));

    }
}
