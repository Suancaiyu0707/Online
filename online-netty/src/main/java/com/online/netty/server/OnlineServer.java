package com.online.netty.server;

import com.online.netty.channel.ConnectHandler;
import com.online.netty.channel.DiscardHandler;
import com.online.netty.channel.OnlineServerHandler;
import com.online.netty.listener.BindSuccessListener;
import com.online.netty.listener.CloseSuccessListener;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;

/***
 * netty实现网络编程
 *  1、支持多种传输类型，包括阻塞和非阻塞
 *  2、简单而强大的线程模型
 *  3、真正的无连接数据报套接字支持
 *  4、性能更酷，延迟更低，这主要是因为线程的池化和复用，拥有更低的资源消耗，最少的内存复制
 *
 *
 */
public class OnlineServer {
    private static final Logger logger = LoggerFactory.getLogger(OnlineServer.class);
    private final int port;

    public OnlineServer(int port){
        this.port=port;
    }

    public static void main(String args[]) throws Exception{

            System.out.println(new SimpleDateFormat(("yyyy-MM-dd")));
        if(args.length!=1){
            System.err.println("Usage: "+OnlineServer.class.getSimpleName()+"<port>");
        }
        int port = 9999;
        new OnlineServer(port).start();
    }

    public void start() throws Exception{
        final OnlineServerHandler serverHandler = new OnlineServerHandler();
        //这个线程池，专门用来接收客户端的连接
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);//默认线程是 CPU 核心数乘以2的创建线程池，用来接受新的连接
        //这个线程池里的连接是分别用来处理不同的客户端的请求I/O事件
        EventLoopGroup workerGroup = new NioEventLoopGroup();//用来处理子channel的I/O事件
        ServerBootstrap b = new ServerBootstrap();

        InetSocketAddress address =new InetSocketAddress(port);
        try {
            //Netty 的服务器端的 acceptor 阶段, 没有使用到多线程,服务器端的 ServerSocketChannel 只绑定到了 bossGroup 中的一个线程,
            b.group(bossGroup,workerGroup)//指定用于接收client请求的acceptor池和处理client端请求的池
                   // .channel(OioSctpServerChannel.class) blocking io
                    //(NioServerSocketChannel.class) no blocking io
                    //channle方法里的参数最终是在 b.bind(port).sync()中用到，bind方法会根据该参数反射创建一个channel对象
                    .channel(NioServerSocketChannel.class)//设置服务端的一个SocketChannel,类似BIO中的serverSocket
                    .childOption(ChannelOption.TCP_NODELAY,true)//childOption表示为分配给客户端的channel连接设置TCP的基本属性
                    .localAddress(address)//服务器将绑定到这个地址以监听新的连接请求
                    //指定绑定到ServerSocketChannel上的ChannelPipeline上的ChannelHandler
                    .handler(new ConnectHandler())
                    //指定将要被添加创建出的socketChannel上的channelPipeline上的channelHandler
                    //一般用来设置客户端连接socketChannel的节本handler
                    .childHandler(new ChannelInitializer<SocketChannel>() {//当一个新的连接被接受时，一个新的子channel将会被创建，而ChannelInitializer将会把你的一个EchoServerHandler实例添加到Channel的ChannelPipeline中
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(serverHandler)
                            .addLast(new DiscardHandler());//将channelHandler绑定到pipeline链上

                        }
                    });

            //bind方法是服务端channel创建的入口
            ChannelFuture future = b.bind(port).sync();//本身b.bind(port)是异步的绑定服务器，所以调用sync方法阻塞等待直到绑定完成
            //future可以注册一个或多个ChannelFutureListener实例，通过监听回调方法operationComplete，在操作完成后进行相应的处理
            future.addListener(new BindSuccessListener());
            Channel channel = future.channel();//获得服务器的channel

//            logger.info("channel active is:"+channel.isActive());
//            System.out.println("channel eventloop is:"+channel.eventLoop());
//            logger.info("channel eventloop is:"+channel.eventLoop());
//            System.out.println("channel pipeline is:"+channel.pipeline());
//            logger.info("channel pipeline is:"+channel.pipeline());
//            System.out.println("channel config is:"+channel.config());
//            logger.info("channel config is:"+channel.config());
            //获取channel的CloseFuture，并且阻塞当前线程直到它完成，也就是阻塞直到服务器的channel关闭
            //这里只是获取服务器的channel的closefuture，并不会关闭closefuture，反而通过sync()可以保证程序不会执行完，也就是服务端不会关闭
            //future.channel().closeFuture()表示等待NioServerSocketChannel关闭，默认是异步的。加上sync就表示同步等待了，这样在没有关闭之前，就不会调用finally的代码
            ChannelFuture closefuture = future.channel().closeFuture().sync();
            closefuture.addListener(new CloseSuccessListener());
        }finally{
            //这两个关闭之后，NioEventLoop也会退出
            bossGroup.shutdownGracefully().sync();//关闭EventLoopGroup并释放资源
            workerGroup.shutdownGracefully();
        }

    }
}
