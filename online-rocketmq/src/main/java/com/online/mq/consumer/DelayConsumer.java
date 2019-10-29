package com.online.mq.consumer;

import com.online.mq.producer.TransactionProducer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/***
 *
 * @author zhifang.xu
 * 消费延迟消息
 */
public class DelayConsumer {
    private static final Logger logger = LoggerFactory.getLogger(TransactionProducer.class);
    private final static String nameServer="localhost:19876;localhost:29876";

    private final static String groupName = "delay_group_consumer";

    private final static String topicName="delay_topic";

    private static final DefaultMQPushConsumer consumer;
    static {
        consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(nameServer);
        //设置消费时每次拉取的最大消息是10个(这里是控制最大)
        consumer.setConsumeMessageBatchMaxSize(10);
        //当消费者第一次启动时，从队列头开始消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //消费线程池中最大线程数
        consumer.setConsumeThreadMax(10);
        //消费线程池中最小线程数
        consumer.setConsumeThreadMin(5);

        consumer.setConsumeConcurrentlyMaxSpan(1000);
    }

    public static void main(String[] args) throws Exception {
        consumer.setMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list,
                                                            ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                System.out.println("recevie message:"+list.get(0));
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.subscribe(topicName,"*");
        consumer.start();
    }

}
