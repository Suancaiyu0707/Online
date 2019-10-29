package com.online.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

//SimpleChannelInboundHandler会在消息被channelRead0 消费完后 自动释放资源
//所以不用存储任何消息的引用供将来使用，因为这些引用都将会失效
@ChannelHandler.Sharable
public class OnlineClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    /***
     * 在到服务器的连接已经建立之后将被调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx);


        Channel channel = ctx.channel();
        //60秒之后，没个60s发送一次定时任务
        channel.eventLoop().scheduleAtFixedRate(new Runnable(){
            @Override
            public void run() {
                System.out.println("hello i am client!,now="+format.format(System.currentTimeMillis()));
                //连接成功后，开始往服务端写数据
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello i am client!,now="+format.format(System.currentTimeMillis()),CharsetUtil.UTF_8));
            }
        },30,30, TimeUnit.SECONDS);
    }

    /***
     * SimpleChannelInboundHandler会自动释放资源，所以不应该存储指向任何消息的应用供将阿里使用，因为这些引用都将会失效
     * @param ctx
     * @param msg
     * @throws Exception
     */
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println(ctx);
//        ByteBuf buf = (ByteBuf)msg;
//        System.out.println("client receive msg from server :"+ buf.toString(CharsetUtil.UTF_8));
//        super.channelRead(ctx, msg);
//    }

    /***
     * 当从服务器接收到一条信息时被调用
     * 需要注意的是，服务器发送的消息有可能会被分块接收
     * 当该方法返回时，SimpleChannelInboundHandler负责释放指向保存该消息的ByteBuf的内存引用,这里相当于是msg
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println(ctx);
        System.out.println("hello channelRead0");
        System.out.println("client receive msg from server :"+ msg.toString(CharsetUtil.UTF_8));
    }

    /***
     * 在读取完成后被调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("hello channelRead");
        super.channelReadComplete(ctx);
    }

    /***
     * 在处理过程中发生异常时被调用
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
