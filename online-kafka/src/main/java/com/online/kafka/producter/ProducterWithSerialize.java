package com.online.kafka.producter;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.online.kafka.message.bean.User;
import com.online.kafka.serialize.UserSerializer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

public class ProducterWithSerialize {
	private static final String broker_address="localhost:9092";
	private static final int broker_port = 2281;
	private static final String topic = "xuzf";
	 private static Properties kafkaProps = new Properties();
	 
	 private static final UserSerializer serializer = new UserSerializer();
	 public static KafkaProducer<String,User> producer = null;//指定写消息的类型是user
	 static{
	        //在定义一个生产者的时候，有几个参数是必须配置指定的，
	        //分别是bootstrap.severs、key.serializer和value.serializer
		 kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,broker_address);
		 kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				 StringSerializer.class.getName());
		 kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				 UserSerializer.class.getName());
	    }
	public static void main(String[] args) {
		producer = new KafkaProducer<String, User>(kafkaProps);
		List<ProducerRecord<String,User>> records = getProducerRecords();
		
		for(ProducerRecord<String,User> record:records) {
			producer.send(record, new MyCallBack());
		}
		//producer.
		
	}
	   public static List<ProducerRecord<String,User>> getProducerRecords(){
	        List<ProducerRecord<String,User>> records = new ArrayList <ProducerRecord<String,User>>();
	        
	        for(int i = 0;i<10000;i++){
	        	User user = new User();
	        	user.setId(i+1);
	        	user.setAge(28);
	        	user.setSex("1");
	        	user.setName("xuzf_"+i);
	        	//byte b[] =serializer.serialize(topic, user);
	        	records.add(new ProducerRecord <String, User>(topic,"user_key_"+i,user));
	        }
	        return records;
	    }
	   
	   static class MyCallBack implements Callback {
	        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
	            System.out.println("topic="+recordMetadata.topic()+",partition="+recordMetadata.partition()
	                    +",offset="+recordMetadata.offset() );
	        }
	    }

}
