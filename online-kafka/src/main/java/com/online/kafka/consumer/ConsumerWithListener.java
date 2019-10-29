package com.online.kafka.consumer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.online.kafka.listener.ConsumerRebalance;
import com.online.kafka.message.bean.User;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;


/***
 * 通过监听器，处理再均衡事件
 * @author hb
 *
 */
public class ConsumerWithListener {
	public static KafkaConsumer<String,User> consumer = null;
	private static Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<TopicPartition, OffsetAndMetadata>();
	private static int count = 0;
	private static final boolean flag = true;
	static {
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:2181");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "test_group");
		consumer = new KafkaConsumer<String, User>(props);
		
	}
	public static void main(String[] args) {
		try {
		ArrayList<String>  topicList = new ArrayList<String>();
		topicList.add("test_topic");
		consumer.subscribe(topicList,new ConsumerRebalance(consumer, currentOffsets));
		while(flag) {
			ConsumerRecords<String, User> records = consumer.poll(Duration.ofMillis(1000));
			for(ConsumerRecord<String, User> record : records) {
				System.out.println("topic ="+record.topic()+", partition="+record.partition()+", offset="+record.offset()+" customer="+record.key()+", country="+record.value()
				+"\n");
				TopicPartition partition = new TopicPartition(record.topic(), record.partition());
				//使用期望处理的下一个消息的偏移量更新map里的偏移量，下一次就从这里开始读取消息
				OffsetAndMetadata metadata = new OffsetAndMetadata(record.offset()+1, "no metadata");
				currentOffsets.put(partition, metadata);
				if(count % 1000 ==0) {//每1000次提交一次偏移量
					consumer.commitAsync(currentOffsets,null);
				}
				count++;
			}
		}
	}catch (Exception e) {
		System.out.println("unexpected error,e="+e);
	}finally {
		try {
			consumer.commitSync(currentOffsets);
		} finally {
			
		}
	}
	}

}
