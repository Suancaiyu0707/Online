package com.online.netty.listener;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * 监听绑定成功
 */
public class BindSuccessListener  implements ChannelFutureListener {
    private static final Logger logger = LoggerFactory.getLogger(BindSuccessListener.class);
    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if(channelFuture.isSuccess()){
            System.out.println("bind success");
            logger.info("bind success");
        }else{
            System.err.println("bind faild");
            logger.info("bind faild");
        }

    }
}
