package com.online.kafka.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
/***
 * 创建一个消费者，并采用指定分区
 * 		优点是：不受再均衡影响
 * 		缺点是：当有新分区加入，并不会收到通知
 * 
 *	一个消费者可以订阅主题(并加入消费群组),也可为自己分区，但是不能同时作这两件事
 * @author xuzhifang
 *
 */
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

public class ConsumerWithAssignPartition {
	private static  KafkaConsumer<String, String> consumer;
	private static List<TopicPartition> partitions = new ArrayList<TopicPartition>();
	

	static {
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:2181");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

		consumer = new KafkaConsumer<String, String>(props);
//		ArrayList<String>  topicList = new ArrayList<String>();
//		topicList.add("test_topic");
//		consumer.subscribe(topicList);
	}
	public static void main(String[] args) {
		List<PartitionInfo> partitionInfos = consumer.partitionsFor("test_topic");
		if(partitionInfos!=null) {
			for(PartitionInfo info:partitionInfos) {
				partitions.add(new TopicPartition(info.topic(), info.partition()));
			}
			//调用assign指定分配的分区
			//通过subscribe订阅的消费有自动再均衡的功能，而通过assign没有
			consumer.assign(partitions);
			
			while(true) {
				ConsumerRecords<String, String> records = consumer.poll(1000);
				for(ConsumerRecord<String, String> record : records) {
					System.out.println("topic ="+record.topic()+", partition="+record.partition()+", offset="+record.offset()+" customer="+record.key()+", country="+record.value()
							+"\n");
				}
				consumer.commitSync();
			}
		}
	}
}
