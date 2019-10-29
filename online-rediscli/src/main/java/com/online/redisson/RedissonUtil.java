package com.online.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * redisson工具类
 * @author zhifang.xu
 */
public class RedissonUtil {
    //关闭redisson，会返回给连接池
    public static void close(RedissonClient redisson){
        redisson.shutdown();

    }
}
