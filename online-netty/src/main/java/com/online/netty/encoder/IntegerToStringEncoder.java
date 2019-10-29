package com.online.netty.encoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class IntegerToStringEncoder extends MessageToMessageDecoder<Integer> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, Integer msg, List<Object> list) throws Exception {
        list.add(String.valueOf(msg));
    }
}
