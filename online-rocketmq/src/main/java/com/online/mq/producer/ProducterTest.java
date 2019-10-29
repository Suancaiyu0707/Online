package com.online.mq.producer;

import com.online.mq.hook.OnlineRpcHook;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class ProducterTest {
        public static void main(String[] args) throws MQClientException, InterruptedException {

            /*
             * Instantiate with a producer group name.
             */

            DefaultMQProducer producer = new DefaultMQProducer("commmonProducer");

            producer.setNamesrvAddr("localhost:19876;localhost:29876");
            producer.setSendMsgTimeout(1000*60*60*24);
            producer.start();


            for (int i = 0; i < 10; i++) {
                try {

                    /*
                     * Create a message instance, specifying topic, tag and message body.
                     */
                    Message msg = new Message("sametopic" /* Topic */,
                            "TagA" /* Tag */,
                            ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
                    );

                    /*
                     * Call send message to deliver message to one of brokers.
                     */
                    SendResult sendResult = producer.send(msg);

                    System.out.printf("%s%n", sendResult);
                } catch (Exception e) {
                    e.printStackTrace();
                    Thread.sleep(1000);
                }
            }

            /*
             * Shut down once the producer instance is not longer in use.
             */
            producer.shutdown();
        }

}
