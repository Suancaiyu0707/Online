package com.online.mq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/***
 * @author zhifang.xu
 * 消费重试消息
 */
public class RetryConsumer {
    public static final String nameserverAddr = "localhost:19876;localhost:29876";

    public static final String groupName = "retry_group_consumer";
    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static void main(String[] args) throws MQClientException {
        final DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(nameserverAddr);

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

        consumer.setConsumeTimeout(3000);




        consumer.setConsumeMessageBatchMaxSize(10);
        consumer.setMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list,
                                                            ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                System.out.println(sdf.format(new Date())+" list="+list.size());
                for(MessageExt msg: list){
                    //MessageExt [
                    // queueId=3, //所属consumeQueue的编号
                    // storeSize=189, //存储大小
                    // queueOffset=1, //在consumeQueue中偏移量
                    // sysFlag=0, //消息状态
                    // bornTimestamp=1556162211575,
                    // bornHost=/172.17.24.25:60579,
                    // storeTimestamp=1556162211577, //存储时间
                    // storeHost=/172.17.24.25:11911,
                    // msgId=AC11181900002E870000000002137CE2, //消息msgId，可用来查找消息
                    // commitLogOffset=34831586, //在commitLog中的便宜量
                    // bodyCRC=622647049,
                    // reconsumeTimes=0, //重试次数，如果=0表示非重试
                    // preparedTransactionOffset=0,
                    System.out.println(msg);
                    String body = new String(msg.getBody());
                    System.out.println(body);
//                    try {
//                        consumer.sendMessageBack(msg,3);
//                    } catch (RemotingException e) {
//                        e.printStackTrace();
//                    } catch (MQBrokerException e) {
//                        e.printStackTrace();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (MQClientException e) {
//                        e.printStackTrace();
//                    }
                }
                //int a =1/0;
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.subscribe("retry_Topic","TagET");

        consumer.start();
    }
}
