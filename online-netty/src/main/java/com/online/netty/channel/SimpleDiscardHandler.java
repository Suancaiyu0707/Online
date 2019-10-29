package com.online.netty.channel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/***
 *  由于 SimpleChannelInboundHandler 会自动释放资源，
 *  所以你不应该存储指向任何消 息的引用供将来使用，因为这些引用都将会失效。
 *  SimpleChannelInboundHandler继承了ChannelInboundHandlerAdapter
 */
public class SimpleDiscardHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

    }
}
