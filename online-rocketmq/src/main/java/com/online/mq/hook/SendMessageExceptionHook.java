package com.online.mq.hook;

import com.google.gson.Gson;
import org.apache.rocketmq.client.hook.SendMessageContext;
import org.apache.rocketmq.client.hook.SendMessageHook;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

public class SendMessageExceptionHook implements SendMessageHook {
    @Override
    public String hookName() {
        return null;
    }

    @Override
    public void sendMessageBefore(SendMessageContext sendMessageContext) {
        System.out.println("begin SendMessageHook");
        System.out.println("消息发送开始："+new Gson().toJson(sendMessageContext.getMq()));

    }

    /***
     * 无论是发送成功，还是发送异常都会回调
     */
    @Override
    public void sendMessageAfter(SendMessageContext sendMessageContext) {
        System.out.println("begin SendMessageHook");
        Exception e = sendMessageContext.getException();
        if(e!=null){//就表示发送失败
            e.printStackTrace();
        }else{
            SendResult sendResult =sendMessageContext.getSendResult();
            System.out.println("消息发送完成："+sendResult);
        }
    }
}
