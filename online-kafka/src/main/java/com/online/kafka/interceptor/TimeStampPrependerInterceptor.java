package com.online.kafka.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/***
 * Interceptor 使得用户在消息被发送前以及producer回调逻辑前有机会对消息进一步做定制化要求，比如修改消息等
 * 允许定义多个Interceptor形成一个拦截链
 */
public class TimeStampPrependerInterceptor implements ProducerInterceptor<String,String> {

    private int successCount=0;

    private int failCount=0;
    /***
     * 该方法被封装进 producer.send方法中
     * 该方法确保休息在被系列化1以计算分区前调用该你方法
     * 运行在producer的主线程中
     * @param producerRecord
     * @return
     */
    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord <String, String> producerRecord) {
        return new ProducerRecord(producerRecord.topic(),producerRecord.partition(),
                producerRecord.timestamp(),producerRecord.key(),System.currentTimeMillis()+","+producerRecord.value().toString());
    }

    /***
     * 该方法确保消息在被应答之前，或者消息发送失败时被调用，并且通常在producer回调函数触发之前
     * 运行在producer的I/O线程中
     * @param recordMetadata
     * @param e
     */
    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
        if(e==null){
            successCount++;
        }
        if(recordMetadata==null){
            failCount++;
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
