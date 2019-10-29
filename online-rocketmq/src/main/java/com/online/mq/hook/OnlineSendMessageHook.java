package com.online.mq.hook;

import org.apache.rocketmq.client.hook.SendMessageContext;
import org.apache.rocketmq.client.hook.SendMessageHook;

/***
 * 创建发送消息前的钩子函数
 */
public class OnlineSendMessageHook  implements SendMessageHook {
    @Override
    public String hookName() {
        return null;
    }

    @Override
    public void sendMessageBefore(SendMessageContext sendMessageContext) {
        System.out.println("开始发送消息=========");
    }

    @Override
    public void sendMessageAfter(SendMessageContext sendMessageContext) {
        System.out.println("结束发送消息=========");
    }
}
