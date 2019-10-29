package com.online.lock;

import org.redisson.Redisson;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/***
 * 采用redLock实现分布式锁
 */
public class RedLock {
    /***
     *
     * @param key key名称
     * @param waitTime 获得锁等待时间
     * @param expiretTime 锁失效时间
     * @param timeUnit 时间单位
     */
    public static void  distributedLock(String key,int waitTime,int expiretTime,TimeUnit timeUnit){
        Config config1 = new Config();
        config1.useSingleServer().setAddress("redis://127.0.0.1:6279")
                .setPassword("xuzf880707").setDatabase(0);
        RedissonClient redissonClient1 = Redisson.create(config1);

        Config config2 = new Config();
        config2.useSingleServer().setAddress("redis://127.0.0.1:6280")
                .setPassword("xuzf880707").setDatabase(0);
        RedissonClient redissonClient2 = Redisson.create(config2);

        Config config3 = new Config();
        config3.useSingleServer().setAddress("redis://127.0.0.1:6281")
                .setPassword("xuzf880707").setDatabase(0);
        RedissonClient redissonClient3 = Redisson.create(config3);

        String resourceName = key;
        RLock lock1 = redissonClient1.getLock(resourceName);
        RLock lock2 = redissonClient2.getLock(resourceName);
        RLock lock3 = redissonClient3.getLock(resourceName);
        // 同时加锁：lock1 lock2 lock3
        // 红锁在大部分节点上加锁成功就算成功。
        RedissonRedLock redLock = new RedissonRedLock(lock1, lock2, lock3);
        boolean isLock;
        try {
            isLock = redLock.tryLock(waitTime, expiretTime, TimeUnit.MILLISECONDS);
            System.out.println("isLock = "+isLock);
            if (isLock) {
                System.out.println("已成功获得锁");
                Thread.sleep(30000);
            }
        } catch (Exception e) {
        } finally {
            // 无论如何, 最后都要解锁
            System.out.println("");
            redLock.unlock();
        }

    }
}
