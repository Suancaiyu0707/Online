package com.online.mq.consumer;

import com.online.mq.listener.MQConsumeMsgListenerProcessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class OnlineConsumerFactory {
    public static final Logger LOGGER = LoggerFactory.getLogger(OnlineConsumerFactory.class);
    @SuppressWarnings("static-access")
    @Value("${rocketmq.namesrvAddr}")
    private  static String namesrvAddr;
    @SuppressWarnings("static-access")
    @Value("${rocketmq.groupName}")
    private static String groupName;
    @SuppressWarnings("static-access")
    @Value("${rocketmq.consumer.consumeThreadMin}")
    private static int consumeThreadMin;
    @SuppressWarnings("static-access")
    @Value("${rocketmq.consumer.consumeThreadMax}")
    private static int consumeThreadMax;
    @SuppressWarnings("static-access")
    @Value("${rocketmq.consumer.topics}")
    private static String topics;
    @SuppressWarnings("static-access")
    @Value("${rocketmq.consumer.consumeMessageBatchMaxSize}")
    private static int consumeMessageBatchMaxSize;
    @Autowired
    private MQConsumeMsgListenerProcessor mqMessageListenerProcessor;

    public DefaultMQPushConsumer getRocketMQConsumer() throws Exception {
        if (StringUtils.isEmpty(groupName)){
            throw new Exception("groupName is null !!!");
        }
        if (StringUtils.isEmpty(namesrvAddr)){
            throw new Exception("namesrvAddr is null !!!");
        }
        if(StringUtils.isEmpty(topics)){
            throw new Exception("topics is null !!!");
        }
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);
        consumer.registerMessageListener(mqMessageListenerProcessor);
        /**
         * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
         * 如果非第一次启动，那么按照上次消费的位置继续消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        /**
         * 设置消费模型，集群还是广播，默认为集群
         */
        //consumer.setMessageModel(MessageModel.CLUSTERING);
        /**
         * 设置一次消费消息的条数，默认为1条
         */
        consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
        try {
            /**
             * 设置该消费者订阅的主题和tag，如果是订阅该主题下的所有tag，则tag使用*；如果需要指定订阅该主题下的某些tag，则使用||分割，例如tag1||tag2||tag3
             */
            String[] topicTagsArr = topics.split(";");
            for (String topicTags : topicTagsArr) {
                String[] topicTag = topicTags.split("~");
                consumer.subscribe(topicTag[0],topicTag[1]);
            }
            consumer.start(); 
            LOGGER.info("consumer is start !!! groupName:{},topics:{},namesrvAddr:{}",groupName,topics,namesrvAddr);
        }catch (MQClientException e){
            LOGGER.error("consumer is start !!! groupName:{},topics:{},namesrvAddr:{}",groupName,topics,namesrvAddr,e);
            throw new Exception(e);
        }
        return consumer;
    }
}
