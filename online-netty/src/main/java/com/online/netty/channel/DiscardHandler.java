package com.online.netty.channel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/***
 * ChannelInboundHandlerAdapter并不会自动释放资源，所以要显式的释放资源
 * 当某个 ChannelInboundHandler 的实现重写 channelRead()方法时，它将负责显式地 释放与池化的 ByteBuf 实例相关的内存
 *
 * SimpleChannelInboundHandler会自动释放资源
 */
public class DiscardHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //super.channelRead(ctx, msg);
        System.out.println("DiscardHandler=============");
        ReferenceCountUtil.release(msg);//释放资源
    }
}
