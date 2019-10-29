package com.online.kafka.listener;

import java.util.Collection;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

public class ConsumerSaveOffsetListener implements ConsumerRebalanceListener {
	

	private KafkaConsumer consumer = null;
	public ConsumerSaveOffsetListener ( KafkaConsumer consumer) {
		this.consumer = consumer;
	}
	/***
	 * 发生在再均衡之前，消费者停止消息消费之后
	 */
	public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
		//出现再均衡之前，赶紧把当前消费者负责的所有分区的消费情况都提交到数据库
		for(TopicPartition partition:partitions) {
			commitDBTransaction(partition);
		}
	
	}
	/***
	 * 发生在再均衡之后，消费者消费之前
	 */
	public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
		//循环消费者负责的所有的分区，从数据库中获取该分区被消费的最新位置，然后定位到该位置继续消费，避免重复消费
		//注意，在重分区的时候，对于分区信息本身是不会变的
		for(TopicPartition partition:partitions) {
			consumer.seek(partition, getOffsetFromDb(partition));
		}
	}
	/***
	 * 存储某个分区的最新消费位置
	 * @param partition
	 */
	public void commitDBTransaction(TopicPartition partition) {
		
	}
	/***
	 * 获取某个分区的最新消费位置
	 * @param partition
	 * @return
	 */
	public int getOffsetFromDb(TopicPartition partition) {
		return 100;
	}
}
