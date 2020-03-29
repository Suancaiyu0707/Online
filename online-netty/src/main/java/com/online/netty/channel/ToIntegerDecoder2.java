package com.online.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class ToIntegerDecoder2 extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //ReplayingDecoder内部的byteBuf实际类型是ReplayingDecoderByteBuf,
        //如果当前in中的没有足够的字节数的话，则会抛出Error
       out.add(in.readInt());
    }
}
