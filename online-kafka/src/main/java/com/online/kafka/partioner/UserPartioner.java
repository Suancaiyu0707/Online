package com.online.kafka.partioner;

import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.record.InvalidRecordException;
import org.apache.kafka.common.utils.Utils;
/***
 * 自定义分区器
 * @author xuzf
 *
 */
public class UserPartioner implements Partitioner {
	public void configure(Map<String, ?> configs) {	
	}
	//返回分区编号
	public int partition(String topic, Object key, byte[] keyBytes,
			Object value, byte[] valueBytes, Cluster cluster) {
		//获取集群下某个主题的所有分区
		List<PartitionInfo> partitions =cluster.partitionsForTopic(topic);
		int partionSize = partitions.size();
		//如果key为空或者key不是String类型，则抛出异常
		if((keyBytes == null) ||(!(key instanceof String))) {
			throw new InvalidRecordException("we expect all messages to have user name as key");
		}
		if(((String)key).indexOf("xuzf")!=-1) {
			return partionSize;
		}
		return Math.abs(Utils.murmur2(keyBytes)) % (partionSize-1);

	}
	public void close() {
	}

}
