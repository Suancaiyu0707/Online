package com.online.kafka.partioner;


import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;

import java.util.List;
import java.util.Map;

public class AuditPartitioner implements Partitioner {
    /***
     *
     * @param topic 主题
     * @param key 消息键
     * @param keyBytes 消息键字节
     * @param value 消息值
     * @param valueBytes 消息值字节数组
     * @param cluster 集群信息
     * @return
     */
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        //获得集群的该主题的所有分区列表
        List<PartitionInfo> partitions = cluster.availablePartitionsForTopic(topic);
        String msgKey = (String)key;
        int partionSize = partitions.size();
        if(!StringUtils.isEmpty(msgKey)&&msgKey.indexOf("audit")!=-1){
            return partionSize-1;
        }else{
            return Math.abs(Utils.murmur2(keyBytes)) % (partionSize-1);
        }
    }

    @Override
    public void close() {

    }

    /**
     * 该方法实现必要资源的初始化
     * @param map
     */
    @Override
    public void configure(Map<String, ?> map) {

    }
}
