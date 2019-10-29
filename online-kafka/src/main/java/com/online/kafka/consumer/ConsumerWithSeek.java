package com.online.kafka.consumer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import com.online.kafka.listener.ConsumerSaveOffsetListener;
import com.online.kafka.message.bean.User;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;


public class ConsumerWithSeek {
	public static KafkaConsumer< String, User> consumer =null;
	public static ConsumerSaveOffsetListener listener = null;
	static {
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9000");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "test_group");
		consumer = new KafkaConsumer<String, User>(props);
		ArrayList<String>  topicList = new ArrayList<String>();
		topicList.add("topi1");
		//给消费者绑定再均衡监听器
		listener = new ConsumerSaveOffsetListener(consumer);
		consumer.subscribe(topicList,new ConsumerSaveOffsetListener(consumer));
		
	}
	public static void main(String[] args) {
		//设置优雅退出consumer的线程
		final Thread mainThread = Thread.currentThread();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				/***
				 * 当consumer在无限循环的时候，如果要优雅的退出循环，需要另一个线程调用consumer.wakeup方法。
				 * 如果循环运行在主线程里(下面的例子就是)，可以在ShutdownHook里调用consumer.wakeup方法。
				 * 调用consumer.wakeup方法可以退出poll()，并抛出wakeupException异常，或者如果调用consumer.wakeup时线程没有等待轮询，那么异常将在下一轮调用poll方法时抛出。
				 * 对于wakeupException我们不需要处理，因为它只是用于跳出循环的一种方式。
				 * 要记得调用consumer.close，它会提交任何还没有提交的东西，并向群组协调器发送消息，告诉自己要离开群组，接下来就会触发再均衡，而不需要等待会话超时
				 */
				consumer.wakeup();
				try {
					mainThread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		//该操作拉去零条消息，它的主要作用是让消费者加入消费者群组里，从而获取到它负责的分区
		consumer.poll(0);
		//循环遍历该消费者负责的分区，并seek到最新位置上
		 Set<TopicPartition> partitions =new HashSet<TopicPartition>();
		for(TopicPartition partition:consumer.assignment()) {
			//定位到该分区最新消费位置(它是保存到数据库里)
			consumer.seek(partition, listener.getOffsetFromDb(partition));
		}
			try {
				while(true) {
					ConsumerRecords<String, User> records = consumer.poll(Duration.ofMillis(1000));
					partitions.clear();
					for (ConsumerRecord<String, User> record:records) {
					
						//开始消费
						System.out.println("topic ="+record.topic()+", partition="+record.partition()+", offset="+record.offset()+" customer="+record.key()+", country="+record.value()
						+"\n");
						//将消息存到数据库
						storeRecordInDB(record);
						//将消费位置存到数据库
						storeOffsetInDB(record.topic(), record.partition(), record.offset());
					}
					partitions = consumer.assignment();
				
					for(TopicPartition partition : partitions) {
						listener.commitDBTransaction(partition);
					}		
				}
			
			} catch (WakeupException e) {
				// TODO: 忽略异常关闭
			} finally {
				//要记得调用consumer.close，它会提交任何还没有提交的东西，并向群组协调器发送消息，告诉自己要离开群组，接下来就会触发再均衡，而不需要等待会话超时
				consumer.close();
			}
		
	}
	 public static void storeRecordInDB(ConsumerRecord record) {
		 
	 }
	 public static void storeOffsetInDB(String topic,int partition, long offset) {
		 
	 }
}
