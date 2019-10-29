package com.online.mq.producer;

import com.online.mq.listener.TransactionListenerImpl;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.concurrent.*;

/***
 * @author zhifang.xu
 * 发送事务消息
 */
public class TransactionProducer {
    private static final Logger logger = LoggerFactory.getLogger(TransactionProducer.class);
    private final static String nameServer="localhost:19876;localhost:29876";

    private final static String groupName = "tans_group_producer";

    private final static String topicName="trans_topic";

    private static final TransactionMQProducer producer;

    private static final TransactionListener listener = new TransactionListenerImpl();

    static{
        producer = new TransactionMQProducer(groupName);
        producer.setNamesrvAddr(nameServer);
        //消息发送失败重试次数
        producer.setRetryTimesWhenSendAsyncFailed(5);
        //消息没有存储成功，是否发送到另一个Broker中
        producer.setRetryAnotherBrokerWhenNotStoreOK(true);
        //用于执行本地事务和在状态丢失时，broker回查消费者
        producer.setTransactionListener(listener);
        ExecutorService executorService = new ThreadPoolExecutor(2, Runtime.getRuntime().availableProcessors(), 100, TimeUnit.SECONDS,
                new ArrayBlockingQueue <>(2000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("client-transaction-msg-check-thread");
                return thread;
            }
        });
        //为本地事务执行设置合适的线程池
        producer.setExecutorService(executorService);
    }

    public static void main(String[] args) throws Exception {
        producer.start();
        for(int i=0;i<100;i++){
//            Message msg = new Message(topicName,"trans_tag",("hello xuzf,look at me "+i)
//                    .getBytes(Charset.forName("UTF-8"))
//            );
            Message message= new Message();
            message.setTopic(topicName);
            message.setTags("trans_tag");
            message.setBody(("hello xuzf,look at me "+i).getBytes(Charset.defaultCharset()));
            message.setKeys("trans_:"+i);
            //这个属性通常用在消费者进行幂等时用来过滤重复消费
            message.setTransactionId("transactionId_:"+i);
            SendResult sendResult =producer.sendMessageInTransaction(message,null);
            logger.info("sendResult={}",sendResult);
            TimeUnit.MICROSECONDS.sleep(10);
        }
//        for (int i = 0; i < 100000; i++) {
//            Thread.sleep(1000);
//        }

        Thread.sleep(1000*60*60*24);
        producer.shutdown();
    }
}
