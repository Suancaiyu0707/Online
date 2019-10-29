package com.online.mq.producer;

import org.apache.rocketmq.client.QueryResult;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.List;

/***
 *
 * @author zhifang.xu
 * 发送延迟消息
 */
public class DelayProducer {
    private static final Logger logger = LoggerFactory.getLogger(TransactionProducer.class);
    private final static String nameServer="localhost:19876;localhost:29876";

    private final static String groupName = "delay_group_producer";

    private final static String topicName="delay_topic";

    private final static int delayLevel = 3;

    private static final DefaultMQProducer producer;
    static{
        producer = new DefaultMQProducer(groupName);
        producer.setNamesrvAddr(nameServer);
        //设置消息失败重试次数
        producer.setRetryTimesWhenSendFailed(5);
        //设置消息失败后是否需要更换broker来重试
        producer.setRetryAnotherBrokerWhenNotStoreOK(true);
        //设置消息发送超时时间
        producer.setSendMsgTimeout(5000);
        //限制消息最大大小
        producer.setMaxMessageSize(1024*1024*50);

    }

    public static void main(String[] args) throws Exception {
        producer.start();
        for(int i=0;i<100;i++){
            Message message = new Message();
            message.setTags("delay_tag");
            message.setTopic(topicName);
            message.setBody(("hi xuzf,sorry,i am delay:"+i).getBytes(Charset.defaultCharset()));
            message.setKeys("delay_"+i);
            //是否等消息存储完成后再返回
            message.setWaitStoreMsgOK(true);
            //设置消息的延迟级别为3，表示延迟10s后发送
            message.setDelayTimeLevel(delayLevel);
            SendResult sendResult=producer.send(message);
            MessageQueue queue = sendResult.getMessageQueue();
            //根据时间戳从队列中查找其偏移量
            long offset = producer.searchOffset(queue,System.currentTimeMillis()-1000);
            //查找该消息队列中最大的物理偏移量
            long maxoffset = producer.maxOffset(queue);
            //查找该消息队列中最小的物理偏移量
            long minoffset = producer.minOffset(queue);
            //根据条件查询消息,最后两位是开始时间和结束时间
            QueryResult result = producer.queryMessage(topicName,"delay_1",5,
                    System.currentTimeMillis()-5000,System.currentTimeMillis());


        }
        //查询该主题下所有的队列
        List<MessageQueue> queueList = producer.fetchPublishMessageQueues(topicName);
        System.out.println("queueList="+queueList);
        producer.shutdown();
    }
}
