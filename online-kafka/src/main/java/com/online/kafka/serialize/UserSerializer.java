package com.online.kafka.serialize;

import java.nio.ByteBuffer;
import java.util.Map;

import com.online.kafka.message.bean.User;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
/***
 * 自定义序列化器
 * 自定义的系列化器要实现org.apache.kafka.common.serialization.Serializer
 * @author xuzf
 *
 */
public class UserSerializer implements Serializer<User>{

	public void configure(Map<String, ?> configs, boolean isKey) {
		// TODO 这里可以做额外的配置
		
	}
	
	public byte[] serialize(String topic, User user) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		if(user == null) {
			return null;
		}
		try {//通过下面的写法，我们可以看到，这种方式，灵活性低，只要生产者字段类型发生变化，就会出现版本兼容的问题
			buffer.putInt(user.getId());//第一个字段是
			if(user.getName()!=null) {
				buffer.putInt(user.getName().getBytes("UTF-8").length);
				buffer.put(user.getName().getBytes("UTF-8"));
			}else {//注意没有也要补上，这样消费端才会根据顺序读取
				buffer.putInt(0);
				buffer.put(new byte[0]);
			}
			buffer.putInt(user.getAge());//int类型的长度是固定的，所以不要传长度
			
			if(user.getSex()!=null) {
				buffer.putInt(user.getSex().getBytes("UTF-8").length);
				buffer.put(user.getSex().getBytes("UTF-8"));
			}else {//注意没有也要补上，这样消费端才会根据顺序读取
				buffer.putInt(0);
				buffer.put(new byte[0]);
			}
			return buffer.array();
		}catch(Exception e) {
			throw new SerializationException("error when Serialize user to byte[]"+e);
		}
	}

	public void close() {
		// 可以关闭任何东西
		
	}
	
}