package com.online.mq.consumer;

import com.google.gson.Gson;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.store.OffsetStore;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
/**
 * 集群：消费者2
 * @author zhifang.xu
 */
public class ClusterConsumer2 {

    public static void main(String[] args) throws Exception {
        /*
         * Instantiate with specified consumer group name.
         */
        //第2个参数表示是否可以跟踪消息，如果传递true，则表示将会对消息进行跟踪
        final DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("cluster_group",true);

        consumer.setNamesrvAddr("localhost:19876;localhost:29876");

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        /*
         * Subscribe one more more topics to consume.
         */
        consumer.subscribe("clusrer_topic", "*");
        /*
         *  Register callback to execute on arrival of messages fetched from brokers.
         *  这里要注意，如果是顺序消费的话MessageListenerOrderly
         */
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    System.out.printf("Consumer2: %s Receive New Messages: %s %n", Thread.currentThread().getName(), msg);
                    //如果是集群的模式，默认是RemoteBrokerOffsetStore
                    //如果是广播模式，则默认是LocalFileOffsetStore
                    //一个消费者组一个OffsetStore
                    //OffsetStore store = consumer.getDefaultMQPushConsumerImpl().getOffsetStore();
                    //System.out.println(new Gson().toJson(store));
//                try {
//                    Thread.sleep(Integer.MAX_VALUE);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }

        });
        consumer.start();
    }
}
