package com.online.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ByteBufAllocatorTest {
    public static void main(String[] args) {
        //池化了ByteBuf的实例以提高性能并最大限度地减少内存碎片.
        //此实现使用了一种叫做jemalloc的已被大量现代操作系统所采用的高效方法来分配内存。
        ByteBufAllocator allocator = new PooledByteBufAllocator();
        //不池化ByteBuf的实例，并且在每次调用时都会返回一个新的实例
        ByteBufAllocator allocator2 = new UnpooledByteBufAllocator(false);

        allocator.buffer(1024);//返回一个基于堆或者直接内存 存储的 ByteBuf
        allocator.heapBuffer(1024,1024);//返回一个基于堆内存存储的 ByteBuf
        allocator.directBuffer();//返回一个基于直接内存存储的 ByteBuf
        allocator.compositeBuffer();//返回一个可以通过添加最大到 指定数目的基于堆的或者直接 内存存储的缓冲区来扩展的 CompositeByteBuf

        //可以通过 Channel(每个都可以有一个不同的 ByteBufAllocator 实例)获取
        Channel channel = new NioServerSocketChannel();
        ByteBufAllocator alloc = channel.alloc();



    }
}
