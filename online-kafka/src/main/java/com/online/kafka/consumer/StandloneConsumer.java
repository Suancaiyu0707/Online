package com.online.kafka.consumer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;


public class StandloneConsumer {
	private static  KafkaConsumer<String, String>  consumer=null;
	
	private static String topicName ="test-topic";
	private static String groupId = "test_group";
	
	public static void main(String[] args) {
		Properties props = new Properties();
		
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");//必须指定,可以用逗号分隔指定多台，指定多台只是为了常规的failover
	//	props.put("key.deserializer", "org.apahe.kafka.common.serilization.StringDeserializer");//必须指定
	//	props.put("value.deserializer", "org.apahe.kafka.common.serilization.StringDeserializer");//必须指定
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);//必须指定
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"true");//是否自动提交位移
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"1000");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");//指定了无位移消息或位移越界的时候,从最早的消息开始读取
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,"18000");//用来检查consumer group中成员崩溃需要的时间
		props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,"18000");//consumer处理逻辑的最大时间
		props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG,"3554432");//指定单次获取的最大字节数
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,"1000");//单次poll返回的最大的消息数，
		props.put(ConsumerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG,"-1");//kafka会定期的关闭空闲socket连接，这样下次consumer处理请求时需要重新创建连接向broker的socket连接，当前默认值是9分钟，如果用户实际环境中不在乎这些socket资源的消耗，可以设置为-1，即不关闭
	  	consumer = new KafkaConsumer <String, String>(props,new StringDeserializer(),new StringDeserializer());
	  	List <String>topics = new ArrayList<String>();
	  	topics.add(topicName);

		  try {
	  		//可订阅多个主题
			  TopicPartition t1= new TopicPartition(topicName,0);
			  TopicPartition t2= new TopicPartition(topicName,1);
			  consumer.assign(Arrays.asList(t1,t2));
			 
			  while(true) {
				 ConsumerRecords<String, String> records= consumer.poll(Duration.ofMillis(1000));//这个poll操作类似于linux的select I/O机制
				  
				  int count =0 ;
				  for(ConsumerRecord<String, String> record:records) {
					  System.out.println(record.partition());

					  
				  }
				consumer.commitAsync();

				  
			  }
		  }catch(Exception e) {
			  
		  }finally {
			  //混合使用同步和异步提交偏移量
			  consumer.commitSync();
			  //显式关闭consumer，释放consumer占用的资源（包括线程资源、内存、socket连接）
			  consumer.close();;
		  }
	}
	
}
