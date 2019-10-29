package com.online.netty.channel;

import io.netty.channel.*;

import java.net.SocketAddress;

/**
 * ChannelHandler 可以通过添加、删除或者替换其他的 ChannelHandler 来实时地修改 ChannelPipeline 的布局
 */
public class ChannelModifyTest {
    public static void main(String[] args) {
       Channel channel = new AbstractChannel(null) {
           @Override
           protected AbstractUnsafe newUnsafe() {
               return null;
           }

           @Override
           protected boolean isCompatible(EventLoop loop) {
               return false;
           }

           @Override
           protected SocketAddress localAddress0() {
               return null;
           }

           @Override
           protected SocketAddress remoteAddress0() {
               return null;
           }

           @Override
           protected void doBind(SocketAddress localAddress) throws Exception {

           }

           @Override
           protected void doDisconnect() throws Exception {

           }

           @Override
           protected void doClose() throws Exception {

           }

           @Override
           protected void doBeginRead() throws Exception {

           }

           @Override
           protected void doWrite(ChannelOutboundBuffer in) throws Exception {

           }

           @Override
           public ChannelConfig config() {
               return null;
           }

           @Override
           public boolean isOpen() {
               return false;
           }

           @Override
           public boolean isActive() {
               return false;
           }

           @Override
           public ChannelMetadata metadata() {
               return null;
           }
       };

       ChannelPipeline channelPipeline = channel.pipeline();

        ChannelHandlerAdapter firstHandler = new ChannelDuplexHandler();
        ChannelHandlerAdapter secondHandler = new ChannelDuplexHandler();

        channelPipeline.addFirst("firstHandler",firstHandler);
        channelPipeline.addFirst("secondHandler",secondHandler);


        channelPipeline.remove("firstHandler");

        channelPipeline.replace("handler2", "handler4", new ChannelDuplexHandler());

    }
}
