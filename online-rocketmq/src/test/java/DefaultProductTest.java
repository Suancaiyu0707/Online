import com.online.mq.producer.OnlineProducerFactory;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DefaultProductTest {
    private static final Logger logger = LoggerFactory.getLogger(DefaultProductTest.class);

    public static void main(String[] args) throws Exception {
        DefaultMQProducer defaultMQProducer = OnlineProducerFactory.getRocketMQProducer();
        send(defaultMQProducer);
    }
    /**
     * 发送消息
     *
     * @throws InterruptedException
     * @throws MQBrokerException
     * @throws RemotingException
     * @throws MQClientException
     */

    public static void send(DefaultMQProducer defaultMQProducer ) throws MQClientException, RemotingException, MQBrokerException, InterruptedException{
        String msg = "demo msg test";
        logger.info("开始发送消息："+msg);
        Message sendMsg = new Message("DemoTopic","DemoTag",msg.getBytes());
        //获得某个主题下所有的队列
        List<MessageQueue>list =  defaultMQProducer.fetchPublishMessageQueues("topicName");
        //默认3秒超时
        SendResult sendResult = defaultMQProducer.send(sendMsg);
        logger.info("消息发送响应信息："+sendResult.toString());
    }
}
