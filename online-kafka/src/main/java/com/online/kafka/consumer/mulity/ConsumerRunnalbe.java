package com.online.kafka.consumer.mulity;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

public class ConsumerRunnalbe implements Runnable{

    private final KafkaConsumer<String,String> consumer;

    public ConsumerRunnalbe(String brokerList, String groupId, String topic){
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);//必须指定,可以用逗号分隔指定多台，指定多台只是为了常规的failover
        //	props.put("key.deserializer", "org.apahe.kafka.common.serilization.StringDeserializer");//必须指定
        //	props.put("value.deserializer", "org.apahe.kafka.common.serilization.StringDeserializer");//必须指定
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);//必须指定
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,false);//是否自动提交位移
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"5000");//设置自动提交位移的时间，默认是5秒
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");//指定了无位移消息或位移越界的时候,从最早的消息开始读取
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,"18000");//用来检查consumer group中成员崩溃需要的时间
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,"18000");//consumer处理逻辑的最大时间
        props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG,"3554432");//指定单次获取的最大字节数
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,"1000");//单次poll返回的最大的消息数，
        props.put(ConsumerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG,"-1");//kafka会定期的关闭空闲socket连接，这样下次consumer处理请求时需要重新创建连接向broker的socket连接，当前默认值是9分钟，如果用户实际环境中不在乎这些socket资源的消耗，可以设置为-1，即不关闭

        consumer= new KafkaConsumer <String, String>(props,new StringDeserializer(),new StringDeserializer());
        consumer.subscribe(Arrays.asList(topic));
    }

    @Override
    public void run() {
        while(true){
            ConsumerRecords <String, String> records = consumer.poll(Duration.ofMillis(200));
            for(ConsumerRecord<String,String> record : records){
                System.out.println(Thread.currentThread().getName()+ " consumed "+record.partition()+" the message with offset:"+record.offset());
            }

        }

    }
}
