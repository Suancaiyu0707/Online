package com.online.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.PooledByteBufAllocator;

public class ByteBufHolderTest {
    public static void main(String[] args) {
        ByteBufHolder holder = new ByteBufHolder() {
            @Override
            public ByteBuf content() {//返回由这个 ByteBufHolder 所持有的 ByteBuf
                return null;
            }

            @Override
            public ByteBufHolder copy() {//返回这个 ByteBufHolder 的一个深拷贝，包括一个其所包含的 ByteBuf 的非共享拷贝
                return null;
            }

            @Override
            public ByteBufHolder duplicate() {//返回这个ByteBufHolder的一个浅拷贝，包括一个其所包含的ByteBuf的共享拷贝
                return null;
            }

            @Override
            public ByteBufHolder retainedDuplicate() {
                return null;
            }

            @Override
            public ByteBufHolder replace(ByteBuf byteBuf) {
                return null;
            }

            @Override
            public ByteBufHolder retain() {
                return null;
            }

            @Override
            public ByteBufHolder retain(int i) {
                return null;
            }

            @Override
            public ByteBufHolder touch() {
                return null;
            }

            @Override
            public ByteBufHolder touch(Object o) {
                return null;
            }

            @Override
            public int refCnt() {
                return 0;
            }

            @Override
            public boolean release() {
                return false;
            }

            @Override
            public boolean release(int i) {
                return false;
            }
        };
    }
}
