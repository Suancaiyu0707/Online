package com.online.lock;

import com.online.redisson.*;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/***
 * 用redisson实现分布式锁
 * @author zhifang.xu
 */
public class RedissionLock {

    private static final String RAtomicName = "genId_";
    private static final String LOCK_NAME = "redisLock_";
    /***
     * 单机模式模式
     * 使用redisson设置过期时间
     * @param value 键名
     * @param value 键值
     * @param time 有效期时间
     * @param timeUnit 时间单位
     * @return
     */
    public static boolean standloneSetKV(String key ,String value, long time, TimeUnit timeUnit) {
        RedissonClient redisson = StandloneConfig.createRedisson();
        //首先获取redis中的key-value对象，key不存在没关系
        RBucket<String> keyObject = redisson.getBucket(key);
        //设置key的value值，并指定过期时间，如果key存在的话返回false
        Boolean result =  keyObject.trySet(value,time,timeUnit);
        //用完记得归还
        RedissonUtil.close(redisson);
        return result;


    }


    /***
     * 哨兵模式
     * 使用redisson设置过期时间
     * @param value 键名
     * @param value 键值
     * @param time 有效期时间
     * @param timeUnit 时间单位
     * @return
     */
    public static boolean sentrySetKV(String key ,String value, long time, TimeUnit timeUnit) {
        RedissonClient redisson = SentryConfig.createRedisson();
        //首先获取redis中的key-value对象，key不存在没关系
        RBucket<String> keyObject = redisson.getBucket(key);
        //设置key的value值，并指定过期时间，如果key存在的话返回false
        Boolean result =  keyObject.trySet(value,time,timeUnit);
        //用完记得归还
        RedissonUtil.close(redisson);
        return result;

    }

    /***
     * 哨兵模式
     * 使用redisson设置过期时间
     * @param value 键名
     * @param value 键值
     * @param time 有效期时间
     * @param timeUnit 时间单位
     * @return
     */
    public static boolean clusterSetKV(String key ,String value, long time, TimeUnit timeUnit) {
        RedissonClient redisson = ClusterConfig.createRedisson();
        //首先获取redis中的key-value对象，key不存在没关系
        RBucket<String> keyObject = redisson.getBucket(key);
        //设置key的value值，并指定过期时间，如果key存在的话返回false
        Boolean result =  keyObject.trySet(value,time,timeUnit);
        //用完记得归还
        RedissonUtil.close(redisson);
        return result;

    }


    /***
     * 哨兵模式
     * 使用redisson设置过期时间
     * @param value 键名
     * @param value 键值
     * @param time 有效期时间
     * @param timeUnit 时间单位
     * @return
     */
    public static boolean masterSlaveSetKV(String key ,String value, long time, TimeUnit timeUnit) {
        RedissonClient redisson = MasterSlaveConfig.createRedisson();
        //首先获取redis中的key-value对象，key不存在没关系
        RBucket<String> keyObject = redisson.getBucket(key);

        //设置key的value值，并指定过期时间，如果key存在的话返回false
        Boolean result =  keyObject.trySet(value,time,timeUnit);
        //用完记得归还
        RedissonUtil.close(redisson);
        return result;

    }

    /***
     * 尝试获得分布式锁
     * @param lockName 锁名称
     * @param timeout 获得锁等待超时时间
     * @param expireTime 锁失效时间
     * @param timeUnit 时间单位
     * @return
     */
    public static boolean acquireBySentry(String lockName,int timeout,int expireTime,TimeUnit timeUnit){
        String key = LOCK_NAME + lockName;
        boolean islock = false;
        RLock lock = SentryConfig.createRedisson().getLock(key);
        try {
            //lock提供带expireTime参数，timeout结束强制解锁，防止死锁.timeout表示获得锁时等待的超时时间
            islock = lock.tryLock(timeout,expireTime, timeUnit);
            if (islock) {
                System.out.println("线程["+Thread.currentThread()+"]成功获得锁");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            // 无论如何, 最后都要解锁
            lock.unlock();
        }
        return islock;
    }

    /***
     * 尝试获得分布式锁
     * @param lockName 锁名称
     * @param timeout 获得锁等待超时时间
     * @param expireTime 锁失效时间
     * @param timeUnit 时间单位
     * @return
     */
    public static boolean acquireByCluster(String lockName,int timeout,int expireTime,TimeUnit timeUnit){
        String key = LOCK_NAME + lockName;
        boolean islock = false;
        RLock lock = ClusterConfig.createRedisson().getLock(key);
        try {
            //lock提供带expireTime参数，timeout结束强制解锁，防止死锁.timeout表示获得锁时等待的超时时间
            islock = lock.tryLock(timeout,expireTime, timeUnit);
            if (islock) {
                System.out.println("线程["+Thread.currentThread()+"]成功获得锁");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            // 无论如何, 最后都要解锁
            lock.unlock();
        }
        System.err.println("======lock======"+Thread.currentThread().getName());
        return  true;
    }
    /***
     * 尝试释放分布式锁
     * @param lockName 锁名称
     * @return
     */
    public static void release(String lockName){
        String key = LOCK_NAME + lockName;
        RLock mylock = MasterSlaveConfig.createRedisson().getLock(key);
        mylock.unlock();
        System.err.println("======unlock======"+Thread.currentThread().getName());
    }


    /** 获取redis中的原子ID */
    public static Long nextID(){
        RAtomicLong atomicLong = StandloneConfig.createRedisson().getAtomicLong(RAtomicName);
        //原子性的获取下一个ID，递增1
        atomicLong.incrementAndGet();
        return atomicLong.get();
    }

}
