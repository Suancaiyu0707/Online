package com.online.kafka.consumer.mulity;

public class ConsumerMain {
    public static void main(String[] args) {
        String brokerList = "localhost:9092";

        String groupId ="test-group";
        String topic = "test-topic";

        int consumerNum = 3 ;
        ConsumerGroup group = new ConsumerGroup(consumerNum,groupId,topic,brokerList);
        group.execute();
    }
}
