package com.online.kafka.consumer;

import avro.shaded.com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ApiTest {

    private static String topicName ="testTopic";
    private static String groupId = "test_group";

    private static volatile  boolean flag = true;
    public static Map<Integer,Long> lastConsumed = Maps.newConcurrentMap();
    //用来维护每个分区最新提交的位置
    public static  Map<Integer,OffsetClass> asynCmmited = Maps.newConcurrentMap();
    public static KafkaConsumer createConsumer(){
        KafkaConsumer<String, String>  consumer=null;
        Properties props = new Properties();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9000");//必须指定,可以用逗号分隔指定多台，指定多台只是为了常规的failover
        //	props.put("key.deserializer", "org.apahe.kafka.common.serilization.StringDeserializer");//必须指定
        //	props.put("value.deserializer", "org.apahe.kafka.common.serilization.StringDeserializer");//必须指定
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);//必须指定
//        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"true");//是否自动提交位移
//        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"1000");
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");//指定了无位移消息或位移越界的时候,从最早的消息开始读取
//        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,"18000");//用来检查consumer group中成员崩溃需要的时间
//        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,"18000");//consumer处理逻辑的最大时间
//        props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG,"3554432");//指定单次获取的最大字节数
//        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,"1000");//单次poll返回的最大的消息数，
//        props.put(ConsumerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG,"-1");//kafka会定期的关闭空闲socket连接，这样下次consumer处理请求时需要重新创建连接向broker的socket连接，当前默认值是9分钟，如果用户实际环境中不在乎这些socket资源的消耗，可以设置为-1，即不关闭

        //consumer  =  new KafkaConsumer<String, String>(props);
        consumer = new KafkaConsumer <String, String>(props,new StringDeserializer(),new StringDeserializer());
        List <String>topics = new ArrayList<String>();
        topics.add(topicName);
        //可订阅多个主题
        consumer.subscribe(topics);

        ExecutorService executorService =Executors.newCachedThreadPool();
        return consumer;
    }

    public static void consumeMsg(KafkaConsumer consumer){
        try{
            while(flag) {
                //消息被消费后，是否需要确认？对于生产者消费的偏移量是什么更新？
                //最简单的提交偏移量的方式是自动提交偏移量，如果enable.auto.commit被设为true，那么默认是每过5s，消费者会自动从poll方法接收到的最大的偏移量提交上去。
                //提交时间间隔auto.commit.interval.ms控制。自动提交也是在轮询里进行的，消费者每次在进行轮询时会检查是否该提交偏移量
                //使用自动提交缺陷有两点：
                //	1、时间间隔太长，可能导致偏移量没有及时提交上去，消费者就挂掉了，这个时候会出现重复消费
                //	2、因为轮询提交的时候，是提交最新poll的最大的偏移量，但是不能保证这些消息都被消费完，因为它本身不知道是否业务逻消费成功，所以可能造成记录每被消费。
                ConsumerRecords<String, String> records=  consumer.poll(Duration.ofMillis(1000));//这个poll操作类似于linux的select I/O机制,可同时跟多个broker的通信实现消息的并行拉取

                //按分区顺序遍历
                for(TopicPartition partition:records.partitions()) {

                    //遍历每个分区的记录
                    List<ConsumerRecord<String , String>> partitionRecords = records.records(partition);
                    for(ConsumerRecord<String , String> record:partitionRecords){
                        System.out.println(record.partition());

                    }
                    //获得每个分区中最后一条消息的offset
                    long lastConsumeOffset =partitionRecords.get(partitionRecords.size()-1).offset();
                    lastConsumed.put(partition.partition(),lastConsumeOffset);
                    //提交确认消费位置
                    submit(consumer,false,partition,lastConsumeOffset,null);
                }
                consumer.commitSync();
            }
        }finally {
            consumer.close();
        }

    }


    public static void asynConsumeMsg(KafkaConsumer consumer){
        try{
            while(flag) {
                //消息被消费后，是否需要确认？对于生产者消费的偏移量是什么更新？
                //最简单的提交偏移量的方式是自动提交偏移量，如果enable.auto.commit被设为true，那么默认是每过5s，消费者会自动从poll方法接收到的最大的偏移量提交上去。
                //提交时间间隔auto.commit.interval.ms控制。自动提交也是在轮询里进行的，消费者每次在进行轮询时会检查是否该提交偏移量
                //使用自动提交缺陷有两点：
                //	1、时间间隔太长，可能导致偏移量没有及时提交上去，消费者就挂掉了，这个时候会出现重复消费
                //	2、因为轮询提交的时候，是提交最新poll的最大的偏移量，但是不能保证这些消息都被消费完，因为它本身不知道是否业务逻消费成功，所以可能造成记录每被消费。
                ConsumerRecords<String, String> records=  consumer.poll(Duration.ofMillis(1000));//这个poll操作类似于linux的select I/O机制,可同时跟多个broker的通信实现消息的并行拉取
                //按分区顺序遍历
                for(TopicPartition partition:records.partitions()) {
                    int count = 0;
                    //遍历每个分区的记录
                    List<ConsumerRecord<String , String>> partitionRecords = records.records(partition);
                    for(ConsumerRecord<String , String> record:partitionRecords){
                        System.out.println(record.partition());
                        if(count%100==0){
                            submit(consumer, false, partition, record.offset(), new OffsetCommitCallback() {
                                @Override
                                public void onComplete(Map <TopicPartition, OffsetAndMetadata> map, Exception e) {
                                    if(e!=null){
                                        for(TopicPartition p:map.keySet()){
                                            int pnum = p.partition();
                                            long ofset = map.get(p).offset();
                                            OffsetClass o = asynCmmited.get(pnum);
                                            if(o!=null){
                                                if(o.getOffset()<=ofset){
                                                    o.setOffset(ofset);
                                                    asynCmmited.put(pnum,o);
                                                }
                                            }else{
                                                o = new OffsetClass();
                                                o.setOffset(ofset);
                                                asynCmmited.put(pnum,o);
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }

                }
                consumer.commitSync();
            }
        }finally {
            consumer.close();
        }

    }

    /***
     * 获取某个消费者对于某个分区next消费的位置
     * @param consumer
     * @param tp
     * @return
     */
    public static long nextConsume(KafkaConsumer consumer,TopicPartition tp){
        return consumer.position(tp);
    }

    /***
     * 获取某个消费者对于某个分区当前已提交的偏移量=current消费的位置+1
     * @param consumer 消费者
     * @param tp 分区
     * @return
     */
    public static OffsetAndMetadata currentCommited(KafkaConsumer consumer,TopicPartition tp){
        return consumer.committed(tp);
    }
    public static void main(String[] args) {
        KafkaConsumer<String,String> consumer = createConsumer();
        consumeMsg(consumer);
        //获得某个消费者负责主题的分区列表
        List<PartitionInfo> partitionInfos = consumer.partitionsFor(topicName);
        if(!partitionInfos.isEmpty()){
            //遍历消费者负责的分区
            for(PartitionInfo partitionInfo:partitionInfos){
                System.out.println("topic: "+partitionInfo.topic()+";  partion: "+partitionInfo.partition());
                //获得某个分区的消费位置
                TopicPartition tp = new TopicPartition(partitionInfo.topic(),partitionInfo.partition());
                //获得分区下一次拉取的消息的位置
                long nextOffset = nextConsume(consumer,tp);
                //获得分区当前已提交的偏移量
                OffsetAndMetadata metadata =currentCommited(consumer,tp);
                long currentOffset = metadata.offset();
                String metadat = metadata.metadata();

                long lastConsumeOffset = lastConsumed.get(partitionInfo.partition());
            }

        }
    }

    /***
     * 消费者提交的模式
     * @param consumer 消费者
     * @param asynFlag true:异步提交 false:同步提交
     */
    public static void submit(KafkaConsumer consumer,boolean asynFlag){
        if(asynFlag){
            consumer.commitAsync();
        }else{
            consumer.commitSync();
        }
    }

    /***
     * seek只能重置消费者分配到的分区位置，而分区的分配是在poll方法的调用过程中实现的，
     * 也就是说调用seek之前会先调用一次poll方法，等待分配到分区后，才可以重置位置
     * @param consumer
     * @param topicPartition
     * @param offset
     */
    public static void seek(KafkaConsumer consumer,TopicPartition topicPartition,long offset){
        consumer.seek(topicPartition,offset);
    }

    /***
     * 从分区的开头开始消费
     * @param consumer
     * @param topicPartition
     */
    public static void seekToBeginning(KafkaConsumer consumer,List<TopicPartition> topicPartition){
        consumer.seekToBeginning(topicPartition);
    }
    /***
     * 从分区的结尾 开始消费
     * @param consumer
     * @param topicPartition
     */
    public static void offsetsForTimes(KafkaConsumer consumer,List<TopicPartition> topicPartition){
        consumer.seekToEnd(topicPartition);
    }
    /***
     * 从分区的jieiwei 开始消费
     * @param consumer
     * @param topicPartition
     */
    public static void seekToEnd(KafkaConsumer consumer,List<TopicPartition> topicPartition){
        consumer.seekToEnd(topicPartition);
    }

    /***
     * 获得消费者所分配的分区的情况
     * @param consumer
     * @return
     */
    public static Set<TopicPartition> assignment(KafkaConsumer consumer){

        while(CollectionUtils.isEmpty(consumer.assignment())){
            consumer.poll(Duration.ofMillis(1000));
        };
        return consumer.assignment();
    }
    public static Map<TopicPartition,Long> ednoffsets(KafkaConsumer consumer){

        while(CollectionUtils.isEmpty(consumer.assignment())){
            consumer.poll(Duration.ofMillis(1000));
        };
        Set<TopicPartition> assignment= consumer.assignment();
        //获取分区的末尾消息的位置
        Map<TopicPartition,Long> offsets = consumer.endOffsets(assignment);

        return offsets;
    }

    /***
     * 提交批次指定对应的position
     * @param consumer 消费者
     * @param partition 分区
     * @param offset 分区指定的position
     */
    public static void submit(KafkaConsumer consumer,boolean asynFlag,
                              TopicPartition partition,
                              long offset, OffsetCommitCallback commitCallback){
        if(asynFlag){
            //这边第二个参数一个回调函数，如果成功了话，会回调这个函数
            consumer.commitAsync(Collections.singletonMap(partition, new OffsetAndMetadata(offset + 1)), new OffsetCommitCallback() {
                @Override
                public void onComplete(Map <TopicPartition, OffsetAndMetadata> map, Exception e) {
                    if(e!=null){
                        System.out.println(" commit fail,e="+e);
                    }else{
                        System.out.println(" commit success,map="+new Gson().toJson(map));
                    }

                }
            });
            return ;
        }
        consumer.commitSync(Collections.singletonMap(partition,new OffsetAndMetadata(offset+1)));
    }

    static class OffsetClass{
        private volatile long offset;

        public long getOffset() {
            return offset;
        }

        public void setOffset(long offset) {
            this.offset = offset;
        }
    }



}
