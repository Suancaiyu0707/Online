package com.online.kafka.consumer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.online.kafka.message.bean.User;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

/***
 * 同步提交方式也可以通过回调函数来获得提交结果
 * @author xuzhifang
 *
 */
public class ConsumerWithSyncWIthCallBack {
	public static KafkaConsumer<String,User> consumer = null;
	public static boolean flag = true;
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
		while(flag) {
			ConsumerRecords< String, User>records = consumer.poll(Duration.ofMillis(1000));
			for(ConsumerRecord<String, User> record : records) {
				System.out.println("topic ="+record.topic()+", partition="+record.partition()+", offset="+record.offset()+" customer="+record.key()+", country="+record.value()
				+"\n");
			}
			consumer.commitAsync(new OffsetCommitCallback() {
				
				public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception e) {
					if(e!=null) {
						System.out.println("commit failed for offsets{"+e+"},e={"+e+"}");
					}else {
						Set<TopicPartition> set = offsets.keySet();
						for(TopicPartition topicPartition : set) {
							System.out.println("partition="+topicPartition.partition()+", topic="+topicPartition.topic()+",  offset="+offsets.get(topicPartition));
						}
					}
					
				}
			});
		}
	}

}
