package com.online.mq.producer;

import com.online.filter.ListSplitter;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/***
 *
 * @author zhifang.xu
 * 发送批量消息
 */
public class BatchProducer {
    private final static String nameServer="localhost:19876;localhost:29876";

    private final static String groupName = "batch_group_producer";

    private final static String topicName="batch_topic";

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
        //将所有的消息添加到一个list里
        List<Message> list = new ArrayList<>();
        for(int i=0;i<10;i++){
            Message message = new Message();
            message.setTags("batch_tag");
            message.setTopic(topicName);
            message.setBody(("hi xuzf,i am give you love:"+i).getBytes(Charset.defaultCharset()));
            message.setKeys("batch_"+i);
            //是否等消息存储完成后再返回
            message.setWaitStoreMsgOK(true);
            list.add(message);
        }
        //发送消息时，传的参数一个list，则就会批量发送消息
        //把大批量的消息切分成满足batch的最大限制的多份小批量信息
        ListSplitter splitter = new ListSplitter(list);
        while (splitter.hasNext()) {
            List<Message> listItem = splitter.next();
            producer.send(listItem);
        }

        producer.shutdown();
    }
}
