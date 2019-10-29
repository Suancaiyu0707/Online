package com.online.kafka.consumer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

import avro.shaded.com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.serialization.StringDeserializer;

public class Conumer2 {
	private static  KafkaConsumer<String, String> consumer;


	static {
		Properties props = new Properties();
		 props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:2181");
		  props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		  props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		  props.put(ConsumerConfig.GROUP_ID_CONFIG, "test_group");
		  consumer = new KafkaConsumer<String, String>(props);
	}
	public static void main(String[] args) {
		consumer.subscribe(Lists.newArrayList());
		while(true) {
			ConsumerRecords<String, String> records= consumer.poll(Duration.ofMillis(100));
			for(ConsumerRecord<String, String> r : records) {
				
			}
		}
	}

	public static List<PartitionInfo> partitionsFor(String topic){
		List<PartitionInfo> result = consumer.partitionsFor(topic);

		for(PartitionInfo partitionInfo:result){
			//打印出ISR集合
			Node[] in_notes=partitionInfo.inSyncReplicas();
			System.out.println(new Gson().toJson(in_notes));
			//打印出OSR集合
			Node[] out_notes=partitionInfo.offlineReplicas();
			System.out.println(new Gson().toJson(out_notes));
			//打印出AR
			Node[] all_notes=partitionInfo.replicas();
			System.out.println(new Gson().toJson(all_notes));
		}
		return result;
	}
}
