package com.online.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ToIntegerDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list) throws Exception {
        if(in.readableBytes()>=4){//检查是否至少4字节可读（一个int的字节长度）
            list.add(in.readInt());//从入站Bytebuf中读取一个int，并将其添加到解码消息的List中
        }

    }
}
