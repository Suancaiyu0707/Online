package com.online.mq.hook;

import com.google.gson.Gson;
import org.apache.rocketmq.client.hook.SendMessageContext;
import org.apache.rocketmq.client.hook.SendMessageHook;
import org.apache.rocketmq.remoting.RPCHook;
import org.apache.rocketmq.remoting.protocol.RemotingCommand;

/***
 * 创建发送消息前的钩子函数
 */
public class OnlineRpcHook implements RPCHook {
    @Override
    public void doBeforeRequest(String s, RemotingCommand remotingCommand) {
        System.out.println("开始请求remotingCommand："+new Gson().toJson(remotingCommand));
    }

    /***
     * 只有发送成功才会回调
     */
    @Override
    public void doAfterResponse(String s, RemotingCommand remotingCommand, RemotingCommand remotingCommand1) {
        System.out.println("结束请求remotingCommand："+new Gson().toJson(remotingCommand));
    }
}
