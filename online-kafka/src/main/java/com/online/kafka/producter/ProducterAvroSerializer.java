package com.online.kafka.producter;

import org.apache.avro.Schema;  
import org.apache.avro.generic.GenericData;  
import org.apache.avro.generic.GenericRecord;  
import org.apache.avro.io.*;  
import org.apache.avro.specific.SpecificDatumReader;  
import org.apache.avro.specific.SpecificDatumWriter;  
import org.apache.commons.codec.DecoderException;  
import org.apache.commons.codec.binary.Hex;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.ByteArrayOutputStream;  
import java.io.File;  
import java.io.IOException;  
import java.nio.charset.Charset;  
import java.util.Properties;  
  
public class ProducterAvroSerializer {  
	private static final String broker_address="localhost:9092";
	private static final int broker_port = 2281;
	private static final String topic = "xuzf";
	 private static Properties kafkaProps = new Properties();
	 public static KafkaProducer<String, GenericRecord> producer = null;
	 static{
	        //在定义一个生产者的时候，有几个参数是必须配置指定的，
	        //分别是bootstrap.severs、key.serializer和value.serializer
         kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,broker_address);
         kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                 StringSerializer.class.getName());
         kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                 StringSerializer.class.getName());
	    }
    void producer(Schema schema) throws IOException {  
  
        Properties props = new Properties();  
        props.put("metadata.broker.list", "127.0.0.1:9092");
        props.put("serializer.class", "kafka.serializer.DefaultEncoder");  
        props.put("request.required.acks", "1");  
        KafkaProducer<String, GenericRecord> producer = new KafkaProducer<String, GenericRecord>(kafkaProps);  
        GenericRecord user = new GenericData.Record(schema);  
        user.put("id", "'999'");  
        user.put("name", "xuzf");  
        user.put("age", 28);  
        user.put("sex","1");  
//        System.out.println("Original Message : "+ user);  
//        //Step3 : Serialize the object to a bytearray  
      /*  DatumWriter<GenericRecord>writer = new SpecificDatumWriter<GenericRecord>(schema);  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);  
        writer.write(user, encoder);  
        encoder.flush();  
        out.close();  
  
        byte[] serializedBytes = out.toByteArray();  
        System.out.println("Sending message in bytes : " + serializedBytes);  */
        ProducerRecord<String,GenericRecord> record = new ProducerRecord <String, GenericRecord>(topic,"avro_key",user);
        producer.send(record) ;
        producer.close();  
  
    }  
  
  
    public static void main(String[] args) throws IOException, DecoderException {  
    	ProducterAvroSerializer test = new ProducterAvroSerializer();  
        Schema schema = new Schema.Parser().parse(new File("src/user.avsc"));  
        test.producer(schema);  
    }  
}  