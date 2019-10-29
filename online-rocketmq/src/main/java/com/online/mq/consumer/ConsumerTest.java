package com.online.mq.consumer;

import com.google.gson.Gson;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.consumer.store.OffsetStore;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.hook.FilterMessageContext;
import org.apache.rocketmq.client.hook.FilterMessageHook;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class ConsumerTest {
    public static void main(String[] args) throws InterruptedException, MQClientException {

        /*
         * Instantiate with specified consumer group name.
         */
        //第2个参数表示是否可以跟踪消息，如果传递true，则表示将会对消息进行跟踪
        final DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("please_rename_unique_group_name_4",true);

        consumer.setNamesrvAddr("localhost:19876;localhost:29876");

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //向消费者注册过滤消息的filter
        consumer.getDefaultMQPushConsumerImpl().registerFilterMessageHook(new FilterMessageHook() {
            @Override
            public String hookName() {
                return null;
            }

            @Override
            public void filterMessage(FilterMessageContext filterMessageContext) {

            }
        });
        /*
         * Subscribe one more more topics to consume.
         */
        consumer.subscribe("sametopic", "*");

        /*
         *  Register callback to execute on arrival of messages fetched from brokers.
         *  这里要注意，如果是顺序消费的话MessageListenerOrderly
         */
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
                //如果是集群的模式，默认是RemoteBrokerOffsetStore
                //如果是广播模式，则默认是LocalFileOffsetStore
                //一个消费者组一个OffsetStore
                OffsetStore store = consumer.getDefaultMQPushConsumerImpl().getOffsetStore();
                System.out.println(new Gson().toJson(msgs.get(0)));
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }

        });
        /***
         * 用来指定他是顺序消费
         */
//        consumer.registerMessageListener(new MessageListenerOrderly(){
//
//            @Override
//            public ConsumeOrderlyStatus consumeMessage(List <MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
//                return ConsumeOrderlyStatus.SUCCESS;
//            }
//        });

        /*
         *  Launch the consumer instance.
         */
        consumer.start();

        System.out.printf("Consumer Started.%n");
    }
}
