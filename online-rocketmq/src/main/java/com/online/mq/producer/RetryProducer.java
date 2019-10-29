package com.online.mq.producer;

import com.online.mq.hook.MessageCheckHook;
import com.online.mq.hook.OnlineRpcHook;
import com.online.mq.hook.SendMessageExceptionHook;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

/***
 * 定义重试的producer
 * @author zhifang.xu
 */
public class RetryProducer {
    public static final String nameserverAddr = "localhost:19876;localhost:29876";

    public static final String groupName = "retry_group_producer";
    public static void main(String[] args) throws UnsupportedEncodingException, MQClientException {
        //设置发送消息的钩子函数
//        final DefaultMQProducer producer = new DefaultMQProducer(groupName,new OnlineRpcHook());
        final DefaultMQProducer producer = new DefaultMQProducer(groupName);

        producer.setRetryTimesWhenSendFailed(5);
//
//        producer.getDefaultMQProducerImpl().registerSendMessageHook(new SendMessageExceptionHook());
//
//        producer.getDefaultMQProducerImpl().registerCheckForbiddenHook(new MessageCheckHook());

        producer.setNamesrvAddr(nameserverAddr);

        producer.start();


        for(int i=0;i<20;i++){
            Message message = new Message("retry_Topic","TagET",
                    ("hello RetryProducer: "+i).getBytes(RemotingHelper.DEFAULT_CHARSET));

            try {
                producer.send(message);
            } catch (RemotingException e) {
                e.printStackTrace();
            } catch (MQBrokerException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("关闭虚拟机========");
                producer.shutdown();
            }
        }));

    }
}
