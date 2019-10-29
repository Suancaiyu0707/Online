package com.online.netty.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

public class SslChannelInitalizer extends ChannelInitializer<Channel> {
    private final SslContext context;

    private final boolean startTls;

    public SslChannelInitalizer(SslContext context, boolean startTls) {
        this.context = context;
        this.startTls = startTls;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        SSLEngine engine = context.newEngine(channel.alloc());

        channel.pipeline().addFirst("ssl",new SslHandler(engine,startTls));
    }
}
