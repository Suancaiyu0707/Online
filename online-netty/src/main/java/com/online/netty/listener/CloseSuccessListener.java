package com.online.netty.listener;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * 监听关闭成功
 */
public class CloseSuccessListener implements ChannelFutureListener {
    private static final Logger logger = LoggerFactory.getLogger(CloseSuccessListener.class);
    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        System.out.println("close success");
        logger.info("close success");
    }
}
