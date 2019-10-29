package com.online.netty.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/***
 * //SimpleChannelInboundHandler会在消息被channelRead0 消费完后 自动释放资源
 * @param <ByteBuf>
 */
public class OnlineEncodeChannelHandler<ByteBuf> extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf t) throws Exception {
        Channel channel =channelHandlerContext.channel();
        System.out.println(channel.eventLoop());
        System.out.println(channel.pipeline());
    }
}
