package com.online.mq.producer;

import com.online.mq.hook.OnlineSendMessageHook;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.hook.SendMessageContext;
import org.apache.rocketmq.client.hook.SendMessageHook;
import org.apache.rocketmq.client.impl.producer.DefaultMQProducerImpl;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class OnlineProducerFactory {
    public static final Logger LOGGER = LoggerFactory.getLogger(OnlineProducerFactory.class);
    /**
     * 发送同一类消息的设置为同一个group，保证唯一,默认不需要设置，rocketmq会使用ip@pid(pid代表jvm名字)作为唯一标示
     */
    @SuppressWarnings("static-access")
    @Value("${rocketmq.groupName}")
    private static String groupName;
    @SuppressWarnings("static-access")
    @Value("${rocketmq.namesrvAddr}")
    private static String namesrvAddr;
    /**
     * 消息最大大小，默认4M
     */
    @SuppressWarnings("static-access")
    @Value("${rocketmq.producer.maxMessageSize}")
    private static Integer maxMessageSize ;
    /**
     * 消息发送超时时间，默认3秒
     */
    @SuppressWarnings("static-access")
    @Value("${rocketmq.producer.sendMsgTimeout}")
    private static Integer sendMsgTimeout;
    /**
     * 消息发送失败重试次数，默认2次
     */
    @SuppressWarnings("static-access")
    @Value("${rocketmq.producer.retryTimesWhenSendFailed}")
    private static Integer retryTimesWhenSendFailed;

    public static DefaultMQProducer getRocketMQProducer() throws Exception {
        if (StringUtils.isEmpty(groupName)) {
            throw new Exception("groupName is blank");
        }
        if (StringUtils.isEmpty(namesrvAddr)) {
            throw new Exception("nameServerAddr is blank");
        }
        DefaultMQProducer producer;
        producer = new DefaultMQProducer(groupName);
        producer.setNamesrvAddr(namesrvAddr);
        //如果需要同一个jvm中不同的producer往不同的mq集群发送消息，需要设置不同的instanceName
        //producer.setInstanceName(instanceName);
        if(maxMessageSize!=null){
            producer.setMaxMessageSize(maxMessageSize);
        }
        if(sendMsgTimeout!=null){
            producer.setSendMsgTimeout(sendMsgTimeout);
        }
        //如果发送消息失败，设置重试次数，默认为2次
        if(retryTimesWhenSendFailed!=null){
            producer.setRetryTimesWhenSendFailed(retryTimesWhenSendFailed);
        }

//      设置发送消息前的钩子函数，可以注册多个
        producer.getDefaultMQProducerImpl().registerSendMessageHook(new OnlineSendMessageHook());
//        producer.setCreateTopicKey();
        try {
            producer.start();

            LOGGER.info(String.format("producer is start ! groupName:[%s],namesrvAddr:[%s]"
                    , groupName, namesrvAddr));
        } catch (MQClientException e) {
            LOGGER.error(String.format("producer is error {}"
                    , e.getMessage(),e));
            throw new Exception(e);
        }
        return producer;
    }

}
