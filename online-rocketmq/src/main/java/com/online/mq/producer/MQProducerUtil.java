package com.online.mq.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MQProducerUtil {
    private static Logger logger = LoggerFactory.getLogger(MQProducerUtil.class);
    public static void sendMessage(MQProducer producer, Message message) throws
            InterruptedException, RemotingException, MQClientException, MQBrokerException {
        SendResult sendResult = producer.send(message);
        logger.info("消息发送响应信息："+sendResult.toString());
    }


    public static void batchSendMessages(MQProducer producer,List<Message> messages)
            throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        producer.send(messages);

    }
}
