package com.online.util;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ZooKeeperLock {

        private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

        private ZooKeeper zookeeper;
        private static CountDownLatch latch;

        public ZooKeeperLock() {
            try {
                this.zookeeper = new ZooKeeper("192.168.31.187:2181,192.168.31.19:2181,192.168.31.227:2181",
                        50000, new ZooKeeperWatcher(latch));
                try {
                    connectedSemaphore.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("ZooKeeper session established......");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /**
         * 获取分布式锁
         *
         * @param productId
         */
        public Boolean acquireDistributedLock(Long productId) {
            String path = "/product-lock-" + productId;

            try {
                //尝试创建一个临时节点
                zookeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                return true;
            } catch (Exception e) {
                while (true) {
                    try {
                        // 相当于是给node注册一个监听器，去看看这个监听器是否存在
                        Stat stat = zookeeper.exists(path, true);

                        if (stat != null) {
                            this.latch = new CountDownLatch(1);//阻塞等待在这
                            this.latch.await(1000, TimeUnit.MILLISECONDS);
                            this.latch = null;
                        }
                        zookeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                        return true;
                    } catch (Exception ee) {
                        continue;
                    }
                }

            }
        }

    /**
     * 释放掉一个分布式锁
     *
     * @param productId
     */
    public void releaseDistributedLock(Long productId) {
        String path = "/product-lock-" + productId;
        try {
            zookeeper.delete(path, -1);
            System.out.println("release the lock for product[id=" + productId + "]......");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ZooKeeperWatcher implements Watcher {
        private  CountDownLatch latch;
        public ZooKeeperWatcher( CountDownLatch latch){
            this.latch=latch;
        }
        public void process(WatchedEvent event) {
            System.out.println("Receive watched event: " + event.getState());

            if (Event.KeeperState.SyncConnected == event.getState()) {
                connectedSemaphore.countDown();
            }
            if (this.latch != null) {
                this.latch.countDown();
            }
        }

    }

    private static class Singleton {

        private static ZooKeeperLock instance;

        static {
            instance = new ZooKeeperLock();
        }

        public static ZooKeeperLock getInstance() {
            return instance;
        }

    }
    /**
     * 获取单例
     *
     * @return
     */
    public static ZooKeeperLock getInstance() {
        return Singleton.getInstance();
    }

    /**
     * 初始化单例的便捷方法
     */
    public static void init() {
        getInstance();
    }
}
