package com.online.kafka.serialize;


import com.online.kafka.message.bean.User;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class UserSerializerWithProtostuff implements Serializer<User> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, User user) {
        if(user==null) return null;
        Schema schema = RuntimeSchema.getSchema(user.getClass());
        return new byte[0];
    }

    @Override
    public void close() {

    }
}
