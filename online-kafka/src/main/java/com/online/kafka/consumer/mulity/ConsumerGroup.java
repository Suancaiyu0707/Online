package com.online.kafka.consumer.mulity;


import java.util.ArrayList;
import java.util.List;

public class ConsumerGroup {

    private List<ConsumerRunnalbe> consumers;

    public ConsumerGroup(int consumerNum,String groupId,String topic,String brokerList){
        consumers = new ArrayList<>(consumerNum);

        for(int i=0;i<consumerNum;i++){
            ConsumerRunnalbe runnalbe = new ConsumerRunnalbe(brokerList,groupId,topic);

            consumers.add(runnalbe);
        }
    }


    public void execute(){
        for(ConsumerRunnalbe runnalbe:consumers){
            new Thread(runnalbe).start();
        }
    }


}
