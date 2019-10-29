package com.online.kafka.consumer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Properties;

import com.online.kafka.message.bean.User;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

/***
 * 同步提交会影响吞吐量
 * @author hb
 *
 */
public class ConsumerWithSynCommit {
	public static KafkaConsumer<String,User> consumer = null;
	
	static {
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:2181");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "test_group");
		consumer = new KafkaConsumer<String, User>(props);
		ArrayList<String>  topicList = new ArrayList<String>();
		topicList.add("test_topic");
		consumer.subscribe(topicList);
	}
	
	public static void main(String[] args) {
		while (true) {
			ConsumerRecords<String, User> records = consumer.poll(Duration.ofMillis(1000));
			for(ConsumerRecord<String, User> record:records) {
				System.out.println("topic ="+record.topic()+", partition="+record.partition()+", offset="+record.offset()+" customer="+record.key()+", country="+record.value()
				+"\n");
			}
			try {
				//主动同步提交当前偏移量，如果失败了，会重复消费。
				//消除丢失消息的可能性
				//提交的是最新偏移量，否则如果发生在均衡，从最近一批消息到发生再均衡之间的所有消息都将被重复处理
				consumer.commitSync();//只要没有发生不可恢复的错误，方法会一致尝试直至提交成功，如果失败了，要记得记录日志
			}catch (Exception e) {
				System.out.println("commited failed");//消费成功了，但是提交失败，就可能导致重复消费
			}
			
		}
	}
}
