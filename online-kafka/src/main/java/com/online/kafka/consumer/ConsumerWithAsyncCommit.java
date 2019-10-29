package com.online.kafka.consumer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * 异步提交,可能导致消息丢失
 * @author xuzf
 *
 */
public class ConsumerWithAsyncCommit {
	private static  KafkaConsumer<String, String> consumer;
	static {
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:2181");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "test_group");
		consumer = new KafkaConsumer<String, String>(props);
		ArrayList<String>  topicList = new ArrayList<String>();
		topicList.add("test_topic");
		consumer.subscribe(topicList);
	}

	public static void main(String[] args) {
		try {
			while(true) {
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
				for(ConsumerRecord<String, String> record : records) {
					System.out.println("topic ="+record.topic()+", partition="+record.partition()+", offset="+record.offset()+" customer="+record.key()+", country="+record.value()
							+"\n");
				}
				//在成功提交或碰到无法恢复的错误之前，commitSync会一直尝试，但是commitAsync不会，所以它的可靠性会差一些。
				//为什么设计成不进行重试？主要是因为它是异步提交，在前一次的提交成功之前，可能后一次的异步提交先成功了，
				//这时候如果你继续尝试，可能会出现前面前一次的提交会覆盖后面一次的消费确认
				consumer.commitAsync();
		}

		}catch(Exception e) {
			System.out.println("e="+e);
		}	finally {
			try {
			consumer.commitSync();
			}finally {
				consumer.close();
			}
		}
	}


}
