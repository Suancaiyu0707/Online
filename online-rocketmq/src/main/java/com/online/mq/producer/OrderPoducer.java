package com.online.mq.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.List;

/***
 *
 * @author zhifang.xu
 * 发送顺序消息
 */
public class OrderPoducer {
    private static final Logger logger = LoggerFactory.getLogger(TransactionProducer.class);
    private final static String nameServer="localhost:19876;localhost:29876";

    private final static String groupName = "order_group_producer";

    private final static String topicName="order_topic";
    private static final DefaultMQProducer producer;

    static {
        producer = new DefaultMQProducer(groupName);
        producer.setNamesrvAddr(nameServer);
        //设置消息失败重试次数
        producer.setRetryTimesWhenSendFailed(5);
        //设置消息失败后是否需要更换broker来重试
        producer.setRetryAnotherBrokerWhenNotStoreOK(true);
        //设置消息发送超时时间
        producer.setSendMsgTimeout(500000);
        //限制消息最大大小
        producer.setMaxMessageSize(1024*1024*50);
    }

    public static void main(String[] args) throws Exception {

        producer.start();
        for(int i=0;i<100;i++){
            Message message = new Message();
            message.setTags("order_tag");
            message.setTopic(topicName);
            message.setBody(("hi xuzf,i am order:"+i).getBytes(Charset.defaultCharset()));
            message.setKeys("order_"+i);
            //是否等消息存储完成后再返回
            message.setWaitStoreMsgOK(true);
            //注意，顺序消费必须根据一定的业务规则，将有顺序关系的消息发送到同一MessageQueue
            producer.send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                    //这个o其实就是外面传过来的aa，可以用来作为选举MessageQueue的条件
                    System.out.println(o);
                    //这边我写死所有的消息只发送到队列0里
                    return list.get(0);
                }
            },"aa");
        }
        producer.shutdown();
    }
}
