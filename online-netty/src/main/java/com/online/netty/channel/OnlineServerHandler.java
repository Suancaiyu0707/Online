package com.online.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.EventExecutor;

/***
 * 通过回调触发来处理相应的事件
 * ChannelInboundHandlerAdapter提供了ChannelInboundHandler的默认实现
 *
 * 每个channel都一个与之相关联的ChannelPipeline，该ChannelPipeline持有一个ChannelHandler的实例链。
 */
@ChannelHandler.Sharable//标记一个channelHandler实例可以被多个channel共享
public class OnlineServerHandler extends ChannelInboundHandlerAdapter {
    public OnlineServerHandler() {
        super();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    /***
     * 当一个新的连接被建立时，连接事件会被该方法处理
     * @param ctx ChannelHandlerContext代表了channelHandler和ChannelPipeline之间的关联，所以它是跟channelHandler一一队对应的
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBufAllocator allocator = ctx.alloc();//返回跟channel关联的ByteBufAllocator
        EventExecutor executor = ctx.executor();//返回调度事件的executor
        System.out.println("executor="+executor);
        System.out.println("Client "+ctx.channel().remoteAddress()+" connected!");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    /***
     * 对于每个传入的消息都要调用
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf)msg;//获得传入的消息
        System.out.println("===============channelRead============");
        System.out.println("Server received: "+in.toString(CharsetUtil.UTF_8));
        ctx.write(in);//将接收到的消息写到缓冲区并等待发送给发送者，而不冲刷出站消息
        //当某个chaneleInboundHandler的实现重写了channelRead方法，需要调用 ReferenceCountUtil.release();来显式的释放和池化的ByteBuf实例相关的内存
        //会显式的释放内存
       // ReferenceCountUtil.release(msg);

    }

    /***
     * 通知ChannelInboundHandlerAdapter最后一次对channelRead的调用是当前批量读取中的最后一条消息
     * @param ctx 相当于channel
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        Channel channel =ctx.channel();
        System.out.println(channel.eventLoop());
        System.out.println(channel.pipeline());
        System.out.println(channel.isActive());
        System.out.println(channel.localAddress());

        System.out.println("===============channelReadComplete============");
        //将缓冲区中的消息冲刷到远程节点，并且关闭该Channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
    }

    /***
     * 在读取期间，有异常抛出时会调用,在这里可以对异常进行相应的处理
     *  每个channel都一个与之相关联的ChannelPipeline，该ChannelPipeline持有一个ChannelHandler的实例链。因此如果exceptionCaught方法如果没有被该链上的某处实现，
     *  那么所接收到的异常将会被传递到ChannelPipeline的尾端并被记录
     * @param ctx 相当于channel
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("===============exceptionCaught============");
        //System.out.println("throw exceptionCaught: "+ctx.write());
        System.out.println("throw exceptionCaught: "+cause);
        ctx.close();//关闭该channel
    }
}
