package com.online.netty.channel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ConnectHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("severSocketChannel成功注册到selector上");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client "+ctx.channel().remoteAddress()+" connected");

        System.out.println("ByteBufAllocator: "+ctx.alloc());
        System.out.println("Channel: "+ctx.channel());
        System.out.println("EventExecutor: "+ctx.executor());
        System.out.println("ChannelHandler: "+ctx.handler());
        System.out.println("Pipelin: "+ctx.pipeline());
    }
}
