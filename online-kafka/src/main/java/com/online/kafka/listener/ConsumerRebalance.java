package com.online.kafka.listener;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
/***
 * 自定义再均衡时监听器
 * @author hb
 *
 */
public class ConsumerRebalance implements ConsumerRebalanceListener {
	private Map<TopicPartition, OffsetAndMetadata>  currentOffsets = new HashMap<TopicPartition, OffsetAndMetadata>();
	private KafkaConsumer consumer = null;
	public ConsumerRebalance(  KafkaConsumer consumer ,Map<TopicPartition, OffsetAndMetadata>  currentOffsets ) {
		this.consumer=consumer;
		this.currentOffsets = currentOffsets;
	}
	/***
	 * 方法会在再均衡之前和消费者停止读取消息之后被调用
	 * 如果发生再均衡，我们要在即将失去分区所有权时提交偏移量
	 * 注意：这里提交的是最近处理过的偏移量，而不是批次中还在处理的最后一个偏移量。
	 * 		我们要提交所有分区的偏移量，而不只是那些即将丢失所有权的分区的偏移量，因为提交的是一句处理过的，所以不回有什么问题
	 */
	public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
		System.out.println("Lost partitions  in rebalance,committing current offsets:"+currentOffsets);
		consumer.commitSync(currentOffsets);
	}
	/***
	 * 在再均衡之后和消费者开始读取消息之前被调用
	 */
	public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
		
	}

}
