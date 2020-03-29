package com.online.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.*;

import java.util.List;

public class WebSocketConvertHandler extends MessageToMessageCodec<WebSocketFrame,WebSocketConvertHandler.MyWebSocketFrame> {


    @Override
    protected void encode(ChannelHandlerContext ctx, MyWebSocketFrame msg, List<Object> out) throws Exception {
        ByteBuf payload = msg.getData().duplicate().retain();

        switch (msg.getType()){
            case Binary:
                out.add(new BinaryWebSocketFrame(payload));
                break;
            case Text:
                out.add(new TextWebSocketFrame(payload));
                break;
            case Close:
                out.add(new CloseWebSocketFrame(true,0,payload));
                break;
            case Continuation:
                out.add(new ContinuationWebSocketFrame(payload));
                 break;
            case Ping:
                out.add(new PingWebSocketFrame(payload));
            case Pong:
                out.add(new PongWebSocketFrame(payload));
                break;
            default:
                throw new IllegalAccessException("Unsupprted websocekt msg "+msg);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
        ByteBuf buf=msg.content().duplicate().retain();
        if(msg instanceof BinaryWebSocketFrame){
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.Binary,buf));
        }else if(msg instanceof CloseWebSocketFrame){
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.Close,buf));
        }else if(msg instanceof PingWebSocketFrame){
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.Ping,buf));
        }else if(msg instanceof PongWebSocketFrame){
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.Pong,buf));
        }else if(msg instanceof TextWebSocketFrame){
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.Text,buf));
        }else if(msg instanceof ContinuationWebSocketFrame){
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.Continuation,buf));
        }else{
            throw new IllegalAccessException("Unsupprted websocekt msg "+msg);
        }
    }

    public static final class MyWebSocketFrame {

        public enum FrameType{
            Binary,
            Close,
            Ping,
            Pong,
            Text,
            Continuation
        }

        private final FrameType type;

        private final ByteBuf data;

        public MyWebSocketFrame(FrameType type,ByteBuf data){
            this.type=type;
            this.data=data;
        }

        public FrameType getType() {
            return type;
        }

        public ByteBuf getData() {
            return data;
        }
    }
}
