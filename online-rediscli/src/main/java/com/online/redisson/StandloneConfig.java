package com.online.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;

/***
 * 单例的config
 * @author zhifang.xu
 */
public class StandloneConfig {

    private static final String address = "redis://127.0.0.1:6379";

    private static final int poolSize = 500;

    private static final int idleConnTimeOut = 10000;
    private static final int connTimeOut = 30000;
    private static final int reconnTimeOut = 3000;
    private static Config config = null;
    static {
        //指定编码，默认编码为org.redisson.codec.JsonJacksonCodec
        config.setCodec(new StringCodec());
        //指定使用单节点部署方式,redis是协议
        config.useSingleServer().setAddress(address);
        //config.setPassword("password")//设置密码
        config.useSingleServer().setConnectionPoolSize(poolSize);//设置对于master节点的连接池中连接数最大为500
        config.useSingleServer().setIdleConnectionTimeout(idleConnTimeOut);//如果当前连接池里的连接数量超过了最小空闲连接数，而同时有连接空闲时间超过了该数值，那么这些连接将会自动被关闭，并从连接池里去掉。时间单位是毫秒。
        config.useSingleServer().setConnectTimeout(connTimeOut);//同任何节点建立连接时的等待超时。时间单位是毫秒。
        config.useSingleServer().setReconnectionTimeout(reconnTimeOut);//当与某个节点的连接断开时，等待与其重新建立连接的时间间隔。时间单位是毫秒。
    }

    public static RedissonClient createRedisson(){
        //创建客户端(发现创建RedissonClient非常耗时，基本在2秒-4秒左右)
        RedissonClient redisson = Redisson.create(config);
        return redisson;

    }
}
