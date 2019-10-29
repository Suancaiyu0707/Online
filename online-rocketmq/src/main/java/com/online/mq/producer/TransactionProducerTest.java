package com.online.mq.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.concurrent.TimeUnit;
@Deprecated
public class TransactionProducerTest {
    public static void main(String[] args) throws Exception {
        String group_name = "transaction_producer";
        final TransactionMQProducer producer = new TransactionMQProducer(group_name);


        producer.setNamesrvAddr("192.168.xx.xxx:9876;192.168.xx.xxx:9876");
        //事务最小并法数
        producer.setCheckThreadPoolMinSize(5);
        //事务最大并发数
        producer.setCheckThreadPoolMaxSize(20);
        //队列数
        producer.setCheckRequestHoldMax(2000);

        producer.start();
        //服务器回调Producer,检查本地事务分支成功还是失败
        //rocketmq会定时的调用这个checklistener,
        //在这里，我们可以根据由MQ回传的key去数据库查询，
        //判断这条数据到底是成功了还是失败了。
        //就是在这个定时check，rocketmq把这个功能在开源的代码中去除掉了。
        producer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message messageExt, Object o) {
                System.out.println("key: " + messageExt.getKeys());
                System.out.println("state--" + new String(messageExt.getBody()));
                // return LocalTransactionState.ROLLBACK_MESSAGE;
                return LocalTransactionState.COMMIT_MESSAGE;
                // return LocalTransactionState.UNKNOW;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                System.out.println("key: " + messageExt.getKeys());
                System.out.println("state--" + new String(messageExt.getBody()));
                // return LocalTransactionState.ROLLBACK_MESSAGE;
                return LocalTransactionState.COMMIT_MESSAGE;
                // return LocalTransactionState.UNKNOW;
            }
        });

        /**
         * 下面这段代码表明一个Producer对象可以发送多个topic, 多个tag的消息
         * 注意：send方法是同步调用，只要不抛异常就标识成功。但是发送成功也可会有多种状态
         * 例如消息写入Master成功，但是Slave不成功，这种情况消息属于成功，但是对于个别应用
         * 如果对消息可靠性要求很高需要对这种情况做处理。另外，消息可能会存在发送失败的情况
         * 失败重试由应用来处理
         */
        TransactionExecuterImpl transactionExecuter = new TransactionExecuterImpl();
        for(int i = 1; i <= 3; i++) {
            Message msg = new Message("TopicTransaction", "Transaction" + i, "key",
                    ("Hello Rocket" + i).getBytes());
            //发送事务消息和同步发送普通消息流程是一样的
            SendResult result = producer.sendMessageInTransaction(msg, transactionExecuter, "tq");
            System.out.println(result);

            try {
                TimeUnit.MICROSECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
         * 应用退出时，要调用shutdown来清理资源，关闭网络连接，从MetaQ服务器上注销自己
         * 注意：我们建议应用在JBOSS,Tomcat等容器的退出钩子里调用shutdown 方法
         */
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                producer.shutdown();
            }
        }));

        System.exit(0);
    }
}
