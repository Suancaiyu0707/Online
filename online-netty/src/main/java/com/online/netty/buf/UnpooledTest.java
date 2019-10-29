package com.online.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class UnpooledTest {
    public static void main(String[] args) {
        ByteBuf buf = Unpooled.buffer();//返回一个未池化的基于堆内存存储的ByteBuf

        Unpooled.directBuffer(1024);//返回一个未池化的基于直接内存存储 的 ByteBuf

        Unpooled.wrappedBuffer(buf.array());//返回一个包装了给定数据的 ByteBuf
        //引用计数对于池化实现(如 PooledByteBufAllocator)来说是至关重要的，它降低了 内存分配的开销。
        //减少到该对象的活动引用。当减少到 0 时 该对象被释放，并且该方法返回 true
        //试图访问一个已经被释放的引用计数的对象，将会导致一个 IllegalReferenceCount- Exception。
        boolean released = buf.release();
    }
}
