package com.online.netty.client;

import com.online.netty.channel.OnlineClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.InetSocketAddress;

public class OnlineClient {
    private final String host;

    private final int port;

    public OnlineClient(String host, int port){
        this.host=host;
        this.port=port;
    }

    public void start() throws  Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        //Bootstrap主要为客户端的应用程序创建channel
        Bootstrap b = new Bootstrap();
        b.group(group)//设置用于处理channel所有事件的EventLoopGroup
                .channel(NioSocketChannel.class)//指定了Channel的实现类
                .remoteAddress(new InetSocketAddress(host,port))//指定远程地址，或者可以通过connect来指定它
                //指定被绑定到ChannelPipeline,以接收事件通知的ChannelHandler
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new OnlineClientHandler());
                    }
                });
        ChannelFuture f = b.connect().sync();//连接到远程地址
        f.channel().closeFuture().sync();

    }

    public static void main(String args[]) throws Exception {
       new OnlineClient("127.0.0.1",9999).start();
    }
}
