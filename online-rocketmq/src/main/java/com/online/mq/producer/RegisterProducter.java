package com.online.mq.producer;

import com.online.mq.hook.OnlineRpcHook;
import com.online.mq.hook.OnlineSendMessageHook;
import com.online.mq.hook.SendMessageExceptionHook;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.hook.CheckForbiddenContext;
import org.apache.rocketmq.client.hook.CheckForbiddenHook;
import org.apache.rocketmq.client.hook.SendMessageContext;
import org.apache.rocketmq.client.hook.SendMessageHook;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.common.sysflag.MessageSysFlag;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class RegisterProducter {
        public static void main(String[] args) throws MQClientException, InterruptedException, UnsupportedEncodingException {

            int sysFlag = 0;
           // System.out.println(sysFlag |= 0x1);
            System.out.println(sysFlag |= (0x1 << 2));//4
            System.out.println(sysFlag);
            /*
             * Instantiate with a producer group name.
             */
            /***
             * OnlineRpcHook的doBeforeRequest方法会在发送消息前调用，可以用来记录消息详情，比SendMessageHook.sendMessageBefore晚调用
             *
             * OnlineRpcHook的doBeforeRequest方法会在发送消息成功后调用，可以用来记录消息发送结果，比SendMessageHook.sendMessageAfter早调用，只要发送成功响应，才会被调用，比SendMessageHook.sendMessageAfter早调用
             */
            DefaultMQProducer producer = new DefaultMQProducer("register_group",new OnlineRpcHook());

            producer.setNamesrvAddr("localhost:19876;localhost:29876");
            //可以通过DefaultMQProducerImpl注册校验的hook钩子函数
            producer.getDefaultMQProducerImpl().registerCheckForbiddenHook(new CheckForbiddenHook() {
                @Override
                public String hookName() {
                    return null;
                }
                //该方法会在发送消息前被调用，可以用来校验消息的合法性
                @Override
                public void checkForbidden(CheckForbiddenContext checkForbiddenContext) throws MQClientException {
                    System.out.println("可以注册消息校验的hook");
                }
            });
            //可以通过DefaultMQProducerImpl注册消息发送的hook钩子函数
            producer.getDefaultMQProducerImpl().registerSendMessageHook(new SendMessageHook() {
                @Override
                public String hookName() {
                    return null;
                }
                //该方法会在发送消息前被调用，比CheckForbiddenHook晚执行，可以记录日志
                @Override
                public void sendMessageBefore(SendMessageContext sendMessageContext) {

                }
                //该方法会在发送消息后被调用，就算是发送异常也会调用
                @Override
                public void sendMessageAfter(SendMessageContext sendMessageContext) {

                }
            });


            producer.start();

            for (int i = 0; i < 10; i++) {
                try {

                    /*
                     * Create a message instance, specifying topic, tag and message body.
                     */
                    Message msg = new Message("TopicTest" /* Topic */,
                            "TagA" /* Tag */,
                            ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
                    );

                   // msg.putUserProperty(MessageConst.PROPERTY_DELAY_TIME_LEVEL,"1");//可以设置延迟等级，它要和SendMessageHook一起配合使用，在发送消息前，会把消息类型设置为延迟消息
                    /*
                     * 消息发送失败默认会重试3次，那么后面的线程就会出现阻塞
                     */
                    SendResult sendResult = producer.send(msg);

                    //批量发送消息
                    producer.send(new ArrayList <>());

                    System.out.printf("%s%n", sendResult);
                } catch (Exception e) {
                    e.printStackTrace();
                    Thread.sleep(1000);
                }
            }


            for (int i = 0; i < 10; i++) {
                try {

                    /*
                     * Create a message instance, specifying topic, tag and message body.
                     */
                    Message msg = new Message("TopicTest_Asyn" /* Topic */,
                            "TagA_Asyn" /* Tag */,
                            ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
                    );

                    //异步发送，消息会交给异步处理的线程池处理
                    //线程池默认大小是当前服务器的核数，然后队列长度是50000
                    //异步发送消息失败并不会重试
                    producer.send(msg, new SendCallback() {
                        @Override
                        public void onSuccess(SendResult sendResult) {
                            System.out.printf("%s%n", sendResult);
                        }

                        @Override
                        public void onException(Throwable throwable) {
                            System.out.println("发送失败:"+throwable.getCause());
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    Thread.sleep(1000);
                }
            }
            List<Message> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                /*
                 * Create a message instance, specifying topic, tag and message body.
                 */
                Message msg = new Message("TopicTest_batch" /* Topic */,
                        "TagA_batch" /* Tag */,
                        ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
                );
                list.add(msg);
            }
            try {
                //批量发送消息，它其实和发一条消息一样，发送失败后也会重试3次
                producer.send(list);


            } catch (Exception e) {
                e.printStackTrace();
                Thread.sleep(1000);
            }
            /*
             * Shut down once the producer instance is not longer in use.
             */
            producer.shutdown();
        }

}
