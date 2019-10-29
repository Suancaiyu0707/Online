package com.online.zk.util;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.Arrays;
import java.util.List;

/***
 * 复合锁
 */
public class CuratorDistributeLock {
    private static final String address = "127.0.0.1:2181";
    //锁的位置
    public static CuratorFramework cf = null;
    static{
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        cf = CuratorFrameworkFactory.builder()
                .connectString(address)
                .sessionTimeoutMs(2000)
                .retryPolicy(retryPolicy)
                .build();
        cf.start();
    }


    public static void main(String[] args) {
        //创建一个指定路径的分布式锁
        InterProcessMutex lock = createLock("/online-zk-lock");
        //创建复合锁
//        InterProcessLock lock1 = new InterProcessMutex(cf, "/online-zk-lock/lock1"); // 可重入锁
//        InterProcessLock lock2 = new InterProcessSemaphoreMutex(cf, "/online-zk-lock/lock2"); // 不可重入锁
//        InterProcessMultiLock multiLock = createMultiLock(Arrays.asList(lock1, lock2));
        new Thread("thread-1"){
            @Override
            public void run() {
                process(lock);
            }
        }.start();
        new Thread("thread-2"){
            @Override
            public void run() {
                process(lock);
            }
        }.start();
    }

    /***
     *
     * @return
     */
    public static InterProcessMutex createLock(String lockpath){
        InterProcessMutex lock = new InterProcessMutex(cf, lockpath);
        return lock;
    }

    /***
     * 同时获得多个锁
     * @param locks 要获得的多个锁
     * @return
     */
    public static InterProcessMultiLock createMultiLock(List<InterProcessLock> locks){
        InterProcessMultiLock lock = new InterProcessMultiLock(locks);//多锁对象
        return lock;
    }

    /***
     * 尝试获得指定路径的锁
     * @param lock
     */
    private static void process(InterProcessLock lock) {
        System.out.println(Thread.currentThread().getName() + " acquire");
        try {
            //尝试获得锁
            lock.acquire();
            System.out.println(Thread.currentThread().getName() + " acquire success");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + " release");
            try {
                //释放锁
                lock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + " release success");
    }
}
