package com.online.mq.hook;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.hook.CheckForbiddenContext;
import org.apache.rocketmq.client.hook.CheckForbiddenHook;
import org.apache.rocketmq.common.message.Message;

public class MessageCheckHook implements CheckForbiddenHook {
    @Override
    public String hookName() {
        return null;
    }

    @Override
    public void checkForbidden(CheckForbiddenContext checkForbiddenContext) throws MQClientException {
        System.out.println("begin CheckForbiddenHook");
        Message message = checkForbiddenContext.getMessage();
        System.out.println("开始校验消息："+message);
        String str = new String(message.getBody());
        if(str.indexOf("xuzf")!=-1){
            throw new MQClientException(1,"消息不合规");
        }
    }
}
