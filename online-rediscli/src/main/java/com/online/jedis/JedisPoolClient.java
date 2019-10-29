package com.online.jedis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

/***
 * jedis连接池客户端
 */
public class JedisPoolClient {

    private static final Logger logger = LoggerFactory.getLogger(JedisPoolClient.class);
    public static JedisPool jedisPool = null;
    private static final int maxSize = 20;
    private static final int maxIdle = 5;
    //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；单位毫秒
    //小于零:阻塞不确定的时间,  默认-1
    private static final long maxWaitMillis = 1000*100;

    private static final int connection_timeout = 5000;

    private static int port = 6379;

    // private static String host="116.62.172.92";
    private static String host="localhost";

    private static int database = 0;

    static{
        initJedisPool();
    }

    /***
     * 获取jedis连接
     * @return jedis对象
     */
    public static Jedis getJedis(){
        if(jedisPool == null){
            initJedisPool();
        }
        if(jedisPool != null) {
            return jedisPool.getResource();
        }
        return null;
    }

    /***
     * 释放jedis
     * @param jedis
     */
    public static void returnJedis(Jedis jedis) {
        if(jedis != null) {
            jedis.close();
        }
    }

    /***
     * 初始化jedis
     */
    private static void initJedisPool(){
        try{
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(maxSize);
            config.setMaxIdle(maxIdle);
            config.setMaxWaitMillis(maxWaitMillis);
            //在borrow(引入)一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
            config.setTestOnBorrow(true);
            //return 一个jedis实例给pool时，是否检查连接可用性（ping()）
            config.setTestOnReturn(true);
            jedisPool = new JedisPool(config, JedisPoolClient.host, JedisPoolClient.port, JedisPoolClient.connection_timeout,null,database);
        }catch (Exception e){
            logger.error("init redis error,e={}",e);
        }
    }

    public static JedisCluster getJedisCluster(){
        JedisCluster jedisCluster = new JedisCluster(new HostAndPort("localhost",6379),5000);
        return jedisCluster;
    }
}
