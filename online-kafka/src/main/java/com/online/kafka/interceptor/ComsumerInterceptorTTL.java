package com.online.kafka.interceptor;

import avro.shaded.com.google.common.collect.Lists;
import avro.shaded.com.google.common.collect.Maps;
import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.List;
import java.util.Map;

/***
 * 自定义消费者拦截器
 * @author zhifang.xu
 */
public class ComsumerInterceptorTTL implements ConsumerInterceptor<String,String> {
    /***
     * kafka在poll之前来调用该方法，可以对消息进行相应的定制化操作
     * @param consumerRecords
     */
    @Override
    public ConsumerRecords<String,String> onConsume(ConsumerRecords <String,String>consumerRecords) {
        long now =System.currentTimeMillis();

        Map<TopicPartition,List<ConsumerRecord<String,String>>> newRecords = Maps.newHashMap();

        for(TopicPartition tp : consumerRecords.partitions()){
            List<ConsumerRecord<String,String>> tprecords =consumerRecords.records(tp);
            List<ConsumerRecord<String,String>> newrecords = Lists.newArrayList();

            for(ConsumerRecord<String,String> record:tprecords){
                if(now - record.timestamp()<10*1000){
                    newrecords.add(record);
                }
            }
            if(!newrecords.isEmpty()){
                newRecords.put(tp,newrecords);
            }


        }
        return new ConsumerRecords <>(newRecords);
    }

    @Override
    public void onCommit(Map <TopicPartition, OffsetAndMetadata> map) {
        map.forEach((tp,offset)-> System.out.println(tp+":"+offset.offset()));
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map <String, ?> map) {

    }
}
