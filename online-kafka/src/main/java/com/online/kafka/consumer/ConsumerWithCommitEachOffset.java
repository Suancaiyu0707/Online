package com.online.kafka.consumer;

import java.io.InterruptedIOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.online.kafka.message.bean.User;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * 对于commitSync()和commitAsync()只会提交最后一个偏移量
 * 
 * 当api里传一个主题的分区和偏移量的map，就可以处理部分批次
 * @author xuzhifang
 *
 */
public class ConsumerWithCommitEachOffset {
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
		ArrayList<String>  topicList = new ArrayList<String>();
		topicList.add("test_topic");
		//通过subscribe订阅的消费有自动再均衡的功能，而通过assign没有
		consumer.subscribe(topicList);
	}
	public static void main(String[] args) {
		//获得主线程
		final Thread mainThread = Thread.currentThread();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("begin exit...");
				consumer.wakeup();
					try {
						mainThread.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
			}
		});
		while(flag) {
			ConsumerRecords<String, User> records = consumer.poll(Duration.ofMillis(1000));
			List<TopicPartition> pausePartition = new ArrayList<TopicPartition>();
			for(ConsumerRecord<String, User> record : records) {
				
				try {
					System.out.println("topic ="+record.topic()+", partition="+record.partition()+", offset="+record.offset()+" customer="+record.key()+", country="+record.value()
					+"\n");
					TopicPartition partition = new TopicPartition(record.topic(), record.partition());
					//使用期望处理的下一个消息的偏移量更新map里的偏移量，下一次就从这里开始读取消息
					OffsetAndMetadata metadata = new OffsetAndMetadata(record.offset()+1, "no metadata");
					currentOffsets.put(partition, metadata);
					if(count % 1000 ==0) {//每1000次提交一次偏移量
						consumer.commitAsync(currentOffsets,null);
					}
				} catch (Exception e) {
					System.out.println("consumer topicPartition error");
					pausePartition.add(new TopicPartition(record.topic(), record.partition()));
					//consumer.pause(record.partition());
				}
				
				
				
				count++;
			}
		}
	}

}
